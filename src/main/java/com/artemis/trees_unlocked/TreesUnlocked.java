package com.artemis.trees_unlocked;

import com.artemis.trees_unlocked.world.gen.feature.TreesUnlockedConfiguredFeatures;
import com.artemis.trees_unlocked.world.gen.feature.UnlockedTreeFeature;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.lwjgl.system.CallbackI;


public class TreesUnlocked implements ModInitializer {

    public static Tag.Identified<Block> VALID_GROUND_BLOCKS;
    public static Feature<TreeFeatureConfig> UNLOCKED_TREE_FEATURE;
    public static final Block CAVE_GLOWLEAF = new Block(FabricBlockSettings.of(Material.LEAVES).hardness(0.2f).breakByHand(true).nonOpaque().luminance(7));
    public static final Block PORIFERAN_CHUNK = new Block(FabricBlockSettings.of(Material.STONE).hardness(0.5f).requiresTool().strength(0.8F));

    @Override
    public void onInitialize() {

        // Add the blocks and blockitems used for trees.
        Registry.register(Registry.BLOCK, new Identifier("trees_unlocked", "cave_glowleaf"), CAVE_GLOWLEAF);
        Registry.register(Registry.ITEM, new Identifier("trees_unlocked", "cave_glowleaf"), new BlockItem(CAVE_GLOWLEAF, new Item.Settings().group(ItemGroup.DECORATIONS)));
        Registry.register(Registry.BLOCK, new Identifier("trees_unlocked", "poriferan_chunk"), PORIFERAN_CHUNK);
        Registry.register(Registry.ITEM, new Identifier("trees_unlocked", "poriferan_chunk"), new BlockItem(PORIFERAN_CHUNK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));

        // Blocks that you want trees to be able to be placed upon should be added to this block tag.
        VALID_GROUND_BLOCKS = (Tag.Identified<Block>) TagRegistry.block(new Identifier("trees_unlocked", "valid_ground_blocks"));

        // Register the unlocked tree feature.
        UNLOCKED_TREE_FEATURE = Registry.register(Registry.FEATURE, new Identifier("trees_unlocked", "unlocked_tree_feature"), new UnlockedTreeFeature(TreeFeatureConfig.CODEC));

        // Register cave trees and tree placers.
        TreesUnlockedConfiguredFeatures.registerConfiguredFeatures();

        // Add cave trees to the world.
        BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.OCEAN), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier("trees_unlocked", "scattered_poriferans")));
    }
}
