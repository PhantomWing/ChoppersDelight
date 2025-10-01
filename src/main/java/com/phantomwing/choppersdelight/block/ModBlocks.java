package com.phantomwing.choppersdelight.block;

import com.google.common.collect.Sets;
import com.phantomwing.choppersdelight.block.custom.DecoratedCuttingBoardBlock;
import com.phantomwing.choppersdelight.utils.RegisterUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class ModBlocks {
    public static LinkedHashSet<Supplier<Block>> CUTTING_BOARDS = Sets.newLinkedHashSet();
    public static LinkedHashSet<Supplier<Block>> MINECRAFT_CUTTING_BOARDS = Sets.newLinkedHashSet();
    public static LinkedHashSet<Supplier<Block>> BIOMES_O_PLENTY_CUTTING_BOARDS = Sets.newLinkedHashSet();
    public static LinkedHashSet<Supplier<Block>> BIOMES_WEVE_GONE_CUTTING_BOARDS = Sets.newLinkedHashSet();

    // Special cutting boards
    public static final Supplier<Block> DECORATED_CUTTING_BOARD = registerDecoratedCuttingBoard("decorated_cutting_board");

    // Cutting boards
    public static final Supplier<Block> OAK_CUTTING_BOARD = registerVanillaCuttingBoard("oak_cutting_board");
    public static final Supplier<Block> BIRCH_CUTTING_BOARD = registerVanillaCuttingBoard("birch_cutting_board");
    public static final Supplier<Block> JUNGLE_CUTTING_BOARD = registerVanillaCuttingBoard("jungle_cutting_board");
    public static final Supplier<Block> ACACIA_CUTTING_BOARD = registerVanillaCuttingBoard("acacia_cutting_board");
    public static final Supplier<Block> DARK_OAK_CUTTING_BOARD = registerVanillaCuttingBoard("dark_oak_cutting_board");
    public static final Supplier<Block> MANGROVE_CUTTING_BOARD = registerVanillaCuttingBoard("mangrove_cutting_board");
    public static final Supplier<Block> CHERRY_CUTTING_BOARD = registerVanillaCuttingBoard("cherry_cutting_board");
    public static final Supplier<Block> BAMBOO_CUTTING_BOARD = registerVanillaCuttingBoard("bamboo_cutting_board");
    public static final Supplier<Block> CRIMSON_CUTTING_BOARD = registerVanillaCuttingBoard("crimson_cutting_board");
    public static final Supplier<Block> WARPED_CUTTING_BOARD = registerVanillaCuttingBoard("warped_cutting_board");

    // Not yet in 1.21.1, but made available through Creative Mode.
    public static final Supplier<Block> PALE_OAK_CUTTING_BOARD = registerVanillaCuttingBoard("pale_oak_cutting_board");

    // Biomes O' Plenty
    public static final Supplier<Block> BOP_DEAD_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("dead_cutting_board");
    public static final Supplier<Block> BOP_EMPYREAL_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("empyreal_cutting_board");
    public static final Supplier<Block> BOP_FIR_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("fir_cutting_board");
    public static final Supplier<Block> BOP_HELLBARK_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("hellbark_cutting_board");
    public static final Supplier<Block> BOP_JACARANDA_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("jacaranda_cutting_board");
    public static final Supplier<Block> BOP_MAGIC_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("magic_cutting_board");
    public static final Supplier<Block> BOP_MAHOGANY_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("mahogany_cutting_board");
    public static final Supplier<Block> BOP_MAPLE_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("maple_cutting_board");
    public static final Supplier<Block> BOP_PALM_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("palm_cutting_board");
    public static final Supplier<Block> BOP_PINE_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("pine_cutting_board");
    public static final Supplier<Block> BOP_REDWOOD_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("redwood_cutting_board");
    public static final Supplier<Block> BOP_UMBRAN_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("umbran_cutting_board");
    public static final Supplier<Block> BOP_WILLOW_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("willow_cutting_board");

    // Biomes We've Gone
    public static final Supplier<Block> BWG_ASPEN_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("aspen_cutting_board");
    public static final Supplier<Block> BWG_BAOBAB_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("baobab_cutting_board");
    public static final Supplier<Block> BWG_BLUE_ENCHANTED_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("blue_enchanted_cutting_board");
    public static final Supplier<Block> BWG_CIKA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("cika_cutting_board");
    public static final Supplier<Block> BWG_CYPRESS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("cypress_cutting_board");
    public static final Supplier<Block> BWG_EBONY_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("ebony_cutting_board");
    public static final Supplier<Block> BWG_FIR_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("fir_cutting_board");
    public static final Supplier<Block> BWG_FLORUS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("florus_cutting_board");
    public static final Supplier<Block> BWG_GREEN_ENCHANTED_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("green_enchanted_cutting_board");
    public static final Supplier<Block> BWG_HOLLY_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("holly_cutting_board");
    public static final Supplier<Block> BWG_IRONWOOD_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("ironwood_cutting_board");
    public static final Supplier<Block> BWG_JACARANDA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("jacaranda_cutting_board");
    public static final Supplier<Block> BWG_MAHOGANY_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("mahogany_cutting_board");
    public static final Supplier<Block> BWG_MAPLE_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("maple_cutting_board");
    public static final Supplier<Block> BWG_PALM_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("palm_cutting_board");
    public static final Supplier<Block> BWG_PINE_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("pine_cutting_board");
    public static final Supplier<Block> BWG_RAINBOW_EUCALYPTUS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("rainbow_eucalyptus_cutting_board");
    public static final Supplier<Block> BWG_REDWOOD_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("redwood_cutting_board");
    public static final Supplier<Block> BWG_SAKURA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("sakura_cutting_board");
    public static final Supplier<Block> BWG_SKYRIS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("skyris_cutting_board");
    public static final Supplier<Block> BWG_SPIRIT_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("spirit_cutting_board");
    public static final Supplier<Block> BWG_WHITE_MANGROVE_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("white_mangrove_cutting_board");
    public static final Supplier<Block> BWG_WILLOW_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("willow_cutting_board");
    public static final Supplier<Block> BWG_WITCH_HAZEL_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("witch_hazel_cutting_board");
    public static final Supplier<Block> BWG_ZELKOVA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("zelkova_cutting_board");

    private static Supplier<Block> registerVanillaCuttingBoard(String name) {
        Supplier<Block> block = registerCuttingBoard(name);
        MINECRAFT_CUTTING_BOARDS.add(block);

        return block;
    }

    private static Supplier<Block> registerBiomesOPlentyCuttingBoard(String name) {
        Supplier<Block> block = registerCuttingBoard(name);
        BIOMES_O_PLENTY_CUTTING_BOARDS.add(block);

        return block;
    }

    private static Supplier<Block> registerBiomesWeveGoneCuttingBoard(String name) {
        Supplier<Block> block = registerCuttingBoard(name);
        BIOMES_WEVE_GONE_CUTTING_BOARDS.add(block);

        return block;
    }

    private static Supplier<Block> registerCuttingBoard(String name) {
        Supplier<Block> block = registerBlock(name, ModBlocks::createCuttingBoard);
        CUTTING_BOARDS.add(block);

        return block;
    }

    private static CuttingBoardBlock createCuttingBoard() {
        return new CuttingBoardBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0f).sound(SoundType.WOOD));
    }

    private static Supplier<Block> registerDecoratedCuttingBoard(String name) {
        return registerBlock(name, () -> new DecoratedCuttingBoardBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
    }

    private static Supplier<Block> registerBlock(String name, Supplier<Block> supplier) {
        return RegisterUtils.register(name, supplier, BuiltInRegistries.BLOCK);
    }
}
