package com.phantomwing.choppersdelight.item;

import com.google.common.collect.Sets;
import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.item.custom.DecoratedCuttingBoardItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.item.FuelBlockItem;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ChoppersDelight.MOD_ID);
    public static final DeferredRegister.Items BIOMES_O_PLENTY_ITEMS = DeferredRegister.createItems(Compatibility.BIOMES_O_PLENTY_MOD_ID);
    public static final DeferredRegister.Items BIOMES_WEVE_GONE_ITEMS = DeferredRegister.createItems(Compatibility.BIOMES_WEVE_GONE_MOD_ID);

    public static LinkedHashSet<Supplier<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();
    public static LinkedHashSet<Supplier<Item>> BIOMES_O_PLENTY_CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();
    public static LinkedHashSet<Supplier<Item>> BIOMES_WEVE_GONE_CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

    // Cutting boards
    public static final DeferredItem<Item> OAK_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.OAK_CUTTING_BOARD);
    public static final DeferredItem<Item> BIRCH_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.BIRCH_CUTTING_BOARD);
    public static final DeferredItem<Item> JUNGLE_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.JUNGLE_CUTTING_BOARD);
    public static final DeferredItem<Item> ACACIA_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.ACACIA_CUTTING_BOARD);
    public static final DeferredItem<Item> DARK_OAK_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.DARK_OAK_CUTTING_BOARD);
    public static final DeferredItem<Item> MANGROVE_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.MANGROVE_CUTTING_BOARD);
    public static final DeferredItem<Item> CHERRY_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.CHERRY_CUTTING_BOARD);
    public static final DeferredItem<Item> BAMBOO_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.BAMBOO_CUTTING_BOARD);
    public static final DeferredItem<Item> CRIMSON_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.CRIMSON_CUTTING_BOARD);
    public static final DeferredItem<Item> WARPED_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.WARPED_CUTTING_BOARD);

    // Not yet in 1.21.1, but made available through Creative Mode.
    public static final DeferredItem<Item> PALE_OAK_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.PALE_OAK_CUTTING_BOARD);

    // Biomes o Plenty cutting boards
    public static final DeferredItem<Item> BOP_DEAD_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_DEAD_CUTTING_BOARD);
    public static final DeferredItem<Item> BOP_EMPYREAL_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_EMPYREAL_CUTTING_BOARD);
    public static final DeferredItem<Item> BOP_FIR_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_FIR_CUTTING_BOARD);
    public static final DeferredItem<Item> BOP_HELLBARK_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_HELLBARK_CUTTING_BOARD);
    public static final DeferredItem<Item> BOP_JACARANDA_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_JACARANDA_CUTTING_BOARD);
    public static final DeferredItem<Item> BOP_MAGIC_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_MAGIC_CUTTING_BOARD);
    public static final DeferredItem<Item> BOP_MAHOGANY_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_MAHOGANY_CUTTING_BOARD);
    public static final DeferredItem<Item> BOP_MAPLE_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_MAPLE_CUTTING_BOARD);
    public static final DeferredItem<Item> BOP_PALM_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_PALM_CUTTING_BOARD);
    public static final DeferredItem<Item> BOP_PINE_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_PINE_CUTTING_BOARD);
    public static final DeferredItem<Item> BOP_REDWOOD_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_REDWOOD_CUTTING_BOARD);
    public static final DeferredItem<Item> BOP_UMBRAN_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_UMBRAN_CUTTING_BOARD);
    public static final DeferredItem<Item> BOP_WILLOW_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_WILLOW_CUTTING_BOARD);

    // Biomes We've Gone cutting boards
    public static final DeferredItem<Item> BWG_ASPEN_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_ASPEN_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_BAOBAB_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_BAOBAB_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_BLUE_ENCHANTED_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_BLUE_ENCHANTED_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_CIKA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_CIKA_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_CYPRESS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_CYPRESS_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_EBONY_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_EBONY_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_FIR_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_FIR_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_FLORUS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_FLORUS_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_GREEN_ENCHANTED_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_GREEN_ENCHANTED_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_HOLLY_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_HOLLY_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_IRONWOOD_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_IRONWOOD_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_JACARANDA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_JACARANDA_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_MAHOGANY_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_MAHOGANY_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_MAPLE_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_MAPLE_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_PALM_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_PALM_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_PINE_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_PINE_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_RAINBOW_EUCALYPTUS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_RAINBOW_EUCALYPTUS_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_REDWOOD_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_REDWOOD_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_SAKURA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_SAKURA_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_SKYRIS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_SKYRIS_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_SPIRIT_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_SPIRIT_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_WHITE_MANGROVE_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_WHITE_MANGROVE_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_WILLOW_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_WILLOW_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_WITCH_HAZEL_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_WITCH_HAZEL_CUTTING_BOARD);
    public static final DeferredItem<Item> BWG_ZELKOVA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_ZELKOVA_CUTTING_BOARD);

    // Special cutting boards
    public static final DeferredItem<Item> DECORATED_CUTTING_BOARD = registerDecoratedCuttingBoardItem(ModBlocks.DECORATED_CUTTING_BOARD);

    // Helper functions
    private static Item.Properties baseItem() {
        return new Item.Properties();
    }

    // Registry functions
    private static DeferredItem<Item> registerDecoratedCuttingBoardItem(DeferredBlock<Block> block) {
        return registerBlockItem(block, () -> new DecoratedCuttingBoardItem(block.get(),baseItem(), 200));
    }

    private static DeferredItem<Item> registerCuttingBoardItem(DeferredBlock<Block> block) {
        return registerBlockItem(block, () -> createCuttingBoard(block.get()));
    }

    private static DeferredItem<Item> registerBiomesOPlentyCuttingBoardItem(DeferredBlock<Block> block) {
        String name = block.getRegisteredName().replaceFirst(Compatibility.BIOMES_O_PLENTY_MOD_ID + ":", "");

        DeferredItem<Item> item = BIOMES_O_PLENTY_ITEMS.register(name, () -> createCuttingBoard(block.get()));
        BIOMES_O_PLENTY_CREATIVE_TAB_ITEMS.add(item);

        return item;
    }

    private static DeferredItem<Item> registerBiomesWeveGoneCuttingBoardItem(DeferredBlock<Block> block) {
        String name = block.getRegisteredName().replaceFirst(Compatibility.BIOMES_WEVE_GONE_MOD_ID + ":", "");

        DeferredItem<Item> item = BIOMES_WEVE_GONE_ITEMS.register(name, () -> createCuttingBoard(block.get()));
        BIOMES_WEVE_GONE_CREATIVE_TAB_ITEMS.add(item);

        return item;
    }

    private static FuelBlockItem createCuttingBoard(Block block) {
        return new FuelBlockItem(block, baseItem(), 200);
    }

    private static DeferredItem<Item> registerBlockItem(DeferredBlock<Block> block, Supplier<Item> supplier) {
        String name = block.getRegisteredName().replaceFirst(ChoppersDelight.MOD_ID + ":", "");
        return registerWithTab(name, supplier);
    }

    private static DeferredItem<Item> registerWithTab(final String name, final Supplier<Item> supplier) {
        DeferredItem<Item> item = ITEMS.register(name, supplier);
        CREATIVE_TAB_ITEMS.add(item);
        return item;
    }


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);

        if (Compatibility.IsBiomesOPlentyLoaded()) {
            BIOMES_O_PLENTY_ITEMS.register(eventBus);
        }

        if (Compatibility.IsBiomesWeveGoneLoaded()) {
            BIOMES_WEVE_GONE_ITEMS.register(eventBus);
        }
    }
}
