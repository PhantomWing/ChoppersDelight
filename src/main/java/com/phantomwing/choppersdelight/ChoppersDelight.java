package com.phantomwing.choppersdelight;

import com.mojang.logging.LogUtils;
import com.phantomwing.choppersdelight.block.ModBlockEntityTypes;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.item.ModItems;
import com.phantomwing.choppersdelight.recipe.ModRecipes;
import com.phantomwing.choppersdelight.renderer.DecoratedCuttingBoardRenderer;
import com.phantomwing.choppersdelight.renderer.ModCuttingBoardRenderer;

import com.phantomwing.choppersdelight.ui.ModCreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(ChoppersDelight.MOD_ID)
public class ChoppersDelight
{
    public static final String MOD_ID = "choppersdelight";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ChoppersDelight()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        Compatibility.init();
        registerManagers(modEventBus);
        Compatibility.initEveryCompat();
    }

    // Register all managers to the event bus.
    private void registerManagers(IEventBus eventBus) {
        ModBlocks.register(eventBus);
        ModItems.register(eventBus);
        ModBlockEntityTypes.register(eventBus);
        ModRecipes.register(eventBus);
        ModCreativeModeTab.register(eventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntityTypes.DECORATED_CUTTING_BOARD.get(), DecoratedCuttingBoardRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntityTypes.MOD_CUTTING_BOARD.get(), ModCuttingBoardRenderer::new);
        }
    }
}
