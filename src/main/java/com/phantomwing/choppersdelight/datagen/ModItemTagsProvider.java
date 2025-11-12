package com.phantomwing.choppersdelight.datagen;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.tags.ModTags;
import com.phantomwing.choppersdelight.utils.BlockUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, ChoppersDelight.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(ModTags.Items.CUTTING_BOARDS)
            .add(ModItems.CUTTING_BOARD.get())
            .add(com.phantomwing.choppersdelight.item.ModItems.DECORATED_CUTTING_BOARD.get());

        // Add Vanilla cutting boards
        ModBlocks.MINECRAFT_CUTTING_BOARDS.forEach((blockSupplier -> {
            this.tag(ModTags.Items.CUTTING_BOARDS).add(blockSupplier.get().asItem());
        }));

        // Add Biomes O' Plenty cutting boards
        ModBlocks.BIOMES_O_PLENTY_CUTTING_BOARDS.forEach((blockSupplier -> {
            this.tag(ModTags.Items.CUTTING_BOARDS).addOptional(
                ResourceLocation.fromNamespaceAndPath(
                    Compatibility.BIOMES_O_PLENTY_MOD_ID,
                    BlockUtils.getName(blockSupplier.get()
                    )
                )
            );
        }));

        // Add Biomes We've Gone cutting boards
        ModBlocks.BIOMES_WEVE_GONE_CUTTING_BOARDS.forEach((blockSupplier -> {
            this.tag(ModTags.Items.CUTTING_BOARDS).addOptional(
                ResourceLocation.fromNamespaceAndPath(
                    Compatibility.BIOMES_WEVE_GONE_MOD_ID,
                    BlockUtils.getName(blockSupplier.get()
                    )
                )
            );
        }));
    }
}
