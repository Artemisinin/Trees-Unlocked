package com.artemis.trees_unlocked.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TreeFeature.class)
public interface GetTopPositionInvoker {
    @Invoker("getTopPosition")
    int invokeGetTopPosition(TestableWorld world, int height, BlockPos pos, TreeFeatureConfig config);
}