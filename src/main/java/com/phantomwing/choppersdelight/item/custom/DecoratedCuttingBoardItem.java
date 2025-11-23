package com.phantomwing.choppersdelight.item.custom;

import com.phantomwing.choppersdelight.component.ModDataComponents;
import com.phantomwing.choppersdelight.renderer.DecoratedCuttingBoardItemStackRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.item.FuelBlockItem;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.List;
import java.util.function.Consumer;

public class DecoratedCuttingBoardItem extends FuelBlockItem {

    public DecoratedCuttingBoardItem(Block block, Properties properties, int burnTime) {
        super(block, properties, burnTime);
    }

    public static ItemStack getCuttingBoardStack(ItemStack stack) {
        if (stack.getItem() instanceof DecoratedCuttingBoardItem) {
            CompoundTag tag = stack.getOrCreateTagElement(ModDataComponents.DECORATED_CUTTING_BOARD_DATA);
            CompoundTag cuttingBoardTag = tag.getCompound(ModDataComponents.DECORATED_CUTTING_BOARD_CUTTING_BOARD_DATA);
            ItemStack cuttingBoardStack = ItemStack.of(cuttingBoardTag);

            return cuttingBoardStack.copy();
        }

        return new ItemStack(ModItems.CUTTING_BOARD.get());
    }

    public static ItemStack getBannerStack(ItemStack stack) {
        if (stack.getItem() instanceof DecoratedCuttingBoardItem) {
            CompoundTag tag = stack.getOrCreateTagElement(ModDataComponents.DECORATED_CUTTING_BOARD_DATA);
            CompoundTag bannerTag = tag.getCompound(ModDataComponents.DECORATED_CUTTING_BOARD_BANNER_DATA);
            ItemStack bannerStack = ItemStack.of(bannerTag);

            return bannerStack.copy();
        }

        return new ItemStack(Items.WHITE_BANNER);
    }

    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(getCuttingBoardStack(stack).getItem());
        return "block." + id.getNamespace() + "." + id.getPath();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);

        // Add Banner tooltip.
        ItemStack banner = getBannerStack(stack);
        if (banner != null && !banner.isEmpty()) {

            if (banner.hasCustomHoverName()) {
                tooltipComponents.add(banner.getHoverName().copy().withStyle(ChatFormatting.GRAY));
            } else {
                // Show the banner's item name and then any banner pattern details
                tooltipComponents.add(Component.translatable(banner.getDescriptionId()).withStyle(ChatFormatting.GRAY));
                BannerItem.appendHoverTextFromBannerBlockEntityTag(banner, tooltipComponents);
            }
        }
    }

    // In your item class
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer =
                    new DecoratedCuttingBoardItemStackRenderer(
                            Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                            Minecraft.getInstance().getEntityModels()
                    );

            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }
}