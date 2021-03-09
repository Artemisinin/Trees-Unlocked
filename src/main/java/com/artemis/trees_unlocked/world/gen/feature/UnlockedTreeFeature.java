package com.artemis.trees_unlocked.world.gen.feature;

import com.artemis.trees_unlocked.mixin.GetTopPositionInvoker;
import com.artemis.trees_unlocked.mixin.PlaceLogsAndLeavesInvoker;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;

import java.util.*;

import net.minecraft.fluid.Fluids;
import net.minecraft.structure.Structure;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelSet;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.foliage.FoliagePlacer.TreeNode;

import static com.artemis.trees_unlocked.TreesUnlocked.VALID_GROUND_BLOCKS;

public class UnlockedTreeFeature extends TreeFeature {

    public UnlockedTreeFeature(Codec<TreeFeatureConfig> codec) {
        super(codec);
    }

    // Allows this to be placed just about anywhere.  Edit the json to configure allowable blocks.
    private static boolean canPlaceTreeOn(TestableWorld world, BlockPos pos) {
        return world.testBlockState(pos, (state) -> state.isIn(VALID_GROUND_BLOCKS));
    }

    // Uses config.maxWaterDepth to make sure trees aren't placed in water that is too deep.  If config.maxWaterDepth is <0, the check will be skipped.
    private static boolean isIllegallySubmerged(TestableWorld world, BlockPos pos, TreeFeatureConfig config) {
        if (config.maxWaterDepth >= 0) {
            int blocksUp = 0;
            while (blocksUp < (config.maxWaterDepth + 1)) {
                BlockPos pos2 = pos.offset(Direction.Axis.Y, blocksUp);
                // If a non-water block is encountered, return false since it is not submerged. Currently allows *flowing* water.
                if (!world.testBlockState(pos2, (state) -> state.getFluidState().isEqualAndStill(Fluids.WATER))) {
                    return false;
                // Otherwise it was a water block, so increment to check the next block.
                } else {
                    blocksUp++;
                }
            }
            // We ran through the maximum water height allowed and found more water above, so this would be illegal submerged, return true.
            return true;
        // config.maxWaterDepth apparently was null, so the check is invalid and the tree would not be illegally submerged, return false.
        } else {
            return false;
        }
    }

    private boolean generate(StructureWorldAccess world, Random random, BlockPos pos, Set<BlockPos> logPositions, Set<BlockPos> leavesPositions, BlockBox box, TreeFeatureConfig config) {
        int i = config.trunkPlacer.getHeight(random);
        int j = config.foliagePlacer.getRandomHeight(random, i, config);
        int k = i - j;
        int l = config.foliagePlacer.getRandomRadius(random, k);
        int r;

        if (pos.getY() >= world.getBottomY() + 1 && pos.getY() + i + 1 <= world.getTopY()) {
            // Check to make sure the tree is not being placed too deep in the water.
            if (!canPlaceTreeOn(world, pos.down()) || isIllegallySubmerged(world, pos, config)) {
                return false;
            } else {
                OptionalInt optionalInt = config.minimumSize.getMinClippedHeight();
                r = ((GetTopPositionInvoker) this).invokeGetTopPosition(world, i, pos, config);
                if (r >= i || optionalInt.isPresent() && r >= optionalInt.getAsInt()) {
                    List<TreeNode> list = config.trunkPlacer.generate(world, random, r, pos, logPositions, box, config);
                    list.forEach((node) -> {
                        config.foliagePlacer.generate(world, random, config, r, node, j, l, leavesPositions, box);
                    });
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public final boolean generate(FeatureContext<TreeFeatureConfig> context) {
        StructureWorldAccess structureWorldAccess = context.getWorld();
        Random random = context.getRandom();
        BlockPos blockPos = context.getOrigin();
        TreeFeatureConfig treeFeatureConfig = context.getConfig();
        Set<BlockPos> set = Sets.newHashSet();
        Set<BlockPos> set2 = Sets.newHashSet();
        Set<BlockPos> set3 = Sets.newHashSet();
        BlockBox blockBox = BlockBox.empty();
        boolean bl = this.generate(structureWorldAccess, random, blockPos, set, set2, blockBox, treeFeatureConfig);
        if (blockBox.minX <= blockBox.maxX && bl && !set.isEmpty()) {
            if (!treeFeatureConfig.decorators.isEmpty()) {
                List<BlockPos> list = Lists.newArrayList(set);
                List<BlockPos> list2 = Lists.newArrayList(set2);
                list.sort(Comparator.comparingInt(Vec3i::getY));
                list2.sort(Comparator.comparingInt(Vec3i::getY));
                treeFeatureConfig.decorators.forEach((decorator) -> decorator.generate(structureWorldAccess, random, list, list2, set3, blockBox));
            }

            VoxelSet voxelSet = ((PlaceLogsAndLeavesInvoker) this).invokePlaceLogsAndLeaves(structureWorldAccess, blockBox, set, set3);
            Structure.updateCorner(structureWorldAccess, 3, voxelSet, blockBox.minX, blockBox.minY, blockBox.minZ);
            return true;
        } else {
            return false;
        }
    }
}