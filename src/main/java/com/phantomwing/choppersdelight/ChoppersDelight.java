package com.phantomwing.choppersdelight;

import com.mojang.logging.LogUtils;
import com.phantomwing.choppersdelight.block.ModBlockEntityTypes;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.item.ModItems;
import com.phantomwing.choppersdelight.recipe.ModRecipes;
import com.phantomwing.choppersdelight.ui.ModCreativeModeTab;
import com.phantomwing.choppersdelight.utils.BlockUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

@Mod(ChoppersDelight.MOD_ID)
public class ChoppersDelight
{
    public static final String MOD_ID = "choppersdelight";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ChoppersDelight()
    {
        Compatibility.checkInstalledMods();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        registerManagers(modEventBus);
    }

    // Register all managers to the event bus.
    private void registerManagers(IEventBus eventBus) {
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModBlockEntityTypes.register(eventBus);
        ModRecipes.register(eventBus);

        if (FMLEnvironment.dist.isClient()) {
            ModCreativeModeTab.register(eventBus);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // add all cutting boards from your mod to the Farmers Delight cutting board block entity type
            Block[] additional = BlockUtils.getCuttingBoards().toArray(Block[]::new);
            addValidBlocksTo(vectorwing.farmersdelight.common.registry.ModBlockEntityTypes.CUTTING_BOARD.get(), additional);
        });
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }
    }


    // Reflection helper: append blocks to the private validBlocks/set field of a BlockEntityType
    public static void addValidBlocksTo(BlockEntityType<?> targetType, Block... blocks) {
        try {
            Field targetField = null;
            // try common field names first
            for (String name : new String[] { "validBlocks", "valid_blocks", "blocks" }) {
                try {
                    targetField = BlockEntityType.class.getDeclaredField(name);
                    break;
                } catch (NoSuchFieldException ignored) {}
            }
            // fallback: find first field of type java.util.Set
            if (targetField == null) {
                for (Field f : BlockEntityType.class.getDeclaredFields()) {
                    if (Set.class.isAssignableFrom(f.getType())) {
                        targetField = f;
                        break;
                    }
                }
            }
            if (targetField == null) {
                ChoppersDelight.LOGGER.error("Could not find a Set field on BlockEntityType to modify valid blocks.");
                return;
            }

            targetField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Set<Block> set = (Set<Block>) targetField.get(targetType);
            if (set == null) {
                ChoppersDelight.LOGGER.error("The target BlockEntityType's block set is null.");
                return;
            }
            set.addAll(Arrays.asList(blocks));
            ChoppersDelight.LOGGER.info("Added {} blocks to BlockEntityType {}", blocks.length, targetType);
        } catch (Throwable t) {
            ChoppersDelight.LOGGER.error("Failed to add valid blocks to BlockEntityType", t);
        }
    }
}
