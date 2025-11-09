package com.phantomwing.choppersdelight.mixin;

import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;
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

            ModBlocks.MINECRAFT_CUTTING_BOARDS.forEach(b -> validBlocks.add(b.get()));

            if (Compatibility.IsBiomesOPlentyLoaded()) {
                ModBlocks.BIOMES_O_PLENTY_CUTTING_BOARDS.forEach(b -> validBlocks.add(b.get()));
            }

            if (Compatibility.IsBiomesWeveGoneLoaded()) {
                ModBlocks.BIOMES_WEVE_GONE_CUTTING_BOARDS.forEach(b -> validBlocks.add(b.get()));
            }
        }
    }
}