package com.artemis.trees_unlocked.world.gen.feature;

import com.artemis.trees_unlocked.TreesUnlocked;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

public class TreesUnlockedConfiguredFeatures {

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<?, ?> configuredFeature) {
        return (ConfiguredFeature) Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, configuredFeature);
    }

    public static ConfiguredFeature<?, ?> PORIFERAN;
    public static ConfiguredFeature<?, ?> SCATTERED_PORIFERANS;

    public static void registerConfiguredFeatures() {

        PORIFERAN = register("trees_unlocked:poriferan", TreesUnlocked.UNLOCKED_TREE_FEATURE.configure(
                (new TreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.MUSHROOM_STEM.getDefaultState()),
                        new SimpleBlockStateProvider(TreesUnlocked.PORIFERAN_CHUNK.getDefaultState()),
                        new BlobFoliagePlacer(UniformIntDistribution.of(2, 0), UniformIntDistribution.of(1, 1),4),
                        new StraightTrunkPlacer(4, 1, 1),
                        new TwoLayersFeatureSize(3, 3, 1))).heightmap(Heightmap.Type.MOTION_BLOCKING).build()));

        SCATTERED_PORIFERANS = register("trees_unlocked:scattered_poriferans", PORIFERAN.rangeOf(YOffset.aboveBottom(80), YOffset.fixed(50)).spreadHorizontally().repeat(10));
    }
}