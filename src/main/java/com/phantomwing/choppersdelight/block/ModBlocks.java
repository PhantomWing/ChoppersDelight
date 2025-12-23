package com.phantomwing.choppersdelight.block;

import com.google.common.collect.Sets;
import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.custom.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashSet;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ChoppersDelight.MOD_ID);
    public static final DeferredRegister<Block> BIOMES_O_PLENTY_BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Compatibility.BIOMES_O_PLENTY_MOD_ID);
    public static final DeferredRegister<Block> BIOMES_WEVE_GONE_BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Compatibility.BIOMES_WEVE_GONE_MOD_ID);

    public static LinkedHashSet<Supplier<Block>> CUTTING_BOARDS = Sets.newLinkedHashSet();
    public static LinkedHashSet<Supplier<Block>> MINECRAFT_CUTTING_BOARDS = Sets.newLinkedHashSet();
    public static LinkedHashSet<Supplier<Block>> BIOMES_O_PLENTY_CUTTING_BOARDS = Sets.newLinkedHashSet();
    public static LinkedHashSet<Supplier<Block>> BIOMES_WEVE_GONE_CUTTING_BOARDS = Sets.newLinkedHashSet();

    // Special cutting boards
    public static final RegistryObject<Block> DECORATED_CUTTING_BOARD = registerDecoratedCuttingBoard("decorated_cutting_board");

    // Cutting boards
    public static final RegistryObject<Block> OAK_CUTTING_BOARD = registerCuttingBoard("oak_cutting_board");
    public static final RegistryObject<Block> BIRCH_CUTTING_BOARD = registerCuttingBoard("birch_cutting_board");
    public static final RegistryObject<Block> JUNGLE_CUTTING_BOARD = registerCuttingBoard("jungle_cutting_board");
    public static final RegistryObject<Block> ACACIA_CUTTING_BOARD = registerCuttingBoard("acacia_cutting_board");
    public static final RegistryObject<Block> DARK_OAK_CUTTING_BOARD = registerCuttingBoard("dark_oak_cutting_board");
    public static final RegistryObject<Block> MANGROVE_CUTTING_BOARD = registerCuttingBoard("mangrove_cutting_board");
    public static final RegistryObject<Block> CHERRY_CUTTING_BOARD = registerCuttingBoard("cherry_cutting_board");
    public static final RegistryObject<Block> BAMBOO_CUTTING_BOARD = registerCuttingBoard("bamboo_cutting_board");
    public static final RegistryObject<Block> CRIMSON_CUTTING_BOARD = registerCuttingBoard("crimson_cutting_board");
    public static final RegistryObject<Block> WARPED_CUTTING_BOARD = registerCuttingBoard("warped_cutting_board");

    // Not yet in 1.21.1, but made available through Creative Mode.
    public static final RegistryObject<Block> PALE_OAK_CUTTING_BOARD = registerCuttingBoard("pale_oak_cutting_board");

    // Biomes O' Plenty
    public static final RegistryObject<Block> BOP_DEAD_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("dead_cutting_board");
    public static final RegistryObject<Block> BOP_EMPYREAL_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("empyreal_cutting_board");
    public static final RegistryObject<Block> BOP_FIR_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("fir_cutting_board");
    public static final RegistryObject<Block> BOP_HELLBARK_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("hellbark_cutting_board");
    public static final RegistryObject<Block> BOP_JACARANDA_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("jacaranda_cutting_board");
    public static final RegistryObject<Block> BOP_MAGIC_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("magic_cutting_board");
    public static final RegistryObject<Block> BOP_MAHOGANY_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("mahogany_cutting_board");
    public static final RegistryObject<Block> BOP_MAPLE_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("maple_cutting_board");
    public static final RegistryObject<Block> BOP_PALM_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("palm_cutting_board");
    public static final RegistryObject<Block> BOP_PINE_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("pine_cutting_board");
    public static final RegistryObject<Block> BOP_REDWOOD_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("redwood_cutting_board");
    public static final RegistryObject<Block> BOP_UMBRAN_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("umbran_cutting_board");
    public static final RegistryObject<Block> BOP_WILLOW_CUTTING_BOARD = registerBiomesOPlentyCuttingBoard("willow_cutting_board");

    // Biomes We've Gone
    public static final RegistryObject<Block> BWG_ASPEN_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("aspen_cutting_board");
    public static final RegistryObject<Block> BWG_BAOBAB_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("baobab_cutting_board");
    public static final RegistryObject<Block> BWG_BLUE_ENCHANTED_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("blue_enchanted_cutting_board");
    public static final RegistryObject<Block> BWG_CIKA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("cika_cutting_board");
    public static final RegistryObject<Block> BWG_CYPRESS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("cypress_cutting_board");
    public static final RegistryObject<Block> BWG_EBONY_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("ebony_cutting_board");
    public static final RegistryObject<Block> BWG_FIR_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("fir_cutting_board");
    public static final RegistryObject<Block> BWG_FLORUS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("florus_cutting_board");
    public static final RegistryObject<Block> BWG_GREEN_ENCHANTED_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("green_enchanted_cutting_board");
    public static final RegistryObject<Block> BWG_HOLLY_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("holly_cutting_board");
    public static final RegistryObject<Block> BWG_IRONWOOD_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("ironwood_cutting_board");
    public static final RegistryObject<Block> BWG_JACARANDA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("jacaranda_cutting_board");
    public static final RegistryObject<Block> BWG_MAHOGANY_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("mahogany_cutting_board");
    public static final RegistryObject<Block> BWG_MAPLE_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("maple_cutting_board");
    public static final RegistryObject<Block> BWG_PALM_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("palm_cutting_board");
    public static final RegistryObject<Block> BWG_PINE_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("pine_cutting_board");
    public static final RegistryObject<Block> BWG_RAINBOW_EUCALYPTUS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("rainbow_eucalyptus_cutting_board");
    public static final RegistryObject<Block> BWG_REDWOOD_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("redwood_cutting_board");
    public static final RegistryObject<Block> BWG_SAKURA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("sakura_cutting_board");
    public static final RegistryObject<Block> BWG_SKYRIS_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("skyris_cutting_board");
    public static final RegistryObject<Block> BWG_WHITE_MANGROVE_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("white_mangrove_cutting_board");
    public static final RegistryObject<Block> BWG_WILLOW_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("willow_cutting_board");
    public static final RegistryObject<Block> BWG_WITCH_HAZEL_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("witch_hazel_cutting_board");
    public static final RegistryObject<Block> BWG_ZELKOVA_CUTTING_BOARD = registerBiomesWeveGoneCuttingBoard("zelkova_cutting_board");

    private static RegistryObject<Block> registerCuttingBoard(String name) {
        RegistryObject<Block> block = registerCuttingBoard(BLOCKS, name);
        MINECRAFT_CUTTING_BOARDS.add(block);

        return block;
    }

    private static RegistryObject<Block> registerBiomesOPlentyCuttingBoard(String name) {
        RegistryObject<Block> block = registerCuttingBoard(BIOMES_O_PLENTY_BLOCKS, name);
        BIOMES_O_PLENTY_CUTTING_BOARDS.add(block);

        return block;
    }

    private static RegistryObject<Block> registerBiomesWeveGoneCuttingBoard(String name) {
        RegistryObject<Block> block = registerCuttingBoard(BIOMES_WEVE_GONE_BLOCKS, name);
        BIOMES_WEVE_GONE_CUTTING_BOARDS.add(block);

        return block;
    }

    private static RegistryObject<Block> registerCuttingBoard(DeferredRegister<Block> registry, String name) {
        RegistryObject<Block> block = registry.register(name, ModBlocks::createCuttingBoard);
        CUTTING_BOARDS.add(block);

        return block;
    }

    private static ModCuttingBoardBlock createCuttingBoard() {
        return new ModCuttingBoardBlock(Block.Properties.copy(Blocks.OAK_PLANKS).strength(2.0f).sound(SoundType.WOOD));
    }

    private static RegistryObject<Block> registerDecoratedCuttingBoard(String name) {
        return BLOCKS.register(name, () -> new DecoratedCuttingBoardBlock(Block.Properties.copy(Blocks.OAK_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);

        if (Compatibility.IsBiomesOPlentyLoaded()) {
            BIOMES_O_PLENTY_BLOCKS.register(eventBus);
        }

        if (Compatibility.IsBiomesWeveGoneLoaded()) {
            BIOMES_WEVE_GONE_BLOCKS.register(eventBus);
        }
    }
}
