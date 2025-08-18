package com.phantomwing.choppersdelight;

import com.phantomwing.choppersdelight.component.ModDataComponents;
import com.phantomwing.choppersdelight.recipe.ModRecipes;
import com.phantomwing.choppersdelight.renderer.DecoratedCuttingBoardItemStackRenderer;
import com.phantomwing.choppersdelight.renderer.DecoratedCuttingBoardRenderer;
import com.phantomwing.choppersdelight.block.ModBlockEntityTypes;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.item.ModItems;
import com.phantomwing.choppersdelight.ui.ModCreativeModTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(ChoppersDelight.MOD_ID)
public class ChoppersDelight {
    public static final String MOD_ID = "choppersdelight";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ChoppersDelight(IEventBus eventBus, ModContainer modContainer) {
        eventBus.addListener(this::commonSetup);

        modContainer.registerConfig(ModConfig.Type.COMMON, Configuration.COMMON_CONFIG);

        // This will use NeoForge's ConfigurationScreen to display this mod's configs (Client only)
        if (FMLEnvironment.dist.isClient()) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }

        NeoForge.EVENT_BUS.register(this);

        registerManagers(eventBus);
    }

    // Register all managers to the event bus.
    private void registerManagers(IEventBus eventBus) {
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModBlockEntityTypes.register(eventBus);
        ModCreativeModTab.register(eventBus);
        ModDataComponents.register(eventBus);
        ModRecipes.register(eventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntityTypes.DECORATED_CUTTING_BOARD.get(), DecoratedCuttingBoardRenderer::new);
        }

        @SubscribeEvent
        public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
            event.registerItem(
                    new IClientItemExtensions() {
                        private final BlockEntityWithoutLevelRenderer renderer =
                                new DecoratedCuttingBoardItemStackRenderer(
                                        Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                                        Minecraft.getInstance().getEntityModels()
                                );

                        @Override
                        public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                            return renderer;
                        }
                    },
                    ModItems.DECORATED_CUTTING_BOARD.get()
            );
        }
    }
}
