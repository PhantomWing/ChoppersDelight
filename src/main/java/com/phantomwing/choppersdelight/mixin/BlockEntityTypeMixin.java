package com.phantomwing.choppersdelight.mixin;

import com.phantomwing.choppersdelight.utils.BlockUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import com.phantomwing.choppersdelight.mixin.accessor.BlockEntityTypeAccessor;

import java.util.Set;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {
    @Unique
    private static boolean choppersdelight$blocksAdded = false;

    @Inject(method = "isValid", at = @At("HEAD"))
    private void addCustomBlocks(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (!choppersdelight$blocksAdded && (Object)this == ModBlockEntityTypes.CUTTING_BOARD.get()) {
            choppersdelight$blocksAdded = true;

            Set<Block> validBlocks = ((BlockEntityTypeAccessor)this).getValidBlocks();
            BlockUtils.getCuttingBoards().forEach(validBlocks::add);
        }
    }
}
