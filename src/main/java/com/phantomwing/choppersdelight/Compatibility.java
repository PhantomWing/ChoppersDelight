package com.phantomwing.choppersdelight;

import net.minecraftforge.fml.ModList;

public class Compatibility {
    public static String BIOMES_O_PLENTY_MOD_ID = "biomesoplenty";
    public static String BIOMES_WEVE_GONE_MOD_ID = "biomeswevegone";

    private static boolean _isBiomesOPlentyLoaded = false;
    public static boolean IsBiomesOPlentyLoaded() {
        return _isBiomesOPlentyLoaded;
    }

    private static boolean _isBiomesWeveGoneLoaded = false;
    public static boolean IsBiomesWeveGoneLoaded() {
        return _isBiomesWeveGoneLoaded;
    }

    public static void checkInstalledMods() {
        ModList modList = ModList.get();
        _isBiomesOPlentyLoaded = modList.isLoaded(Compatibility.BIOMES_O_PLENTY_MOD_ID);
        _isBiomesWeveGoneLoaded = modList.isLoaded(Compatibility.BIOMES_WEVE_GONE_MOD_ID);
    }
}