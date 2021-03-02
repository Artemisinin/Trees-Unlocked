package com.artemis.trees_unlocked.world.gen.feature;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;

import java.util.*;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.Structure;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.BitSetVoxelSet;
import net.minecraft.util.shape.VoxelSet;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.WorldAccess;
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

    private int getTopPosition(TestableWorld world, int height, BlockPos pos, TreeFeatureConfig config) {
        Mutable mutable = new Mutable();

        for(int i = 0; i <= height + 1; ++i) {
            int j = config.minimumSize.getRadius(height, i);

            for(int k = -j; k <= j; ++k) {
                for(int l = -j; l <= j; ++l) {
                    mutable.set(pos, k, i, l);
                    if (!canTreeReplace(world, mutable) || !config.ignoreVines && isVine(world, mutable)) {
                        return i - 2;
                    }
                }
            }
        }
        return height;
    }

    private static boolean isVine(TestableWorld world, BlockPos pos) {
        return world.testBlockState(pos, (state) -> state.isOf(Blocks.VINE));
    }

    private boolean generate(StructureWorldAccess world, Random random, BlockPos pos, Set<BlockPos> logPositions, Set<BlockPos> leavesPositions, BlockBox box, TreeFeatureConfig config) {
        int i = config.trunkPlacer.getHeight(random);
        int j = config.foliagePlacer.getRandomHeight(random, i, config);
        int k = i - j;
        int l = config.foliagePlacer.getRandomRadius(random, k);
        int r;
        if (pos.getY() >= world.getBottomY() + 1 && pos.getY() + i + 1 <= world.getTopY()) {
            if (!canPlaceTreeOn(world, pos.down())) {
                return false;
            } else {
                OptionalInt optionalInt = config.minimumSize.getMinClippedHeight();
                r = this.getTopPosition(world, i, pos, config);
                if (r >= i || optionalInt.isPresent() && r >= optionalInt.getAsInt()) {
                    List<TreeNode> list = config.trunkPlacer.generate(world, random, r, pos, logPositions, box, config);
                    list.forEach((node) -> config.foliagePlacer.generate(world, random, config, r, node, j, l, leavesPositions, box));
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

            VoxelSet voxelSet = this.placeLogsAndLeaves(structureWorldAccess, blockBox, set, set3);
            Structure.updateCorner(structureWorldAccess, 3, voxelSet, blockBox.minX, blockBox.minY, blockBox.minZ);
            return true;
        } else {
            return false;
        }
    }

    private VoxelSet placeLogsAndLeaves(WorldAccess world, BlockBox box, Set<BlockPos> logs, Set<BlockPos> leaves) {
        List<Set<BlockPos>> list = Lists.newArrayList();
        VoxelSet voxelSet = new BitSetVoxelSet(box.getBlockCountX(), box.getBlockCountY(), box.getBlockCountZ());

        for(int j = 0; j < 6; ++j) {
            list.add(Sets.newHashSet());
        }

        Mutable mutable = new Mutable();
        Iterator var9 = Lists.newArrayList(leaves).iterator();

        BlockPos blockPos2;
        while(var9.hasNext()) {
            blockPos2 = (BlockPos)var9.next();
            if (box.contains(blockPos2)) {
                voxelSet.set(blockPos2.getX() - box.minX, blockPos2.getY() - box.minY, blockPos2.getZ() - box.minZ);
            }
        }

        var9 = Lists.newArrayList(logs).iterator();

        while(var9.hasNext()) {
            blockPos2 = (BlockPos)var9.next();
            if (box.contains(blockPos2)) {
                voxelSet.set(blockPos2.getX() - box.minX, blockPos2.getY() - box.minY, blockPos2.getZ() - box.minZ);
            }

            Direction[] var11 = Direction.values();
            int var12 = var11.length;

            for(int var13 = 0; var13 < var12; ++var13) {
                Direction direction = var11[var13];
                mutable.set(blockPos2, direction);
                if (!logs.contains(mutable)) {
                    BlockState blockState = world.getBlockState(mutable);
                    if (blockState.contains(Properties.DISTANCE_1_7)) {
                        (list.get(0)).add(mutable.toImmutable());
                        setBlockStateWithoutUpdatingNeighbors(world, mutable, blockState.with(Properties.DISTANCE_1_7, 1));
                        if (box.contains(mutable)) {
                            voxelSet.set(mutable.getX() - box.minX, mutable.getY() - box.minY, mutable.getZ() - box.minZ);
                        }
                    }
                }
            }
        }

        for(int k = 1; k < 6; ++k) {
            Set<BlockPos> set = list.get(k - 1);
            Set<BlockPos> set2 = list.get(k);
            Iterator var25 = set.iterator();

            while(var25.hasNext()) {
                BlockPos blockPos3 = (BlockPos)var25.next();
                if (box.contains(blockPos3)) {
                    voxelSet.set(blockPos3.getX() - box.minX, blockPos3.getY() - box.minY, blockPos3.getZ() - box.minZ);
                }

                Direction[] var27 = Direction.values();
                int var28 = var27.length;

                for(int var16 = 0; var16 < var28; ++var16) {
                    Direction direction2 = var27[var16];
                    mutable.set(blockPos3, direction2);
                    if (!set.contains(mutable) && !set2.contains(mutable)) {
                        BlockState blockState2 = world.getBlockState(mutable);
                        if (blockState2.contains(Properties.DISTANCE_1_7)) {
                            int l = blockState2.get(Properties.DISTANCE_1_7);
                            if (l > k + 1) {
                                BlockState blockState3 = blockState2.with(Properties.DISTANCE_1_7, k + 1);
                                setBlockStateWithoutUpdatingNeighbors(world, mutable, blockState3);
                                if (box.contains(mutable)) {
                                    voxelSet.set(mutable.getX() - box.minX, mutable.getY() - box.minY, mutable.getZ() - box.minZ);
                                }

                                set2.add(mutable.toImmutable());
                            }
                        }
                    }
                }
            }
        }
        return voxelSet;
    }
}