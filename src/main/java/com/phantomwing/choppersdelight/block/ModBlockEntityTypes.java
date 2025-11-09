package com.phantomwing.choppersdelight.block;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.block.entity.DecoratedCuttingBoardBlockEntity;
import com.phantomwing.choppersdelight.utils.RegisterUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModBlockEntityTypes
{
    public static final Supplier<BlockEntityType<DecoratedCuttingBoardBlockEntity>> DECORATED_CUTTING_BOARD = registerBlockEntity("decorated_cutting_board", () -> BlockEntityType.Builder.of(DecoratedCuttingBoardBlockEntity::new,
        ModBlocks.DECORATED_CUTTING_BOARD.get()
    ).build());

    public static <T extends BlockEntity, B extends BlockEntityType<T>> Supplier<B> registerBlockEntity(String name, Supplier<B> supplier) {
        return RegisterUtils.register(name, supplier, BuiltInRegistries.BLOCK_ENTITY_TYPE);
    }

    public static <T extends BlockEntity, B extends BlockEntityType<T>> Supplier<B> registerBlockEntity(String name, Supplier<B> supplier, String namespace) {
        return RegisterUtils.register(name, supplier, BuiltInRegistries.BLOCK_ENTITY_TYPE, namespace);
    }

    public static void registerModBlockEntityTypes() {
        ChoppersDelight.LOGGER.info("Registering block entity types for " + ChoppersDelight.MOD_ID);
    }
}