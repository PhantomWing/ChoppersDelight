package com.phantomwing.choppersdelight.block;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.block.entity.DecoratedCuttingBoardBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.stream.Stream;

@EventBusSubscriber(modid = ChoppersDelight.MOD_ID)
public class ModBlockEntityTypes
{
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ChoppersDelight.MOD_ID);

    public static final Supplier<BlockEntityType<DecoratedCuttingBoardBlockEntity>> DECORATED_CUTTING_BOARD = TILES.register("decorated_cutting_board", () -> BlockEntityType.Builder.of(DecoratedCuttingBoardBlockEntity::new,
        ModBlocks.DECORATED_CUTTING_BOARD.get()
    ).build(null));

    public static void register(IEventBus eventBus) {
        TILES.register(eventBus);
    }

    @SubscribeEvent
    public static void onRegisterFarmersDelightBlockEntityTypes(final BlockEntityTypeAddBlocksEvent event) {
        Block[] blocks = ModBlocks.CUTTING_BOARDS.stream().map(Supplier::get).toArray(Block[]::new);
        event.modify(vectorwing.farmersdelight.common.registry.ModBlockEntityTypes.CUTTING_BOARD.get(), blocks);
    }
}