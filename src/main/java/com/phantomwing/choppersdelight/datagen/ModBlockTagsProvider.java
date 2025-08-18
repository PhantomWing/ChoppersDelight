package com.phantomwing.choppersdelight.datagen;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.tags.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ChoppersDelight.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(ModTags.Blocks.CUTTING_BOARDS)
            .add(vectorwing.farmersdelight.common.registry.ModBlocks.CUTTING_BOARD.get())
            .add(ModBlocks.DECORATED_CUTTING_BOARD.get());

        ModBlocks.CUTTING_BOARDS.forEach((blockSupplier -> {
            this.tag(ModTags.Blocks.CUTTING_BOARDS).add(
                    blockSupplier.get()
            );
        }));
    }
}
