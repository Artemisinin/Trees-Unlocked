package com.artemis.trees_unlocked.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(FoliagePlacer.class)
public class FoliagePlacerMixin {

    @Redirect(method = "generateSquare", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ModifiableTestableWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private boolean applyWaterlogging(ModifiableTestableWorld world, BlockPos pos, BlockState state, int flags) {
        if (world.testBlockState(pos, (testState) -> testState.getFluidState().isEqualAndStill(Fluids.WATER)) && state.contains(Properties.WATERLOGGED))
        {
            return world.setBlockState(pos, state.with(Properties.WATERLOGGED, true), 19);
        }
        else return world.setBlockState(pos, state, 19);
    }
}