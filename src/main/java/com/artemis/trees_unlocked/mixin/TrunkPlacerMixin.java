package com.artemis.trees_unlocked.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TrunkPlacer.class)
public class TrunkPlacerMixin {

    @Redirect(method = "setToDirt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/trunk/TrunkPlacer;canGenerate(Lnet/minecraft/world/TestableWorld;Lnet/minecraft/util/math/BlockPos;)Z"))
    private static boolean notTurnToDirt(TestableWorld world, BlockPos pos) {
        return !world.testBlockState(pos, (state) -> state.isOf(Blocks.GRASS_BLOCK)) && !world.testBlockState(pos, (state) -> state.isOf(Blocks.MYCELIUM));
    }
}
