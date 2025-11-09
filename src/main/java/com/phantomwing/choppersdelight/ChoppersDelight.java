package com.phantomwing.choppersdelight;

import com.phantomwing.choppersdelight.block.ModBlockEntityTypes;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.component.ModDataComponents;
import com.phantomwing.choppersdelight.item.ModItems;

import com.phantomwing.choppersdelight.recipe.ModRecipes;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChoppersDelight implements ModInitializer {
	public static final String MOD_ID = "choppersdelight";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Compatibility.checkInstalledMods();

		// Items
		ModBlockEntityTypes.registerModBlockEntityTypes();
		ModDataComponents.registerModDataComponents();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModRecipes.registerModRecipes();
	}
}