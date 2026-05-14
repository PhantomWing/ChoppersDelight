package com.phantomwing.choppersdelight;

import com.phantomwing.choppersdelight.block.ModBlockEntityTypes;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.block.custom.DecoratedCuttingBoardBlock;
import com.phantomwing.choppersdelight.component.ModDataComponents;
import com.phantomwing.choppersdelight.item.ModItems;

import com.phantomwing.choppersdelight.recipe.ModRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

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

		// Register a built-in resource pack that overrides FD's cutting_board model.
		// Fabric Loader orders regular mod packs alphabetically by mod ID, so "farmersdelight" beats
		// "choppersdelight" and overrides we put under assets/farmersdelight/... in our main resources
		// get ignored at runtime. Built-in packs registered through Fabric Resource Loader sit above
		// the regular mod-pack stack, so files inside them take precedence.
		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container ->
				ResourceManagerHelper.registerBuiltinResourcePack(
						ResourceLocation.fromNamespaceAndPath(MOD_ID, "farmersdelight_overrides"),
						container,
						ResourcePackActivationType.ALWAYS_ENABLED
				)
		);

		// Must run after ModBlocks is initialized so EveryCompat's WoodGood module
		// can reference OAK_CUTTING_BOARD as its base block.
		Compatibility.init();
	}
}
