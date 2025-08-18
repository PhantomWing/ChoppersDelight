package com.phantomwing.choppersdelight.renderer;

import com.phantomwing.choppersdelight.item.custom.DecoratedCuttingBoardItem;
import com.phantomwing.choppersdelight.utils.BannerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.neoforged.neoforge.client.ClientHooks;
import vectorwing.farmersdelight.common.registry.ModItems;

import javax.annotation.Nonnull;

public class DecoratedCuttingBoardItemStackRenderer extends BlockEntityWithoutLevelRenderer {
    private final ModelPart bannerModelPart;

    public DecoratedCuttingBoardItemStackRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);

        this.bannerModelPart = BannerUtils.initializeBannerModelPart();
    }

    @Override
    public void renderByItem(@Nonnull ItemStack stack, @Nonnull ItemDisplayContext displayContext, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        // Initialize item.
        ItemStack cuttingBoardItem = DecoratedCuttingBoardItem.getCuttingBoardStack(stack);

        // Apply fallback if needed.
        if (cuttingBoardItem.isEmpty())
        {
            cuttingBoardItem = new ItemStack(ModItems.CUTTING_BOARD.get());
        }

        poseStack.translate(0.5, 0.5, 0.5);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderStatic(cuttingBoardItem, displayContext, light,
                overlay, poseStack, buffer, null, 0);

        // Render banner
        ItemStack bannerItemStack = DecoratedCuttingBoardItem.getBannerStack(stack);

        // Apply fallback if needed.
        if (bannerItemStack.isEmpty())
        {
            bannerItemStack = new ItemStack(Items.WHITE_BANNER);
        }

        if (bannerItemStack.getItem() instanceof BannerItem bannerItem) {
            // Prepare PoseStack based on display context.
            BakedModel model = itemRenderer.getModel(cuttingBoardItem, null, null, 0);
            ClientHooks.handleCameraTransforms(poseStack, model, displayContext, false);
            poseStack.translate(-0.5F, -0.5F, -0.5F);

            // Render the banner.
            BannerUtils.renderBanner(poseStack, buffer, bannerModelPart, light, overlay, Direction.SOUTH, bannerItem.getColor(), bannerItemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
        }
    }




}