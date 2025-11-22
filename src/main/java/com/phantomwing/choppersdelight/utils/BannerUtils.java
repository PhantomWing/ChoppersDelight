package com.phantomwing.choppersdelight.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import com.phantomwing.choppersdelight.block.entity.DecoratedCuttingBoardBlockEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BannerUtils {
    public static ModelPart initializeBannerModelPart() {
        CubeListBuilder cubeListBuilder = new CubeListBuilder();
        cubeListBuilder.texOffs(0, 0);
        cubeListBuilder.addBox(-10.0F, 0.0F, -2.0F, 20.0F, 40.0F, 1.0F);
        List<ModelPart.Cube> cuboids = cubeListBuilder.getCubes().stream().map(modelCuboidData -> modelCuboidData.bake(64, 64)).collect(Collectors.toList());

        return new ModelPart(cuboids, new HashMap<>());
    }

    public static void renderBanner(PoseStack poseStack, MultiBufferSource buffer, ModelPart modelPart, int light, int overlay, Direction direction, DecoratedCuttingBoardBlockEntity blockEntity) {
        ItemStack banner = blockEntity.getBanner();

        List<Pair<Holder<BannerPattern>, DyeColor>> patternLayers = BannerUtils.getPatternLayers(banner);

        if (patternLayers != null) {
            BannerUtils.renderBanner(poseStack, buffer, modelPart, light, overlay, direction, patternLayers);
        }
    }

    public static List<Pair<Holder<BannerPattern>, DyeColor>> getPatternLayers(ItemStack banner) {
        if (!banner.isEmpty() && banner.getItem() instanceof BannerItem bannerItem) {
            CompoundTag compoundTag = BlockItem.getBlockEntityData(banner);
            if (compoundTag != null && compoundTag.contains("Patterns")) {
                ListTag listtag = compoundTag.getList("Patterns", 10);

                DyeColor dyeColor = bannerItem.getColor();

                return BannerBlockEntity.createPatterns(dyeColor, listtag);
            }
        }

        return null;
    }

    public static void renderBanner(PoseStack poseStack, MultiBufferSource buffer, ModelPart modelPart, int light, int overlay, Direction direction, List<Pair<Holder<BannerPattern>, DyeColor>> patternLayers) {
        poseStack.pushPose();

        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F)); // Make sure the texture is flat on the Cutting Board
        poseStack.mulPose(Axis.ZP.rotationDegrees(direction.toYRot()));
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        // Position correctly
        poseStack.translate(0.5D - 1/32D - 3/16D, 0.75D + 3/16D, 1.0D + 1/16D);

        // Prevent Z-fighting
        poseStack.translate(0.0D, 0.0001D, 0.01D);

        // Scale
        poseStack.scale(-0.351F, -0.351F, 1.1F);
        BannerRenderer.renderPatterns(poseStack, buffer, light, overlay, modelPart, ModelBakery.BANNER_BASE, true, patternLayers);

        poseStack.popPose();
    }

    public static BakedModel handleCameraTransforms(PoseStack poseStack, BakedModel model, ItemDisplayContext cameraTransformType, boolean applyLeftHandTransform) {
        model = model.applyTransform(cameraTransformType, poseStack, applyLeftHandTransform);
        return model;
    }
}
