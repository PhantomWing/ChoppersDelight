package com.phantomwing.choppersdelight.block;

import com.google.common.collect.Sets;
import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.block.custom.DecoratedCuttingBoardBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ChoppersDelight.MOD_ID);
    public static LinkedHashSet<Supplier<Block>> CUTTING_BOARDS = Sets.newLinkedHashSet();

    // Blocks
    public static final DeferredBlock<Block> OAK_CUTTING_BOARD = registerCuttingBoard("oak_cutting_board");
    public static final DeferredBlock<Block> BIRCH_CUTTING_BOARD = registerCuttingBoard("birch_cutting_board");
    public static final DeferredBlock<Block> JUNGLE_CUTTING_BOARD = registerCuttingBoard("jungle_cutting_board");
    public static final DeferredBlock<Block> ACACIA_CUTTING_BOARD = registerCuttingBoard("acacia_cutting_board");
    public static final DeferredBlock<Block> DARK_OAK_CUTTING_BOARD = registerCuttingBoard("dark_oak_cutting_board");
    public static final DeferredBlock<Block> MANGROVE_CUTTING_BOARD = registerCuttingBoard("mangrove_cutting_board");
    public static final DeferredBlock<Block> CHERRY_CUTTING_BOARD = registerCuttingBoard("cherry_cutting_board");
    public static final DeferredBlock<Block> BAMBOO_CUTTING_BOARD = registerCuttingBoard("bamboo_cutting_board");
    public static final DeferredBlock<Block> CRIMSON_CUTTING_BOARD = registerCuttingBoard("crimson_cutting_board");
    public static final DeferredBlock<Block> WARPED_CUTTING_BOARD = registerCuttingBoard("warped_cutting_board");

    // Not yet in 1.21.1, but made available through Creative Mode.
    public static final DeferredBlock<Block> PALE_OAK_CUTTING_BOARD = registerCuttingBoard("pale_oak_cutting_board");

    public static final DeferredBlock<Block> DECORATED_CUTTING_BOARD = registerDecoratedCuttingBoard("decorated_cutting_board");

    private static DeferredBlock<Block> registerCuttingBoard(String name) {
        DeferredBlock<Block> block = BLOCKS.register(name, ModBlocks::createCuttingBoard);
        CUTTING_BOARDS.add(block);

        return block;
    }

    private static CuttingBoardBlock createCuttingBoard() {
        return new CuttingBoardBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0f).sound(SoundType.WOOD));
    }

    private static DeferredBlock<Block> registerDecoratedCuttingBoard(String name) {
        return BLOCKS.register(name, () -> new DecoratedCuttingBoardBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
