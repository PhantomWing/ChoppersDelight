package com.phantomwing.choppersdelight.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.phantomwing.choppersdelight.block.custom.DecoratedCuttingBoardBlock;
import com.phantomwing.choppersdelight.block.entity.DecoratedCuttingBoardBlockEntity;
import com.phantomwing.choppersdelight.utils.BannerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;

public class DecoratedCuttingBoardRenderer implements BlockEntityRenderer<DecoratedCuttingBoardBlockEntity> {
    private final ModelPart bannerModelPart;

    public DecoratedCuttingBoardRenderer(BlockEntityRendererProvider.Context context) {
        this.bannerModelPart = BannerUtils.initializeBannerModelPart();
    }

    public void render(DecoratedCuttingBoardBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        Direction direction = blockEntity.getBlockState().getValue(DecoratedCuttingBoardBlock.FACING).getOpposite();

        // Render the board itself.
        renderBoard(direction, blockEntity, partialTicks, poseStack, buffer, light, overlay);
        BannerUtils.renderBanner(poseStack, buffer, this.bannerModelPart, light, overlay, direction, blockEntity);

        // Render the content on the board, based on the CuttingBoardBlockEntity rendering.
        renderBoardContent(blockEntity, partialTicks, poseStack, buffer, light, overlay);
    }

    private void renderBoard(Direction direction, DecoratedCuttingBoardBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        Level level = blockEntity.getLevel();
        int seed = (int) blockEntity.getBlockPos().asLong();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        // Render cutting board.
        ItemStack board = blockEntity.getCuttingBoard();
        if (board != null) {
            poseStack.pushPose();

            // Center block above the cutting board
            poseStack.translate(0.5D, 0.5D, 0.5D);

            // Rotate block to face the cutting board's front side
            float f = -direction.toYRot();
            poseStack.mulPose(Axis.YP.rotationDegrees(f));

            // Resize the block
            poseStack.scale(2.0F, 2.0F, 2.0F);

            itemRenderer.renderStatic(board, ItemDisplayContext.FIXED, light, overlay, poseStack, buffer, level, seed);
            poseStack.popPose();
        }
    }

    // Create a CuttingBoardBlockEntity instance and render it.
    private void renderBoardContent(DecoratedCuttingBoardBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        ItemStack board = blockEntity.getCuttingBoard();

        if (board != null && board.getItem() instanceof BlockItem blockItem) {
            BlockState oldBlockState = blockEntity.getBlockState();

            // Duplicate the BlockState to be of the correct type.
            BlockState blockState = blockItem.getBlock().defaultBlockState();
            blockState = blockState.setValue(CuttingBoardBlock.FACING, oldBlockState.getValue(DecoratedCuttingBoardBlock.FACING));
            blockState = blockState.setValue(CuttingBoardBlock.WATERLOGGED, oldBlockState.getValue(DecoratedCuttingBoardBlock.WATERLOGGED));

            // Create a CuttingBoardBlockEntity from the decorated board.
            CuttingBoardBlockEntity cuttingBoard = new CuttingBoardBlockEntity(blockEntity.getBlockPos(), blockState);
            cuttingBoard.addItem(blockEntity.getStoredItem().copy());

            // Render the new CuttingBoardBlockEntity.
            BlockEntityRenderDispatcher dispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
            BlockEntityRenderer<CuttingBoardBlockEntity> boardRenderer = dispatcher.getRenderer(cuttingBoard);

            if (boardRenderer != null) {
                poseStack.pushPose();
                boardRenderer.render(cuttingBoard, partialTicks, poseStack, buffer, light, overlay);
                poseStack.popPose();
            }
        }
    }
}