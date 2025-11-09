package com.phantomwing.choppersdelight.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.phantomwing.choppersdelight.item.custom.DecoratedCuttingBoardItem;
import com.phantomwing.choppersdelight.utils.BannerUtils;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.registry.ModItems;

public class DecoratedCuttingBoardItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    public static final DecoratedCuttingBoardItemRenderer INSTANCE = new DecoratedCuttingBoardItemRenderer();
    private final ModelPart bannerModelPart;

    public DecoratedCuttingBoardItemRenderer() {
        this.bannerModelPart = BannerUtils.initializeBannerModelPart();
    }

    @Override
    public void render(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
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
            model.getTransforms().getTransform(displayContext).apply(false, poseStack);
            poseStack.translate(-0.5F, -0.5F, -0.5F);

            // Render the banner.
            BannerUtils.renderBanner(poseStack, buffer, bannerModelPart, light, overlay, Direction.SOUTH, bannerItem.getColor(), bannerItemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
        }
    }
}