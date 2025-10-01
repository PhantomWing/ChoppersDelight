package com.phantomwing.choppersdelight.utils;

import com.phantomwing.choppersdelight.ChoppersDelight;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class RegisterUtils {
    public static <R, T extends R> Supplier<T> register(String name, Supplier<T> supplier, Registry<R> reg) {
        T object = supplier.get();
        Registry.register(reg, ResourceLocation.fromNamespaceAndPath(ChoppersDelight.MOD_ID, name), object);
        return () -> object;
    }

}
