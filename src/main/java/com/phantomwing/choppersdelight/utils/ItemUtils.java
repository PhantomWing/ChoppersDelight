package com.phantomwing.choppersdelight.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ItemUtils {
    public static String getName(Item item) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath();
    }

    public static ResourceLocation getResourceLocation(String modId, Item item) {
        return ResourceLocation.fromNamespaceAndPath(modId, "item/" + getName(item));
    }
}
