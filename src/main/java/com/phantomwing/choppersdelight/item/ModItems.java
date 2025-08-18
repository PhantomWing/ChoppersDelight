package com.phantomwing.choppersdelight.item;

import com.google.common.collect.Sets;
import com.phantomwing.choppersdelight.ChoppersDelight;
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
    public static LinkedHashSet<Supplier<Item>> CREATIVE_TAB_ITEMS = Sets.newLinkedHashSet();

    // Block items
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

    public static final DeferredItem<Item> DECORATED_CUTTING_BOARD = registerDecoratedCuttingBoardItem(ModBlocks.DECORATED_CUTTING_BOARD);

    // Helper functions
    public static Item.Properties baseItem() {
        return new Item.Properties();
    }

    // Registry functions
    public static DeferredItem<Item> registerWithTab(final String name, final Supplier<Item> supplier) {
        DeferredItem<Item> item = ITEMS.register(name, supplier);
        CREATIVE_TAB_ITEMS.add(item);
        return item;
    }

    public static DeferredItem<Item> registerDecoratedCuttingBoardItem(DeferredBlock<Block> block) {
        return registerBlockItem(block, () -> new DecoratedCuttingBoardItem(block.get(),baseItem(), 200));
    }

    public static DeferredItem<Item> registerCuttingBoardItem(DeferredBlock<Block> block) {
        return registerBlockItem(block, () -> createCuttingBoard(block.get()));
    }

    private static FuelBlockItem createCuttingBoard(Block block) {
        return new FuelBlockItem(block, baseItem(), 200);
    }


    public static DeferredItem<Item> registerBlockItem(DeferredBlock<Block> block, Supplier<Item> supplier) {
        return registerWithTab(block.getRegisteredName().replaceFirst(ChoppersDelight.MOD_ID + ":", ""), supplier);
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
