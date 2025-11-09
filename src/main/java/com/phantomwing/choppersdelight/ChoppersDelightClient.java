package com.phantomwing.choppersdelight;

import com.phantomwing.choppersdelight.block.ModBlockEntityTypes;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.itemGroup.ModItemGroups;
import com.phantomwing.choppersdelight.renderer.DecoratedCuttingBoardItemRenderer;
import com.phantomwing.choppersdelight.renderer.DecoratedCuttingBoardRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class ChoppersDelightClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModItemGroups.registerModItemGroups();

        BlockEntityRenderers.register(ModBlockEntityTypes.DECORATED_CUTTING_BOARD.get(), DecoratedCuttingBoardRenderer::new);
        BuiltinItemRendererRegistry.INSTANCE.register(ModBlocks.DECORATED_CUTTING_BOARD.get(), DecoratedCuttingBoardItemRenderer.INSTANCE);
    }
}