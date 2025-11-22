package com.phantomwing.choppersdelight.block;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.block.entity.DecoratedCuttingBoardBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntityTypes
{
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ChoppersDelight.MOD_ID);

    public static final Supplier<BlockEntityType<DecoratedCuttingBoardBlockEntity>> DECORATED_CUTTING_BOARD = TILES.register("decorated_cutting_board", () -> BlockEntityType.Builder.of(DecoratedCuttingBoardBlockEntity::new,
        ModBlocks.DECORATED_CUTTING_BOARD.get()
    ).build(null));

    public static void register(IEventBus eventBus) {
        TILES.register(eventBus);
    }
}