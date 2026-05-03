package com.phantomwing.choppersdelight;

import com.phantomwing.choppersdelight.integration.WoodGood;
import net.mehvahdjukaar.every_compat.api.EveryCompatAPI;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

/**
 * Separate class for EveryCompat integration setup.
 * This class is only loaded when EveryCompat is confirmed to be installed,
 * preventing NoClassDefFoundError when the mod is absent.
 */
public class EveryCompatSetup {
    private static WoodGood woodGood;

    public static void init() {
        woodGood = new WoodGood();
        EveryCompatAPI.registerModule(woodGood);
    }

    public static Stream<Block> getCuttingBoardBlocks() {
        if (woodGood == null) return Stream.empty();
        return woodGood.cuttingBoard.blocks.values().stream();
    }
}