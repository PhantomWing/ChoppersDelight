package com.phantomwing.choppersdelight.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.item.custom.DecoratedCuttingBoardItem;
import com.phantomwing.choppersdelight.utils.BannerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModItems;

/**
 * The decorated cutting board's item: the board it was made from, with the banner laid on it.
 * Named by {@code {"type": "choppersdelight:decorated_cutting_board"}} in its items/ definition, whose base model
 * gives the display transforms.
 */
public class DecoratedCuttingBoardItemRenderer implements SpecialModelRenderer<ItemStack> {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ChoppersDelight.MOD_ID, "decorated_cutting_board");

    private final ModelPart bannerModelPart;
    private final ItemStackRenderState boardRenderState = new ItemStackRenderState();

    public DecoratedCuttingBoardItemRenderer() {
        this.bannerModelPart = BannerUtils.initializeBannerModelPart();
    }

    public static void register() {
        SpecialModelRenderers.ID_MAPPER.put(ID, Unbaked.MAP_CODEC);
    }

    @Override
    public @Nullable ItemStack extractArgument(@NotNull ItemStack stack) {
        return stack;
    }

    @Override
    public void render(@Nullable ItemStack stack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int light, int overlay, boolean hasFoil) {
        if (stack == null) {
            return;
        }

        // Initialize item.
        ItemStack cuttingBoardItem = DecoratedCuttingBoardItem.getCuttingBoardStack(stack);

        // Apply fallback if needed.
        if (cuttingBoardItem.isEmpty())
        {
            cuttingBoardItem = new ItemStack(ModItems.CUTTING_BOARD.get());
        }

        // The base model's display transforms are already applied, so the board is drawn without its own.
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        Minecraft.getInstance().getItemModelResolver().updateForTopItem(boardRenderState, cuttingBoardItem, ItemDisplayContext.NONE, null, null, 0);
        boardRenderState.render(poseStack, buffer, light, overlay);
        poseStack.popPose();

        // Render banner
        ItemStack bannerItemStack = DecoratedCuttingBoardItem.getBannerStack(stack);

        // Apply fallback if needed.
        if (bannerItemStack.isEmpty())
        {
            bannerItemStack = new ItemStack(Items.WHITE_BANNER);
        }

        if (bannerItemStack.getItem() instanceof BannerItem bannerItem) {
            BannerUtils.renderBanner(poseStack, buffer, bannerModelPart, light, overlay, Direction.SOUTH, bannerItem.getColor(), bannerItemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY));
        }
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<?> bake(@NotNull EntityModelSet entityModels) {
            return new DecoratedCuttingBoardItemRenderer();
        }

        @Override
        public @NotNull MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
