package com.phantomwing.choppersdelight.component;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.utils.RegisterUtils;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final Supplier<DataComponentType<DecoratedCuttingBoardData>> DECORATED_CUTTING_BOARD_DATA =
            register("decorated_cutting_board_data", builder -> builder.persistent(DecoratedCuttingBoardData.CODEC));

    private static <T>Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return registerDataComponent(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    private static <T>Supplier<DataComponentType<T>> registerDataComponent(String name, Supplier<DataComponentType<T>> supplier) {
        return RegisterUtils.register(name, supplier, BuiltInRegistries.DATA_COMPONENT_TYPE);
    }

    public static void registerModDataComponents() {
        ChoppersDelight.LOGGER.info("Registering data components for " + ChoppersDelight.MOD_ID);
    }
}
