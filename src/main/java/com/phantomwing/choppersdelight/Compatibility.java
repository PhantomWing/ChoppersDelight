package com.phantomwing.choppersdelight;

import net.minecraftforge.fml.ModList;

public class Compatibility {
    public static String BIOMES_O_PLENTY_MOD_ID = "biomesoplenty";
    public static String BIOMES_WEVE_GONE_MOD_ID = "biomeswevegone";
    public static String EVERY_COMPAT_MOD_ID = "everycomp";

    private static boolean _isBiomesOPlentyLoaded = false;
    public static boolean IsBiomesOPlentyLoaded() {
        return _isBiomesOPlentyLoaded;
    }

    private static boolean _isBiomesWeveGoneLoaded = false;
    public static boolean IsBiomesWeveGoneLoaded() {
        return _isBiomesWeveGoneLoaded;
    }

    private static boolean _isEveryCompatLoaded = false;
    public static boolean IsEveryCompatLoaded() {
        return _isEveryCompatLoaded;
    }

    public static void init() {
        ModList modList = ModList.get();
        _isBiomesOPlentyLoaded = modList.isLoaded(Compatibility.BIOMES_O_PLENTY_MOD_ID);
        _isBiomesWeveGoneLoaded = modList.isLoaded(Compatibility.BIOMES_WEVE_GONE_MOD_ID);
        _isEveryCompatLoaded = modList.isLoaded(Compatibility.EVERY_COMPAT_MOD_ID);
    }

    public static void initEveryCompat() {
        // Add Every Compat support if the mod is loaded.
        // This is done in a separate class to avoid loading EveryCompat classes when the mod is not installed.
        if (_isEveryCompatLoaded) {
            try {
                EveryCompatSetup.init();
            }
            catch (Exception e) {
                ChoppersDelight.LOGGER.error("Failed to initialize Every Compat support", e);
            }
        }
    }
}