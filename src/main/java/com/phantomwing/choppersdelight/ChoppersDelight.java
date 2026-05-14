package com.phantomwing.choppersdelight;

import com.phantomwing.choppersdelight.block.ModBlockEntityTypes;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.block.custom.DecoratedCuttingBoardBlock;
import com.phantomwing.choppersdelight.component.ModDataComponents;
import com.phantomwing.choppersdelight.item.ModItems;

import com.phantomwing.choppersdelight.recipe.ModRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChoppersDelight implements ModInitializer {
	public static final String MOD_ID = "choppersdelight";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Items
		ModBlockEntityTypes.registerModBlockEntityTypes();
		ModDataComponents.registerModDataComponents();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModRecipes.registerModRecipes();

		// Sneak + tool carving on our decorated cutting boards
		// (FD's own callback only matches CuttingBoardBlockEntity, so we need our own for DecoratedCuttingBoardBlockEntity).
		UseBlockCallback.EVENT.register(DecoratedCuttingBoardBlock.ToolCarvingEvent::onSneakPlaceTool);

		// Must run after ModBlocks is initialized so EveryCompat's WoodGood module
		// can reference OAK_CUTTING_BOARD as its base block.
		Compatibility.init();
	}
}
