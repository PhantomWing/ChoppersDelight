package com.phantomwing.choppersdelight.integration;

import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.block.custom.ModCuttingBoardBlock;
import com.phantomwing.choppersdelight.tags.ModTags;
import net.mehvahdjukaar.every_compat.api.PaletteStrategies;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;

public class WoodGood extends SimpleModule {
    public final SimpleEntrySet<WoodType, Block> cuttingBoard;

    public WoodGood(String modId) {
        super(modId, "chd", Compatibility.EVERY_COMPAT_MOD_ID);

        ResourceLocation tab = modRes("main");

        cuttingBoard = SimpleEntrySet.builder(WoodType.class, "cutting_board",
                        ModBlocks.OAK_CUTTING_BOARD, () -> VanillaWoodTypes.OAK,
                        w -> new ModCuttingBoardBlock(Utils.copyPropertySafe(w.log))
                )
                .addTexture(modRes("block/oak_cutting_board"), PaletteStrategies.PLANKS_STANDARD)
                .addTag(ModTags.Items.CUTTING_BOARDS.location(), Registries.ITEM)
                .addTag(ModTags.Blocks.CUTTING_BOARDS.location(), Registries.BLOCK)
                .setTabKey(tab)
                .defaultRecipe()
                .build();
        this.addEntry(cuttingBoard);
    }
}