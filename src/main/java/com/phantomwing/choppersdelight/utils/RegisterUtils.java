package com.phantomwing.choppersdelight.utils;

import com.phantomwing.choppersdelight.ChoppersDelight;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class RegisterUtils {
    public static <R, T extends R> Supplier<T> register(String name, Supplier<T> supplier, Registry<R> reg, String namespace) {
        T object = supplier.get();
        Registry.register(reg, Identifier.fromNamespaceAndPath(namespace, name), object);
        return () -> object;
    }

    public static <R, T extends R> Supplier<T> register(String name, Supplier<T> supplier, Registry<R> reg) {
        T object = supplier.get();
        Registry.register(reg, Identifier.fromNamespaceAndPath(ChoppersDelight.MOD_ID, name), object);
        return () -> object;
    }
}
