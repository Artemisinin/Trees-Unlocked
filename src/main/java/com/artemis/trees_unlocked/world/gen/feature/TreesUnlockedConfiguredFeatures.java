package com.artemis.trees_unlocked.world.gen.feature;

import com.artemis.trees_unlocked.TreesUnlocked;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.BushFoliagePlacer;
import net.minecraft.world.gen.foliage.PineFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import org.lwjgl.system.CallbackI;
import sun.java2d.pipe.SpanShapeRenderer;

public class TreesUnlockedConfiguredFeatures {

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<?, ?> configuredFeature) {
        return (ConfiguredFeature) Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, configuredFeature);
    }

    public static ConfiguredFeature<?, ?> PORIFERAN;
    public static ConfiguredFeature<?, ?> SCATTERED_PORIFERANS;
    public static ConfiguredFeature<?, ?> SWAMP_OAK_SHRUB;
    public static ConfiguredFeature<?, ?> SWAMP_OAK_SHRUBS;

    public static void registerConfiguredFeatures() {

        PORIFERAN = register("trees_unlocked:poriferan", TreesUnlocked.UNLOCKED_TREE_FEATURE.configure(
                (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.MUSHROOM_STEM.getDefaultState()),
                        new SimpleBlockStateProvider(TreesUnlocked.PORIFERAN_CHUNK.getDefaultState()),
                        new PineFoliagePlacer(UniformIntDistribution.of(1), UniformIntDistribution.of(0),UniformIntDistribution.of(4)),
                        new StraightTrunkPlacer(4, 2, 0),
                        new TwoLayersFeatureSize(1, 1, 1))).heightmap(Heightmap.Type.MOTION_BLOCKING).maxWaterDepth(-1).build()));

        SCATTERED_PORIFERANS = register("trees_unlocked:scattered_poriferans", PORIFERAN.rangeOf(YOffset.fixed(30), YOffset.fixed(55)).spreadHorizontally().repeat(20));

        SWAMP_OAK_SHRUB = register("trees_unlocked:swamp_oak_shrub", TreesUnlocked.UNLOCKED_TREE_FEATURE.configure(
                new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()),
                        new SimpleBlockStateProvider(Blocks.OAK_LEAVES.getDefaultState()),
                        new BushFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(1), 2),
                        new StraightTrunkPlacer(2,1,0),
                        new TwoLayersFeatureSize(1,0,1)).maxWaterDepth(2).build()));

        SWAMP_OAK_SHRUBS = register("trees_unlocked:swamp_oak_shrubs", SWAMP_OAK_SHRUB.rangeOf(YOffset.fixed(60), YOffset.fixed(62)).spreadHorizontally().repeat(10));
    }
}