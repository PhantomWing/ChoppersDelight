package com.phantomwing.choppersdelight.item.custom;

import com.phantomwing.choppersdelight.component.DecoratedCuttingBoardData;
import com.phantomwing.choppersdelight.component.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
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
        // Delegate to the wrapped cutting board item so mods (notably Every Compat) that resolve their
        // block name through a custom translation key — instead of the raw `block.<namespace>.<path>` key —
        // produce the right id.
        ItemStack boardStack = getCuttingBoardStack(stack);
        return boardStack.getItem().getDescriptionId(boardStack);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        // getName drives the displayed name. For EveryCompat-generated cutting boards there is no direct
        // `block.<namespace>.<path>` lang entry — their name comes from Moonlight/Every Compat applying
        // the `block_type.choppersdelight.cutting_board` template to the wood type at runtime. Delegating
        // to the wrapped board's hover name picks that up automatically, while still respecting any
        // custom name/translation set on our decorated item itself.
        Component customName = stack.get(DataComponents.CUSTOM_NAME);
        if (customName != null) {
            return customName;
        }
        Component itemName = stack.get(DataComponents.ITEM_NAME);
        if (itemName != null) {
            return itemName;
        }
        return getCuttingBoardStack(stack).getHoverName();
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