package com.phantomwing.choppersdelight.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.phantomwing.choppersdelight.block.custom.DecoratedCuttingBoardBlock;
import com.phantomwing.choppersdelight.block.entity.DecoratedCuttingBoardBlockEntity;
import com.phantomwing.choppersdelight.utils.BannerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;

public class DecoratedCuttingBoardRenderer implements BlockEntityRenderer<DecoratedCuttingBoardBlockEntity, DecoratedCuttingBoardRenderer.State> {
    private final Model.Simple bannerModel;
    private final SpriteGetter sprites;
    private final ItemModelResolver itemModelResolver;

    public DecoratedCuttingBoardRenderer(BlockEntityRendererProvider.Context context) {
        this.bannerModel = BannerUtils.createBannerModel();
        this.sprites = context.sprites();
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public @NotNull State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(@NotNull DecoratedCuttingBoardBlockEntity blockEntity, @NotNull State state, float partialTick, @NotNull Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, cameraPos, breakProgress);
        state.direction = blockEntity.getBlockState().getValue(DecoratedCuttingBoardBlock.FACING).getOpposite();

        // The board itself.
        ItemStack board = blockEntity.getCuttingBoard();
        state.board = new ItemStackRenderState();
        itemModelResolver.updateForTopItem(state.board, board, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, (int) blockEntity.getBlockPos().asLong());

        // The banner on it.
        ItemStack banner = blockEntity.getBanner();
        if (!banner.isEmpty() && banner.getItem() instanceof BannerItem bannerItem) {
            state.bannerColor = bannerItem.getColor();
            state.bannerPatterns = banner.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
        } else {
            state.bannerColor = null;
        }

        extractBoardContent(blockEntity, state, partialTick, cameraPos, breakProgress);
    }

    // The content on the board, drawn by a CuttingBoardBlockEntity made from the decorated board.
    private void extractBoardContent(DecoratedCuttingBoardBlockEntity blockEntity, State state, float partialTick, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        state.contentRenderer = null;
        state.content = null;

        ItemStack board = blockEntity.getCuttingBoard();
        if (board != null && board.getItem() instanceof BlockItem blockItem) {
            BlockState oldBlockState = blockEntity.getBlockState();

            // Duplicate the BlockState to be of the correct type.
            BlockState blockState = blockItem.getBlock().defaultBlockState();
            blockState = blockState.setValue(CuttingBoardBlock.FACING, oldBlockState.getValue(DecoratedCuttingBoardBlock.FACING));
            blockState = blockState.setValue(CuttingBoardBlock.WATERLOGGED, oldBlockState.getValue(DecoratedCuttingBoardBlock.WATERLOGGED));

            // Create a CuttingBoardBlockEntity from the decorated board.
            // It gets no level: filling a board with one marks the chunks around it for rebuild, on every frame here.
            CuttingBoardBlockEntity cuttingBoard = new CuttingBoardBlockEntity(blockEntity.getBlockPos(), blockState);
            if (blockEntity.isItemCarvingBoard()) {
                cuttingBoard.carveToolOnBoard(blockEntity.getStoredItem().copy());
            } else {
                cuttingBoard.addItem(blockEntity.getStoredItem().copy());
            }

            BlockEntityRenderer<CuttingBoardBlockEntity, BlockEntityRenderState> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(cuttingBoard);
            if (renderer != null) {
                BlockEntityRenderState content = renderer.createRenderState();
                renderer.extractRenderState(cuttingBoard, content, partialTick, cameraPos, breakProgress);
                content.lightCoords = state.lightCoords;
                state.contentRenderer = renderer;
                state.content = content;
            }
        }
    }

    @Override
    public void submit(@NotNull State state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        // Render the board itself.
        if (state.board != null) {
            poseStack.pushPose();

            // Center block above the cutting board
            poseStack.translate(0.5D, 0.5D, 0.5D);

            // Rotate block to face the cutting board's front side
            float f = -state.direction.toYRot();
            poseStack.mulPose(Axis.YP.rotationDegrees(f));

            // Resize the block
            poseStack.scale(2.0F, 2.0F, 2.0F);

            state.board.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        if (state.bannerColor != null) {
            BannerUtils.submitBanner(sprites, poseStack, collector, bannerModel, state.lightCoords, OverlayTexture.NO_OVERLAY, state.direction, state.bannerColor, state.bannerPatterns, state.breakProgress, 0);
        }

        // Render the content on the board, based on the CuttingBoardBlockEntity rendering.
        if (state.contentRenderer != null && state.content != null) {
            poseStack.pushPose();
            state.contentRenderer.submit(state.content, poseStack, collector, camera);
            poseStack.popPose();
        }
    }

    public static class State extends BlockEntityRenderState {
        public Direction direction = Direction.NORTH;
        public @Nullable ItemStackRenderState board;
        public @Nullable DyeColor bannerColor;
        public BannerPatternLayers bannerPatterns = BannerPatternLayers.EMPTY;
        public @Nullable BlockEntityRenderer<CuttingBoardBlockEntity, BlockEntityRenderState> contentRenderer;
        public @Nullable BlockEntityRenderState content;
    }
}
