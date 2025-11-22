package com.phantomwing.choppersdelight.datagen;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;

import com.phantomwing.choppersdelight.tags.ModTags;
import com.phantomwing.choppersdelight.utils.BlockUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ChoppersDelight.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(com.phantomwing.choppersdelight.tags.ModTags.Blocks.CUTTING_BOARDS)
                .add(vectorwing.farmersdelight.common.registry.ModBlocks.CUTTING_BOARD.get())
                .add(ModBlocks.DECORATED_CUTTING_BOARD.get());

        // Add Vanilla cutting boards
        ModBlocks.MINECRAFT_CUTTING_BOARDS.forEach((blockSupplier -> {
            this.tag(com.phantomwing.choppersdelight.tags.ModTags.Blocks.CUTTING_BOARDS).add(blockSupplier.get());
        }));

        // Add Biomes O' Plenty cutting boards
        if (Compatibility.IsBiomesOPlentyLoaded()) {
            ModBlocks.BIOMES_O_PLENTY_CUTTING_BOARDS.forEach((blockSupplier -> {
                this.tag(com.phantomwing.choppersdelight.tags.ModTags.Blocks.CUTTING_BOARDS).addOptional(
                        ResourceLocation.fromNamespaceAndPath(
                                Compatibility.BIOMES_O_PLENTY_MOD_ID,
                                BlockUtils.getName(blockSupplier.get())
                        )
                );
            }));
        }

        // Add Biomes We've Gone cutting boards
        if (Compatibility.IsBiomesWeveGoneLoaded()) {
            ModBlocks.BIOMES_WEVE_GONE_CUTTING_BOARDS.forEach((blockSupplier -> {
                this.tag(ModTags.Blocks.CUTTING_BOARDS).addOptional(
                        ResourceLocation.fromNamespaceAndPath(
                                Compatibility.BIOMES_WEVE_GONE_MOD_ID,
                                BlockUtils.getName(blockSupplier.get())
                        )
                );
            }));
        }
    }
}
