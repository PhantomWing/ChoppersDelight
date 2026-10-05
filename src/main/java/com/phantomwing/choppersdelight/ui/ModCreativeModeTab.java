package com.phantomwing.choppersdelight.ui;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.EveryCompatSetup;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.component.DecoratedCuttingBoardData;
import com.phantomwing.choppersdelight.component.ModDataComponents;
import com.phantomwing.choppersdelight.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChoppersDelight.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB =
        CREATIVE_MODE_TABS.register(ChoppersDelight.MOD_ID + "_tab", () -> CreativeModeTab.builder()
            .icon(ModCreativeModeTab::getTabIcon)
            .title(Component.translatable(("itemGroup." + ChoppersDelight.MOD_ID)))
            .displayItems(ModCreativeModeTab::displayItems)
            .build());

    /**
     * Returns the client-side Level, or null if not on the client or not in a world.
     * This avoids referencing Minecraft directly in method bodies, which would
     * trigger RuntimeDistCleaner when this class is loaded on a dedicated server.
     */
    private static Level getClientLevel() {
        if (!FMLEnvironment.dist.isClient()) {
            return null;
        }
        return ClientHelper.getLevel();
    }

    public static ItemStack getTabIcon() {
        Level level = getClientLevel();

        return Optional.ofNullable(level)
                .flatMap(l -> getDecoratedCuttingBoard(l.registryAccess(), ModBlocks.DARK_OAK_CUTTING_BOARD, Items.GREEN_BANNER, BannerPatterns.CREEPER, DyeColor.BLACK))
                .orElseGet(() -> new ItemStack(ModBlocks.DECORATED_CUTTING_BOARD.get()));
    }

    /**
     * Empty when the registries have no such banner pattern. Banner patterns are a datapack registry, so they
     * come from whoever builds the tab - on a server there is no client level to read them from.
     */
    public static Optional<ItemStack> getDecoratedCuttingBoard(HolderLookup.Provider registries, DeferredBlock<Block> board, Item banner, ResourceKey<BannerPattern> pattern, DyeColor patternColor) {
        return registries.lookup(Registries.BANNER_PATTERN)
                .flatMap(patterns -> patterns.get(pattern))
                .map(patternHolder -> {
                    // Generate a base cutting board
                    ItemStack cuttingBoard = new ItemStack(board.get());

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
                });
    }

    public static void displayItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        displayModItems(parameters, output);
        displayBiomesOPlentyItems(parameters, output);
        displayBiomesWeveGoneItems(parameters, output);

        // Add some preconfigured designs. A design whose pattern is missing is left out: a plain stand-in for each
        // would be the same stack four times, which the tab refuses.
        HolderLookup.Provider registries = parameters.holders();
        getDecoratedCuttingBoard(registries, ModBlocks.CHERRY_CUTTING_BOARD, Items.WHITE_BANNER, BannerPatterns.FLOWER, DyeColor.PINK).ifPresent(output::accept);
        getDecoratedCuttingBoard(registries, ModBlocks.DARK_OAK_CUTTING_BOARD, Items.GREEN_BANNER, BannerPatterns.CREEPER, DyeColor.BLACK).ifPresent(output::accept);
        getDecoratedCuttingBoard(registries, ModBlocks.CRIMSON_CUTTING_BOARD, Items.BLACK_BANNER, BannerPatterns.SKULL, DyeColor.WHITE).ifPresent(output::accept);
        getDecoratedCuttingBoard(registries, ModBlocks.WARPED_CUTTING_BOARD, Items.BLACK_BANNER, BannerPatterns.FLOW, DyeColor.CYAN).ifPresent(output::accept);
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

    /**
     * Inner class that isolates the Minecraft client reference.
     * This class is only ever loaded when FMLEnvironment.dist.isClient() is true,
     * so it will never trigger RuntimeDistCleaner on a dedicated server.
     */
    private static class ClientHelper {
        static Level getLevel() {
            return net.minecraft.client.Minecraft.getInstance().level;
        }
    }
}
