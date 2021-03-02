package com.artemis.trees_unlocked.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

import static com.artemis.trees_unlocked.TreesUnlocked.CAVE_GLOWLEAF;

@Environment(EnvType.CLIENT)
public class TreesUnlockedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        //BlockRenderLayerMap.INSTANCE.putBlock(CAVE_GLOWLEAF, RenderLayer.getCutout());
    }
}
