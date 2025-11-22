package com.phantomwing.choppersdelight.item;

import com.google.common.collect.Sets;
import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.item.custom.DecoratedCuttingBoardItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.item.FuelBlockItem;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ChoppersDelight.MOD_ID);
    public static final DeferredRegister<Item> BIOMES_O_PLENTY_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Compatibility.BIOMES_O_PLENTY_MOD_ID);
    public static final DeferredRegister<Item> BIOMES_WEVE_GONE_ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Compatibility.BIOMES_WEVE_GONE_MOD_ID);

    public static LinkedHashSet<Supplier<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();
    public static LinkedHashSet<Supplier<Item>> BIOMES_O_PLENTY_CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();
    public static LinkedHashSet<Supplier<Item>> BIOMES_WEVE_GONE_CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

    // Cutting boards
    public static final RegistryObject<Item> OAK_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.OAK_CUTTING_BOARD);
    public static final RegistryObject<Item> BIRCH_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.BIRCH_CUTTING_BOARD);
    public static final RegistryObject<Item> JUNGLE_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.JUNGLE_CUTTING_BOARD);
    public static final RegistryObject<Item> ACACIA_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.ACACIA_CUTTING_BOARD);
    public static final RegistryObject<Item> DARK_OAK_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.DARK_OAK_CUTTING_BOARD);
    public static final RegistryObject<Item> MANGROVE_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.MANGROVE_CUTTING_BOARD);
    public static final RegistryObject<Item> CHERRY_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.CHERRY_CUTTING_BOARD);
    public static final RegistryObject<Item> BAMBOO_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.BAMBOO_CUTTING_BOARD);
    public static final RegistryObject<Item> CRIMSON_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.CRIMSON_CUTTING_BOARD);
    public static final RegistryObject<Item> WARPED_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.WARPED_CUTTING_BOARD);

    // Not yet in 1.21.1, but made available through Creative Mode.
    public static final RegistryObject<Item> PALE_OAK_CUTTING_BOARD = registerCuttingBoardItem(ModBlocks.PALE_OAK_CUTTING_BOARD);

    // Biomes o Plenty cutting boards
    public static final RegistryObject<Item> BOP_DEAD_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_DEAD_CUTTING_BOARD);
    public static final RegistryObject<Item> BOP_EMPYREAL_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_EMPYREAL_CUTTING_BOARD);
    public static final RegistryObject<Item> BOP_FIR_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_FIR_CUTTING_BOARD);
    public static final RegistryObject<Item> BOP_HELLBARK_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_HELLBARK_CUTTING_BOARD);
    public static final RegistryObject<Item> BOP_JACARANDA_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_JACARANDA_CUTTING_BOARD);
    public static final RegistryObject<Item> BOP_MAGIC_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_MAGIC_CUTTING_BOARD);
    public static final RegistryObject<Item> BOP_MAHOGANY_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_MAHOGANY_CUTTING_BOARD);
    public static final RegistryObject<Item> BOP_MAPLE_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_MAPLE_CUTTING_BOARD);
    public static final RegistryObject<Item> BOP_PALM_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_PALM_CUTTING_BOARD);
    public static final RegistryObject<Item> BOP_PINE_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_PINE_CUTTING_BOARD);
    public static final RegistryObject<Item> BOP_REDWOOD_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_REDWOOD_CUTTING_BOARD);
    public static final RegistryObject<Item> BOP_UMBRAN_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_UMBRAN_CUTTING_BOARD);
    public static final RegistryObject<Item> BOP_WILLOW_CUTTING_BOARD = registerBiomesOPlentyCuttingBoardItem(ModBlocks.BOP_WILLOW_CUTTING_BOARD);

    // Biomes We've Gone cutting boards
    public static final RegistryObject<Item> BWG_ASPEN_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_ASPEN_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_BAOBAB_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_BAOBAB_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_BLUE_ENCHANTED_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_BLUE_ENCHANTED_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_CIKA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_CIKA_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_CYPRESS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_CYPRESS_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_EBONY_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_EBONY_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_FIR_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_FIR_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_FLORUS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_FLORUS_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_GREEN_ENCHANTED_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_GREEN_ENCHANTED_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_HOLLY_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_HOLLY_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_IRONWOOD_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_IRONWOOD_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_JACARANDA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_JACARANDA_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_MAHOGANY_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_MAHOGANY_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_MAPLE_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_MAPLE_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_PALM_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_PALM_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_PINE_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_PINE_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_RAINBOW_EUCALYPTUS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_RAINBOW_EUCALYPTUS_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_REDWOOD_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_REDWOOD_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_SAKURA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_SAKURA_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_SKYRIS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_SKYRIS_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_WHITE_MANGROVE_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_WHITE_MANGROVE_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_WILLOW_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_WILLOW_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_WITCH_HAZEL_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_WITCH_HAZEL_CUTTING_BOARD);
    public static final RegistryObject<Item> BWG_ZELKOVA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoardItem(ModBlocks.BWG_ZELKOVA_CUTTING_BOARD);

    // Special cutting boards
    public static final RegistryObject<Item> DECORATED_CUTTING_BOARD = registerDecoratedCuttingBoardItem(ModBlocks.DECORATED_CUTTING_BOARD);

    // Helper functions
    public static Item.Properties baseItem() {
        return new Item.Properties();
    }

    // Registry functions
    private static RegistryObject<Item> registerDecoratedCuttingBoardItem(RegistryObject<Block> block) {
        String name = block.getId().getPath().replaceFirst(ChoppersDelight.MOD_ID + ":", "");

        return ITEMS.register(name, () -> new DecoratedCuttingBoardItem(block.get(),baseItem(), 200));
    }

    private static RegistryObject<Item> registerCuttingBoardItem(RegistryObject<Block> block) {
        return registerBlockItem(block, () -> createCuttingBoard(block.get()));
    }

    private static RegistryObject<Item> registerBiomesOPlentyCuttingBoardItem(RegistryObject<Block> block) {
        String name = block.getId().getPath().replaceFirst(Compatibility.BIOMES_O_PLENTY_MOD_ID + ":", "");

        RegistryObject<Item> item = BIOMES_O_PLENTY_ITEMS.register(name, () -> createCuttingBoard(block.get()));
        BIOMES_O_PLENTY_CREATIVE_TAB_ITEMS.add(item);

        return item;
    }

    private static RegistryObject<Item> registerBiomesWeveGoneCuttingBoardItem(RegistryObject<Block> block) {
        String name = block.getId().getPath().replaceFirst(Compatibility.BIOMES_WEVE_GONE_MOD_ID + ":", "");

        RegistryObject<Item> item = BIOMES_WEVE_GONE_ITEMS.register(name, () -> createCuttingBoard(block.get()));
        BIOMES_WEVE_GONE_CREATIVE_TAB_ITEMS.add(item);

        return item;
    }

    private static FuelBlockItem createCuttingBoard(Block block) {
        return new FuelBlockItem(block, baseItem(), 200);
    }

    private static RegistryObject<Item> registerBlockItem(RegistryObject<Block> block, Supplier<Item> supplier) {
        String name = block.getId().getPath().replaceFirst(ChoppersDelight.MOD_ID + ":", "");
        return registerWithTab(name, supplier);
    }

    private static RegistryObject<Item> registerWithTab(final String name, final Supplier<Item> supplier) {
        RegistryObject<Item> item = ITEMS.register(name, supplier);
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