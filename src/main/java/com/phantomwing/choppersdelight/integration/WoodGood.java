package com.phantomwing.choppersdelight.integration;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.block.custom.ModCuttingBoardBlock;
import com.phantomwing.choppersdelight.tags.ModTags;
import com.phantomwing.choppersdelight.ui.ModCreativeModeTab;
import net.mehvahdjukaar.every_compat.api.PaletteStrategies;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.world.level.block.Block;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

public class WoodGood extends SimpleModule {
    // Wood namespaces Chopper's Delight already covers via manual registration. EveryCompat must skip these
    // to avoid registering duplicate cutting boards (kept for backwards compat with existing worlds).
    private static final Set<String> MANUALLY_HANDLED_NAMESPACES = Set.of(
            "minecraft",
            Compatibility.BIOMES_O_PLENTY_MOD_ID,
            Compatibility.BIOMES_WEVE_GONE_MOD_ID
    );

    public final SimpleEntrySet<WoodType, Block> cuttingBoard;

    public WoodGood() {
        super(ChoppersDelight.MOD_ID, "chd");

        cuttingBoard = SimpleEntrySet.builder(WoodType.class, "cutting_board",
                        ModBlocks.OAK_CUTTING_BOARD, () -> VanillaWoodTypes.OAK,
                        w -> new ModCuttingBoardBlock(Utils.copyPropertySafe(w.log))
                )
                .addTexture(modRes("block/oak_cutting_board"), PaletteStrategies.PLANKS_STANDARD)
                .addTag(ModTags.Items.CUTTING_BOARDS.location(), ForgeRegistries.ITEMS.getRegistryKey())
                .addTag(ModTags.Blocks.CUTTING_BOARDS.location(), ForgeRegistries.BLOCKS.getRegistryKey())
                .setTabKey(ModCreativeModeTab.MOD_TAB.getKey())
                .defaultRecipe()
                .addCondition(w -> !MANUALLY_HANDLED_NAMESPACES.contains(w.getNamespace()))
                .build();

        this.addEntry(cuttingBoard);
    }
}