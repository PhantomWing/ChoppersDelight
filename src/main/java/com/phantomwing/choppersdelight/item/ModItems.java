package com.phantomwing.choppersdelight.item;

import com.google.common.collect.Sets;
import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.item.custom.DecoratedCuttingBoardItem;
import com.phantomwing.choppersdelight.utils.RegisterUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import vectorwing.farmersdelight.common.item.FuelBlockItem;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class ModItems {
    public static LinkedHashSet<Supplier<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();
    public static LinkedHashSet<Supplier<Item>> BIOMES_O_PLENTY_CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();
    public static LinkedHashSet<Supplier<Item>> BIOMES_WEVE_GONE_CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

    // Cutting boards
    public static final Supplier<Item> OAK_CUTTING_BOARD = registerVanillaCuttingBoardItem(ModBlocks.OAK_CUTTING_BOARD);
    public static final Supplier<Item> BIRCH_CUTTING_BOARD = registerVanillaCuttingBoardItem(ModBlocks.BIRCH_CUTTING_BOARD);
    public static final Supplier<Item> JUNGLE_CUTTING_BOARD = registerVanillaCuttingBoardItem(ModBlocks.JUNGLE_CUTTING_BOARD);
    public static final Supplier<Item> ACACIA_CUTTING_BOARD = registerVanillaCuttingBoardItem(ModBlocks.ACACIA_CUTTING_BOARD);
    public static final Supplier<Item> DARK_OAK_CUTTING_BOARD = registerVanillaCuttingBoardItem(ModBlocks.DARK_OAK_CUTTING_BOARD);
    public static final Supplier<Item> MANGROVE_CUTTING_BOARD = registerVanillaCuttingBoardItem(ModBlocks.MANGROVE_CUTTING_BOARD);
    public static final Supplier<Item> CHERRY_CUTTING_BOARD = registerVanillaCuttingBoardItem(ModBlocks.CHERRY_CUTTING_BOARD);
    public static final Supplier<Item> BAMBOO_CUTTING_BOARD = registerVanillaCuttingBoardItem(ModBlocks.BAMBOO_CUTTING_BOARD);
    public static final Supplier<Item> CRIMSON_CUTTING_BOARD = registerVanillaCuttingBoardItem(ModBlocks.CRIMSON_CUTTING_BOARD);
    public static final Supplier<Item> WARPED_CUTTING_BOARD = registerVanillaCuttingBoardItem(ModBlocks.WARPED_CUTTING_BOARD);

    // Not yet in 1.21.1, but made available through Creative Mode.
    public static final Supplier<Item> PALE_OAK_CUTTING_BOARD = registerVanillaCuttingBoardItem(ModBlocks.PALE_OAK_CUTTING_BOARD);

    // Biomes o Plenty cutting boards
    public static final Supplier<Item> BOP_DEAD_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_DEAD_CUTTING_BOARD);
    public static final Supplier<Item> BOP_EMPYREAL_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_EMPYREAL_CUTTING_BOARD);
    public static final Supplier<Item> BOP_FIR_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_FIR_CUTTING_BOARD);
    public static final Supplier<Item> BOP_HELLBARK_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_HELLBARK_CUTTING_BOARD);
    public static final Supplier<Item> BOP_JACARANDA_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_JACARANDA_CUTTING_BOARD);
    public static final Supplier<Item> BOP_MAGIC_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_MAGIC_CUTTING_BOARD);
    public static final Supplier<Item> BOP_MAHOGANY_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_MAHOGANY_CUTTING_BOARD);
    public static final Supplier<Item> BOP_MAPLE_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_MAPLE_CUTTING_BOARD);
    public static final Supplier<Item> BOP_PALM_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_PALM_CUTTING_BOARD);
    public static final Supplier<Item> BOP_PINE_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_PINE_CUTTING_BOARD);
    public static final Supplier<Item> BOP_REDWOOD_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_REDWOOD_CUTTING_BOARD);
    public static final Supplier<Item> BOP_UMBRAN_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_UMBRAN_CUTTING_BOARD);
    public static final Supplier<Item> BOP_WILLOW_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_WILLOW_CUTTING_BOARD);

    // Biomes We've Gone cutting boards
    public static final Supplier<Item> BWG_ASPEN_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_ASPEN_CUTTING_BOARD);
    public static final Supplier<Item> BWG_BAOBAB_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_BAOBAB_CUTTING_BOARD);
    public static final Supplier<Item> BWG_BLUE_ENCHANTED_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_BLUE_ENCHANTED_CUTTING_BOARD);
    public static final Supplier<Item> BWG_CIKA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_CIKA_CUTTING_BOARD);
    public static final Supplier<Item> BWG_CYPRESS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_CYPRESS_CUTTING_BOARD);
    public static final Supplier<Item> BWG_EBONY_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_EBONY_CUTTING_BOARD);
    public static final Supplier<Item> BWG_FIR_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_FIR_CUTTING_BOARD);
    public static final Supplier<Item> BWG_FLORUS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_FLORUS_CUTTING_BOARD);
    public static final Supplier<Item> BWG_GREEN_ENCHANTED_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_GREEN_ENCHANTED_CUTTING_BOARD);
    public static final Supplier<Item> BWG_HOLLY_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_HOLLY_CUTTING_BOARD);
    public static final Supplier<Item> BWG_IRONWOOD_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_IRONWOOD_CUTTING_BOARD);
    public static final Supplier<Item> BWG_JACARANDA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_JACARANDA_CUTTING_BOARD);
    public static final Supplier<Item> BWG_MAHOGANY_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_MAHOGANY_CUTTING_BOARD);
    public static final Supplier<Item> BWG_MAPLE_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_MAPLE_CUTTING_BOARD);
    public static final Supplier<Item> BWG_PALM_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_PALM_CUTTING_BOARD);
    public static final Supplier<Item> BWG_PINE_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_PINE_CUTTING_BOARD);
    public static final Supplier<Item> BWG_RAINBOW_EUCALYPTUS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_RAINBOW_EUCALYPTUS_CUTTING_BOARD);
    public static final Supplier<Item> BWG_REDWOOD_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_REDWOOD_CUTTING_BOARD);
    public static final Supplier<Item> BWG_SAKURA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_SAKURA_CUTTING_BOARD);
    public static final Supplier<Item> BWG_SKYRIS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_SKYRIS_CUTTING_BOARD);
    public static final Supplier<Item> BWG_SPIRIT_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_SPIRIT_CUTTING_BOARD);
    public static final Supplier<Item> BWG_WHITE_MANGROVE_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_WHITE_MANGROVE_CUTTING_BOARD);
    public static final Supplier<Item> BWG_WILLOW_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_WILLOW_CUTTING_BOARD);
    public static final Supplier<Item> BWG_WITCH_HAZEL_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_WITCH_HAZEL_CUTTING_BOARD);
    public static final Supplier<Item> BWG_ZELKOVA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_ZELKOVA_CUTTING_BOARD);

    // Special cutting boards
    public static final Supplier<Item> DECORATED_CUTTING_BOARD = registerDecoratedCuttingBoardItem(ModBlocks.DECORATED_CUTTING_BOARD);

    // Helper functions
    private static Item.Properties baseItem() {
        return new Item.Properties();
    }

    // Registry functions
    private static Supplier<Item> registerDecoratedCuttingBoardItem(Supplier<Block> block) {
        String name = BuiltInRegistries.BLOCK.getKey(block.get()).getPath();

        return registerItem(name, () -> new DecoratedCuttingBoardItem(block.get(),baseItem(), 200), ChoppersDelight.MOD_ID);
    }

    private static Supplier<Item> registerVanillaCuttingBoardItem(Supplier<Block> block) {
        return registerBlockItem(block, () -> createCuttingBoard(block.get()), ChoppersDelight.MOD_ID);
    }

    private static Supplier<Item> registerBiomesOPlentyCuttingBoardItem(Supplier<Block> block) {
        String name = BuiltInRegistries.BLOCK.getKey(block.get()).getPath();

        Supplier<Item> item = registerItem(name, () -> createCuttingBoard(block.get()), Compatibility.BIOMES_O_PLENTY_MOD_ID);
        BIOMES_O_PLENTY_CREATIVE_TAB_ITEMS.add(item);

        return item;
    }

    private static Supplier<Item> registerBiomesWeveGoneCuttingBoardItem(Supplier<Block> block) {
        String name = BuiltInRegistries.BLOCK.getKey(block.get()).getPath();

        Supplier<Item> item = registerItem(name, () -> createCuttingBoard(block.get()), Compatibility.BIOMES_WEVE_GONE_MOD_ID);
        BIOMES_WEVE_GONE_CREATIVE_TAB_ITEMS.add(item);

        return item;
    }

    private static FuelBlockItem createCuttingBoard(Block block) {
        return new FuelBlockItem(block, baseItem(), 200);
    }

    private static Supplier<Item> registerBlockItem(Supplier<Block> block, Supplier<Item> supplier, String namespace) {
        String name = BuiltInRegistries.BLOCK.getKey(block.get()).getPath();
        return registerWithTab(name, supplier, namespace);
    }

    private static Supplier<Item> registerWithTab(final String name, final Supplier<Item> supplier, String namespace) {
        Supplier<Item> item = registerItem(name, supplier, namespace);
        CREATIVE_TAB_ITEMS.add(item);
        return item;
    }

    private static Supplier<Item> registerItem(String name, Supplier<Item> supplier, String namespace) {
        return RegisterUtils.register(name, supplier, BuiltInRegistries.ITEM, namespace);
    }

    public static void registerModItems() {
        ChoppersDelight.LOGGER.info("Registering items for " + ChoppersDelight.MOD_ID);
    }
}
