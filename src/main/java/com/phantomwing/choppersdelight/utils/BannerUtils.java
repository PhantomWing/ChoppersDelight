package com.phantomwing.choppersdelight.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Direction;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BannerUtils {
    public static Model.Simple createBannerModel() {
        CubeListBuilder cubeListBuilder = new CubeListBuilder();
        cubeListBuilder.texOffs(0, 0);
        cubeListBuilder.addBox(-10.0F, 0.0F, -2.0F, 20.0F, 40.0F, 1.0F);
        List<ModelPart.Cube> cuboids = cubeListBuilder.getCubes().stream().map(modelCuboidData -> modelCuboidData.bake(64, 64)).collect(Collectors.toList());

        return new Model.Simple(new ModelPart(cuboids, new HashMap<>()), RenderTypes::entitySolid);
    }

    public static void submitBanner(MaterialSet materials, PoseStack poseStack, SubmitNodeCollector collector, Model.Simple model, int light, int overlay, Direction direction, DyeColor dyeColor, BannerPatternLayers patternLayers, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress, int outlineColor) {
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
        BannerRenderer.submitPatterns(materials, poseStack, collector, light, overlay, model, Unit.INSTANCE, ModelBakery.BANNER_BASE, true, dyeColor, patternLayers, false, breakProgress, outlineColor);

        poseStack.popPose();
    }
}
