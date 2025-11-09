package com.phantomwing.choppersdelight.item.custom;

import com.phantomwing.choppersdelight.component.DecoratedCuttingBoardData;
import com.phantomwing.choppersdelight.component.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.item.FuelBlockItem;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.List;

public class DecoratedCuttingBoardItem extends FuelBlockItem {

    public DecoratedCuttingBoardItem(Block block, Properties properties, int burnTime) {
        super(block, properties, burnTime);
    }

    public static ItemStack getCuttingBoardStack(ItemStack stack) {
        if (stack.getItem() instanceof DecoratedCuttingBoardItem) {
            DecoratedCuttingBoardData data = stack.get(ModDataComponents.DECORATED_CUTTING_BOARD_DATA.get());

            if (data != null) {
                return data.cuttingBoard().copy();
            }
        }

        return new ItemStack(ModItems.CUTTING_BOARD.get());
    }

    public static ItemStack getBannerStack(ItemStack stack) {
        if (stack.getItem() instanceof DecoratedCuttingBoardItem) {
            DecoratedCuttingBoardData data = stack.get(ModDataComponents.DECORATED_CUTTING_BOARD_DATA.get());

            if (data != null) {
                return data.banner().copy();
            }
        }

        return new ItemStack(Items.WHITE_BANNER);
    }

    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(getCuttingBoardStack(stack).getItem());
        return "block." + id.getNamespace() + "." + id.getPath();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        // Add Banner tooltip.
        ItemStack banner = getBannerStack(stack);
        if (!banner.isEmpty()) {
            Component customName = banner.get(DataComponents.CUSTOM_NAME);
            if (customName != null) {
                // Display the alternate custom name.
                tooltipComponents.add(
                        customName.copy().withStyle(ChatFormatting.GRAY)
                );
            } else {
                Component itemName = banner.get(DataComponents.ITEM_NAME);
                if (itemName != null)
                {
                    // Display the alternate item name.
                    tooltipComponents.add(
                            itemName.copy().withStyle(ChatFormatting.GRAY)
                    );
                } else {
                    // Display the contents of the Banner.
                    tooltipComponents.add(
                        Component.translatable(banner.getDescriptionId()).withStyle(ChatFormatting.GRAY)
                    );

                    BannerItem.appendHoverTextFromBannerBlockEntityTag(banner, tooltipComponents);
                }
            }
        }
    }
}