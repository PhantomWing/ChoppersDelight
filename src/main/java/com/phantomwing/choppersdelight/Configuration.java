package com.phantomwing.choppersdelight;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Configuration {
    public static ModConfigSpec COMMON_CONFIG;

    static {
        ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

        // Build config
        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
