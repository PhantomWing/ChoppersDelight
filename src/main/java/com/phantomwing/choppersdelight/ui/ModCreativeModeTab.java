package com.phantomwing.choppersdelight.ui;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.component.ModDataComponents;
import com.phantomwing.choppersdelight.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChoppersDelight.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MOD_TAB =
            CREATIVE_MODE_TABS.register(ChoppersDelight.MOD_ID + "_tab", () -> CreativeModeTab.builder()
                    .icon(ModCreativeModeTab::getTabIcon)
                    .title(Component.translatable(("itemGroup." + ChoppersDelight.MOD_ID)))
                    .displayItems(ModCreativeModeTab::displayItems)
                    .build());

    public static ItemStack getTabIcon() {
        return getDecoratedCuttingBoard(ModBlocks.DARK_OAK_CUTTING_BOARD, Items.GREEN_BANNER, BannerPatterns.CREEPER, DyeColor.BLACK);
    }

    public static ItemStack getDecoratedCuttingBoard(RegistryObject<Block> board, Item banner, ResourceKey<BannerPattern> pattern, DyeColor patternColor) {
        Level level = Minecraft.getInstance().level;

        if (level != null) {
            // Generate base items
            ItemStack cuttingBoard = new ItemStack(board.get());
            ItemStack bannerStack = new ItemStack(banner);

            // Contruct a patterns tag.
            BannerPattern.Builder builder = new BannerPattern.Builder();
            builder.addPattern(pattern, patternColor);
            ListTag patternsList = builder.toListTag();

            // Prepare BlockEntityTag and Patterns list
            CompoundTag blockEntityTag = bannerStack.getOrCreateTagElement("BlockEntityTag");
            blockEntityTag.put("Patterns", patternsList);

            // Generate the final item
            ItemStack itemStack = new ItemStack(ModItems.DECORATED_CUTTING_BOARD.get());
            CompoundTag decoratedData = itemStack.getOrCreateTagElement(ModDataComponents.DECORATED_CUTTING_BOARD_DATA);
            decoratedData.put(ModDataComponents.DECORATED_CUTTING_BOARD_CUTTING_BOARD_DATA, cuttingBoard.save(new CompoundTag()));
            decoratedData.put(ModDataComponents.DECORATED_CUTTING_BOARD_BANNER_DATA, bannerStack.save(new CompoundTag()));

            return itemStack;
        }

        // Fallback.
        return new ItemStack(ModBlocks.DECORATED_CUTTING_BOARD.get());
    }

    public static void displayItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        displayModItems(parameters, output);
        displayBiomesOPlentyItems(parameters, output);
        displayBiomesWeveGoneItems(parameters, output);
        // EveryCompat-generated cutting boards are inserted into this tab automatically via WoodGood.setTabKey(...).

        // Add some preconfigured designs.
        output.accept(getDecoratedCuttingBoard(ModBlocks.CHERRY_CUTTING_BOARD, Items.WHITE_BANNER, BannerPatterns.FLOWER, DyeColor.PINK));
        output.accept(getDecoratedCuttingBoard(ModBlocks.DARK_OAK_CUTTING_BOARD, Items.GREEN_BANNER, BannerPatterns.CREEPER, DyeColor.BLACK));
        output.accept(getDecoratedCuttingBoard(ModBlocks.CRIMSON_CUTTING_BOARD, Items.BLACK_BANNER, BannerPatterns.SKULL, DyeColor.WHITE));
        output.accept(getDecoratedCuttingBoard(ModBlocks.WARPED_CUTTING_BOARD, Items.BLACK_BANNER, BannerPatterns.MOJANG, DyeColor.CYAN));
    }

    public static void displayModItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        // Add items to this tab.
        ModItems.CREATIVE_TAB_ITEMS.forEach((item) -> {
            output.accept(item.get());

            // Add the Farmer's Delight "Cutting Board" right after the Oak Cutting Board.
            if (ItemStack.isSameItem(item.get().getDefaultInstance(), new ItemStack(ModItems.OAK_CUTTING_BOARD.get())))
            {
                output.accept(new ItemStack(vectorwing.farmersdelight.common.registry.ModItems.CUTTING_BOARD.get()));
            }
        });
    }

    public static void displayBiomesOPlentyItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        if (!Compatibility.IsBiomesOPlentyLoaded()) {
            return;
        }

        // Add items to this tab.
        ModItems.BIOMES_O_PLENTY_CREATIVE_TAB_ITEMS.forEach((item) -> {
            output.accept(item.get());
        });
    }

    public static void displayBiomesWeveGoneItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        if (!Compatibility.IsBiomesWeveGoneLoaded()) {
            return;
        }

        // Add items to this tab.
        ModItems.BIOMES_WEVE_GONE_CREATIVE_TAB_ITEMS.forEach((item) -> {
            output.accept(item.get());
        });
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}