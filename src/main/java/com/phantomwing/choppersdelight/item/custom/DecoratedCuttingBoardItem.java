package com.phantomwing.choppersdelight.item.custom;

import com.phantomwing.choppersdelight.component.DecoratedCuttingBoardData;
import com.phantomwing.choppersdelight.component.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.item.FuelBlockItem;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

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
    public @NotNull Component getName(@NotNull ItemStack stack) {
        // Named after the wrapped board, while still respecting any custom name/translation set on our
        // decorated item itself.
        Component itemName = ownComponent(stack, DataComponents.ITEM_NAME);
        if (itemName != null) {
            return itemName;
        }
        return getCuttingBoardStack(stack).getHoverName();
    }

    /**
     * A component the stack carries itself rather than from its item: from 1.21.2 every item has an item name.
     */
    private static @Nullable Component ownComponent(ItemStack stack, DataComponentType<Component> type) {
        for (Map.Entry<DataComponentType<?>, Optional<?>> entry : stack.getComponentsPatch().entrySet()) {
            if (entry.getKey() == type) {
                return entry.getValue().map(Component.class::cast).orElse(null);
            }
        }
        return null;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull TooltipDisplay tooltipDisplay, @NotNull Consumer<Component> tooltipAdder, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, tooltipFlag);

        // Add Banner tooltip.
        ItemStack banner = getBannerStack(stack);
        if (!banner.isEmpty()) {
            Component customName = ownComponent(banner, DataComponents.CUSTOM_NAME);
            if (customName != null) {
                // Display the alternate custom name.
                tooltipAdder.accept(
                        customName.copy().withStyle(ChatFormatting.GRAY)
                );
            } else {
                Component itemName = ownComponent(banner, DataComponents.ITEM_NAME);
                if (itemName != null)
                {
                    // Display the alternate item name.
                    tooltipAdder.accept(
                            itemName.copy().withStyle(ChatFormatting.GRAY)
                    );
                } else {
                    // Display the contents of the Banner.
                    tooltipAdder.accept(
                        Component.translatable(banner.getItem().getDescriptionId()).withStyle(ChatFormatting.GRAY)
                    );

                    banner.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
                            .addToTooltip(context, tooltipAdder, tooltipFlag, banner);
                }
            }
        }
    }
}
