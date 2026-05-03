package com.phantomwing.choppersdelight.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.phantomwing.choppersdelight.block.custom.ModCuttingBoardBlock;
import com.phantomwing.choppersdelight.block.entity.ModCuttingBoardBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TridentItem;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Random;

public class ModCuttingBoardRenderer implements BlockEntityRenderer<ModCuttingBoardBlockEntity>
{
    private final Random random = new Random();

    public ModCuttingBoardRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    @Override
    public void render(ModCuttingBoardBlockEntity cuttingBoardEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemStack boardStack = cuttingBoardEntity.getStoredItem();
        if (boardStack.isEmpty()) {
            return;
        }

        Direction direction = cuttingBoardEntity.getBlockState().getValue(ModCuttingBoardBlock.FACING).getOpposite();
        int posLong = (int) cuttingBoardEntity.getBlockPos().asLong();
        int seed = boardStack.isEmpty() ? 187 : net.minecraft.world.item.Item.getId(boardStack.getItem()) + boardStack.getDamageValue();
        random.setSeed(seed);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        int modelCount = getModelCount(boardStack);

        for (int i = 0; i < modelCount; i++) {
            poseStack.pushPose();

            poseStack.pushPose();
            boolean isBlockItem = itemRenderer.getModel(boardStack, cuttingBoardEntity.getLevel(), null, 0).applyTransform(ItemDisplayContext.FIXED, poseStack, false).isGui3d();
            poseStack.popPose();

            float xOffset = modelCount == 1 ? 0.0F : (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
            float zOffset = modelCount == 1 ? 0.0F : (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;

            if (cuttingBoardEntity.isItemCarvingBoard()) {
                renderItemCarved(poseStack, direction, boardStack);
            } else if (isBlockItem && !boardStack.is(ModTags.Items.FLAT_ON_CUTTING_BOARD)) {
                renderBlock(poseStack, direction, xOffset, i, zOffset);
            } else {
                renderItemLayingDown(poseStack, direction, xOffset, i, zOffset);
            }

            Minecraft.getInstance().getItemRenderer().renderStatic(boardStack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffer, cuttingBoardEntity.getLevel(), posLong);
            poseStack.popPose();
        }
    }

    public void renderItemLayingDown(PoseStack matrixStackIn, Direction direction, float xOffset, int index, float zOffset) {
        // Center item above the cutting board, raising slightly per stacked layer
        matrixStackIn.translate(0.5D + xOffset, 0.08D + 0.03D * (index + 1), 0.5D + zOffset);

        // Rotate item to face the cutting board's front side
        float f = -direction.toYRot();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));

        // Rotate item flat on the cutting board. Use X and Y from now on
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));

        // Resize the item
        matrixStackIn.scale(0.6F, 0.6F, 0.6F);
    }

    public void renderBlock(PoseStack matrixStackIn, Direction direction, float xOffset, int index, float zOffset) {
        // Center block above the cutting board, raising slightly per stacked layer
        matrixStackIn.translate(0.5D + xOffset, 0.27D + 0.03D * (index + 1), 0.5D + zOffset);

        // Rotate block to face the cutting board's front side
        float f = -direction.toYRot();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));

        // Resize the block
        matrixStackIn.scale(0.8F, 0.8F, 0.8F);
    }

    public void renderItemCarved(PoseStack matrixStackIn, Direction direction, ItemStack itemStack) {
        // Center item above the cutting board
        matrixStackIn.translate(0.5D, 0.23D, 0.5D);

        // Rotate item to face the cutting board's front side
        float f = -direction.toYRot() + 180;
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));

        // Rotate item to be carved on the surface, A little less so for hoes and pickaxes.
        var toolItem = itemStack.getItem();
        float poseAngle;
        if (toolItem instanceof PickaxeItem || toolItem instanceof HoeItem) {
            poseAngle = 225.0F;
        } else if (toolItem instanceof TridentItem) {
            poseAngle = 135.0F;
        } else {
            poseAngle = 180.0F;
        }
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(poseAngle));

        // Resize the item
        matrixStackIn.scale(0.6F, 0.6F, 0.6F);
    }

    /**
     * Mirror of FD 1.3.1's CuttingBoardRenderer#getModelCount: at most 1 + 4 layered models
     * scaled by stack fill ratio. Single items render as one model with no offset.
     */
    protected int getModelCount(ItemStack stack) {
        int count = 1;
        if (stack.getCount() > 1) {
            count += Mth.ceil((float) stack.getCount() / (float) stack.getMaxStackSize() * 4.0F);
        }
        return count;
    }
}
