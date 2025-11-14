package com.phantomwing.choppersdelight.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class CuttingBoardTranslationMixin {
    @Inject(method = "getHoverName", at = @At("RETURN"), cancellable = true)
    private void onGetHoverName(CallbackInfoReturnable<Component> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getDescriptionId().equals("block.farmersdelight.cutting_board")) {
            cir.setReturnValue(Component.translatable("block.choppersdelight.spruce_cutting_board"));
        }
    }
}