package com.phantomwing.choppersdelight.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.item.custom.DecoratedCuttingBoardItem;
import com.phantomwing.choppersdelight.utils.BannerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Consumer;

/**
 * The decorated cutting board's item: the board it was made from, with the banner laid on it.
 * Named by {@code {"type": "choppersdelight:decorated_cutting_board"}} in its items/ definition, whose base model
 * gives the display transforms.
 */
public class DecoratedCuttingBoardItemRenderer implements SpecialModelRenderer<DecoratedCuttingBoardItemRenderer.Decoration> {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(ChoppersDelight.MOD_ID, "decorated_cutting_board");

    private final Model.Simple bannerModel;
    private final SpriteGetter sprites;

    public DecoratedCuttingBoardItemRenderer(SpriteGetter sprites) {
        this.bannerModel = BannerUtils.createBannerModel();
        this.sprites = sprites;
    }

    public static void register() {
        SpecialModelRenderers.ID_MAPPER.put(ID, Unbaked.MAP_CODEC);
    }

    @Override
    public @Nullable Decoration extractArgument(@NotNull ItemStack stack) {
        ItemStack board = DecoratedCuttingBoardItem.getCuttingBoardStack(stack);
        ItemStack banner = DecoratedCuttingBoardItem.getBannerStack(stack);

        // Apply fallbacks if needed.
        Item boardItem = board.isEmpty() ? ModItems.CUTTING_BOARD.get() : board.getItem();
        DyeColor color = banner.getItem() instanceof BannerItem bannerItem ? bannerItem.getColor() : DyeColor.WHITE;
        return new Decoration(boardItem, color, banner.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
    }

    @Override
    public void submit(@Nullable Decoration decoration, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, int light, int overlay, boolean hasFoil, int outlineColor) {
        if (decoration == null) {
            return;
        }

        // The base model's display transforms are already applied, so the board is drawn without its own.
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        // A state of its own each time: what is submitted is drawn later, from the state it was given.
        ItemStackRenderState boardRenderState = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver().updateForTopItem(boardRenderState, new ItemStack(decoration.board()), ItemDisplayContext.NONE, null, null, 0);
        boardRenderState.submit(poseStack, collector, light, overlay, outlineColor);
        poseStack.popPose();

        BannerUtils.submitBanner(sprites, poseStack, collector, bannerModel, light, overlay, Direction.SOUTH, decoration.color(), decoration.patterns(), null, outlineColor);
    }

    @Override
    public void getExtents(@NotNull Consumer<Vector3fc> output) {
        // The board lies flat at the bottom of the block, the banner a pixel's height on top of it.
        for (float x : new float[] {1.0F / 16.0F, 15.0F / 16.0F}) {
            for (float y : new float[] {0.0F, 0.07F}) {
                for (float z : new float[] {1.0F / 16.0F, 15.0F / 16.0F}) {
                    output.accept(new Vector3f(x, y, z));
                }
            }
        }
    }

    /**
     * What the item draws, compared by content: the GUI caches each item's picture by it from 1.21.6.
     */
    public record Decoration(Item board, DyeColor color, BannerPatternLayers patterns) {
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<Decoration> {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<Decoration> bake(@NotNull SpecialModelRenderer.BakingContext context) {
            return new DecoratedCuttingBoardItemRenderer(context.sprites());
        }

        @Override
        public @NotNull MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
