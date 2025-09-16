package com.phantomwing.choppersdelight.ui;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.component.DecoratedCuttingBoardData;
import com.phantomwing.choppersdelight.component.ModDataComponents;
import com.phantomwing.choppersdelight.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChoppersDelight.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB =
        CREATIVE_MODE_TABS.register(ChoppersDelight.MOD_ID + "_tab", () -> CreativeModeTab.builder()
            .icon(ModCreativeModeTab::getTabIcon)
            .title(Component.translatable(("itemGroup." + ChoppersDelight.MOD_ID)))
            .displayItems(ModCreativeModeTab::displayItems)
            .build());

    public static ItemStack getTabIcon() {
        return getDecoratedCuttingBoard(ModBlocks.DARK_OAK_CUTTING_BOARD, Items.GREEN_BANNER, BannerPatterns.CREEPER, DyeColor.BLACK);
    }

    public static ItemStack getDecoratedCuttingBoard(DeferredBlock<Block> board, Item banner, ResourceKey<BannerPattern> pattern, DyeColor patternColor) {
        Level level = Minecraft.getInstance().level;

        if (level != null) {
            // Generate a base cutting board
            ItemStack cuttingBoard = new ItemStack(board.get());
            Registry<BannerPattern> bannerPatternRegistry = level.registryAccess().registryOrThrow(Registries.BANNER_PATTERN);
            Holder<BannerPattern> patternHolder = bannerPatternRegistry.getHolderOrThrow(pattern);

            // Generate a banner
            ItemStack bannerStack = new ItemStack(banner);
            BannerPatternLayers layers = new BannerPatternLayers.Builder()
                    .add(patternHolder, patternColor)
                    .build();
            bannerStack.set(DataComponents.BANNER_PATTERNS, layers);

            // Generate the final item
            ItemStack itemStack = new ItemStack(ModBlocks.DECORATED_CUTTING_BOARD.get());
            DecoratedCuttingBoardData cuttingBoardData = new DecoratedCuttingBoardData(cuttingBoard, bannerStack);
            itemStack.set(ModDataComponents.DECORATED_CUTTING_BOARD_DATA.get(), cuttingBoardData);

            return itemStack;
        }

        // Fallback.
        return new ItemStack(ModBlocks.DECORATED_CUTTING_BOARD.get());
    }

    public static void displayItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        displayModItems(parameters, output);
        displayBiomesOPlentyItems(parameters, output);
        displayBiomesWeveGoneItems(parameters, output);

        // Add some preconfigured designs.
        output.accept(getDecoratedCuttingBoard(ModBlocks.CHERRY_CUTTING_BOARD, Items.WHITE_BANNER, BannerPatterns.FLOWER, DyeColor.PINK));
        output.accept(getDecoratedCuttingBoard(ModBlocks.DARK_OAK_CUTTING_BOARD, Items.GREEN_BANNER, BannerPatterns.CREEPER, DyeColor.BLACK));
        output.accept(getDecoratedCuttingBoard(ModBlocks.CRIMSON_CUTTING_BOARD, Items.BLACK_BANNER, BannerPatterns.SKULL, DyeColor.WHITE));
        output.accept(getDecoratedCuttingBoard(ModBlocks.WARPED_CUTTING_BOARD, Items.BLACK_BANNER, BannerPatterns.FLOW, DyeColor.CYAN));
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

    public static Holder<BannerPattern> getCreeperPattern() {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return null; // not in a world yet
        }

        Registry<BannerPattern> registry =
                level.registryAccess().registryOrThrow(Registries.BANNER_PATTERN);

        return registry.getHolderOrThrow(BannerPatterns.CREEPER);
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
