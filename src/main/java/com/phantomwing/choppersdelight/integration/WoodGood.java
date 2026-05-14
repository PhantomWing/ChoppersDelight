package com.phantomwing.choppersdelight.integration;

import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.itemGroup.ModItemGroups;
import com.phantomwing.choppersdelight.tags.ModTags;
import net.mehvahdjukaar.every_compat.api.PaletteStrategies;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;

public class WoodGood extends SimpleModule {
    public final SimpleEntrySet<WoodType, Block> cuttingBoard;

    public WoodGood(String modId) {
        super(modId, "chd", Compatibility.EVERY_COMPAT_MOD_ID);

        cuttingBoard = SimpleEntrySet.builder(WoodType.class, "cutting_board",
                        ModBlocks.OAK_CUTTING_BOARD, () -> VanillaWoodTypes.OAK,
                        w -> new CuttingBoardBlock(Utils.copyPropertySafe(w.log))
                )
                .addTexture(modRes("block/oak_cutting_board"), PaletteStrategies.PLANKS_STANDARD)
                .addTag(ModTags.Items.CUTTING_BOARDS, Registries.ITEM)
                .addTag(ModTags.Blocks.CUTTING_BOARDS, Registries.BLOCK)
                .setTabKey(BuiltInRegistries.CREATIVE_MODE_TAB.getKey(ModItemGroups.MOD_TAB.get()))
                .defaultRecipe()
                .build();
        this.addEntry(cuttingBoard);
    }
}
