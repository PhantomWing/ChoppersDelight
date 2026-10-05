package com.phantomwing.choppersdelight.itemGroup;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.component.DecoratedCuttingBoardData;
import com.phantomwing.choppersdelight.component.ModDataComponents;
import com.phantomwing.choppersdelight.item.ModItems;
import com.phantomwing.choppersdelight.utils.RegisterUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.BannerPatterns;

import java.util.Optional;
import java.util.function.Supplier;

public class ModItemGroups {
    public static final Supplier<CreativeModeTab> MOD_TAB =
       RegisterUtils.register(ChoppersDelight.MOD_ID + "_tab", () -> FabricItemGroup.builder()
            .icon(ModItemGroups::getTabIcon)
            .title(Component.translatable(("itemGroup." + ChoppersDelight.MOD_ID)))
            .displayItems(ModItemGroups::displayItems)
            .build(), BuiltInRegistries.CREATIVE_MODE_TAB);

    /**
     * Returns the client-side Level, or null if not on the client or not in a world.
     * The actual {@code Minecraft.getInstance().level} call is isolated in {@link ClientHelper}
     * so that loading this class on a dedicated server (e.g. when EveryCompat's WoodGood
     * references {@link #MOD_TAB}) doesn't pull in the client-only {@code ClientLevel} class.
     */
    private static Level getClientLevel() {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
            return null;
        }
        return ClientHelper.getLevel();
    }

    private static ItemStack getTabIcon() {
        Level level = getClientLevel();

        return Optional.ofNullable(level)
                .flatMap(l -> getDecoratedCuttingBoard(l.registryAccess(), ModBlocks.DARK_OAK_CUTTING_BOARD, Items.GREEN_BANNER, BannerPatterns.CREEPER, DyeColor.BLACK))
                .orElseGet(() -> new ItemStack(ModBlocks.DECORATED_CUTTING_BOARD.get()));
    }

    /**
     * Empty when the registries have no such banner pattern. Banner patterns are a datapack registry, so they
     * come from whoever builds the tab - on a server there is no client level to read them from.
     */
    private static Optional<ItemStack> getDecoratedCuttingBoard(HolderLookup.Provider registries, Supplier<Block> board, Item banner, ResourceKey<BannerPattern> pattern, DyeColor patternColor) {
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

    private static void displayItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
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

    private static void displayModItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
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

    private static void displayBiomesOPlentyItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        if (!Compatibility.IsBiomesOPlentyLoaded()) {
            return;
        }

        // Add items to this tab.
        ModItems.BIOMES_O_PLENTY_CREATIVE_TAB_ITEMS.forEach((item) -> {
            output.accept(item.get());
        });
    }

    private static void displayBiomesWeveGoneItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        if (!Compatibility.IsBiomesWeveGoneLoaded()) {
            return;
        }

        // Add items to this tab.
        ModItems.BIOMES_WEVE_GONE_CREATIVE_TAB_ITEMS.forEach((item) -> {
            output.accept(item.get());
        });
    }

    public static void registerModItemGroups() {
        ChoppersDelight.LOGGER.info("Registering item groups for " + ChoppersDelight.MOD_ID);
    }

    /**
     * Inner class that isolates the {@code Minecraft.getInstance().level} reference.
     * Only loaded when {@link #getLevel()} is invoked, which only happens on the client,
     * so the client-only {@code ClientLevel} type is never resolved on a dedicated server.
     */
    private static class ClientHelper {
        static Level getLevel() {
            return net.minecraft.client.Minecraft.getInstance().level;
        }
    }
}
