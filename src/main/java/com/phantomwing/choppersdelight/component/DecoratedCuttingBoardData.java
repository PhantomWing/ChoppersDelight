package com.phantomwing.choppersdelight.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record DecoratedCuttingBoardData(ItemStack cuttingBoard, ItemStack banner) {

    public static final Codec<DecoratedCuttingBoardData> CODEC = RecordCodecBuilder
        .create(instance ->
            instance.group(
                ItemStack.CODEC.fieldOf("cuttingBoard").forGetter(DecoratedCuttingBoardData::cuttingBoard),
                ItemStack.CODEC.fieldOf("banner").forGetter(DecoratedCuttingBoardData::banner)
            ).apply(instance, DecoratedCuttingBoardData::new)
        );

    public static final StreamCodec<RegistryFriendlyByteBuf, DecoratedCuttingBoardData> STREAM_CODEC =
        StreamCodec.composite(
            ItemStack.STREAM_CODEC, DecoratedCuttingBoardData::cuttingBoard,
            ItemStack.STREAM_CODEC, DecoratedCuttingBoardData::banner,
            DecoratedCuttingBoardData::new
        );

    public static final DataComponentType<DecoratedCuttingBoardData> TYPE =
        DataComponentType.<DecoratedCuttingBoardData>builder().persistent(CODEC).networkSynchronized(STREAM_CODEC).build();

    // ItemStack has no equals of its own, so without these two boards of one design never stacked.
    @Override
    public boolean equals(Object other) {
        return other instanceof DecoratedCuttingBoardData data
                && ItemStack.matches(cuttingBoard, data.cuttingBoard) && ItemStack.matches(banner, data.banner);
    }

    @Override
    public int hashCode() {
        return 31 * ItemStack.hashItemAndComponents(cuttingBoard) + ItemStack.hashItemAndComponents(banner);
    }
}