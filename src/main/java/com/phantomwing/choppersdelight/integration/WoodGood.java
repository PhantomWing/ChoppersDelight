package com.phantomwing.choppersdelight.integration;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.block.custom.ModCuttingBoardBlock;
import com.phantomwing.choppersdelight.tags.ModTags;
import com.phantomwing.choppersdelight.ui.ModCreativeModeTab;
import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.api.PaletteStrategies;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.world.level.block.Block;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.minecraftforge.registries.ForgeRegistries;

public class WoodGood extends SimpleModule {
    public final SimpleEntrySet<WoodType, Block> cuttingBoard;

    public WoodGood() {
        super(ChoppersDelight.MOD_ID, "chd", EveryCompat.MOD_ID);

        cuttingBoard = SimpleEntrySet.builder(WoodType.class, "cutting_board",
                        ModBlocks.OAK_CUTTING_BOARD, () -> VanillaWoodTypes.OAK,
                        w -> new ModCuttingBoardBlock(Utils.copyPropertySafe(w.log))
                )
                .addTexture(modRes("block/oak_cutting_board"), PaletteStrategies.PLANKS_STANDARD)
                .addTag(ModTags.Items.CUTTING_BOARDS.location(), ForgeRegistries.ITEMS.getRegistryKey())
                .addTag(ModTags.Blocks.CUTTING_BOARDS.location(), ForgeRegistries.BLOCKS.getRegistryKey())
                .setTabKey(ModCreativeModeTab.MOD_TAB.getKey())
                .defaultRecipe()
                .build();

        this.addEntry(cuttingBoard);
    }
}