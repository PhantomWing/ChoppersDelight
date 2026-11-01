package com.phantomwing.choppersdelight.block.custom;

import com.mojang.serialization.MapCodec;
import com.phantomwing.choppersdelight.block.ModBlockEntityTypes;
import com.phantomwing.choppersdelight.block.entity.DecoratedCuttingBoardBlockEntity;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.registry.ModSounds;

public class DecoratedCuttingBoardBlock extends CuttingBoardBlock
{
    public static final MapCodec<DecoratedCuttingBoardBlock> CODEC = simpleCodec(DecoratedCuttingBoardBlock::new);

    public DecoratedCuttingBoardBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return ModBlockEntityTypes.DECORATED_CUTTING_BOARD.get().create(pos, state);
    }

    /**
     * Mirror of FD Refabricated 3.6.16 CuttingBoardBlock#useItemOn, but matching against {@link DecoratedCuttingBoardBlockEntity}.
     */
    @Override
    public @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof DecoratedCuttingBoardBlockEntity cuttingBoard)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        ItemStack mainHandStack = player.getMainHandItem();

        if (mainHandStack.isEmpty()) {
            if (cuttingBoard.isEmpty() || level.isClientSide()) {
                return InteractionResult.CONSUME;
            }
            ItemStack removedStack = cuttingBoard.removeItem();
            if (!player.isCreative()) {
                player.getInventory().add(removedStack);
            }
            Vec3 centerPos = pos.getCenter();
            level.playSound(null, centerPos.x(), centerPos.y(), centerPos.z(), ModSounds.BLOCK_CUTTING_BOARD_REMOVE.get(), SoundSource.BLOCKS, 0.25F, 0.5F);
            return InteractionResult.SUCCESS;
        }
        if (cuttingBoard.canAddItem(mainHandStack)) {
            if (level.isClientSide()) {
                return InteractionResult.CONSUME;
            }
            ItemStack remainderStack = cuttingBoard.addItem(player.getAbilities().instabuild ? mainHandStack.copy() : mainHandStack);
            if (!player.isCreative()) {
                player.setItemSlot(EquipmentSlot.MAINHAND, remainderStack);
            }
            Vec3 centerPos = pos.getCenter();
            level.playSound(null, centerPos.x(), centerPos.y(), centerPos.z(), ModSounds.BLOCK_CUTTING_BOARD_PLACE.get(), SoundSource.BLOCKS, 1.0F, 0.8F);
            return InteractionResult.SUCCESS_SERVER;
        }
        return cuttingBoard.processStoredItemUsingTool(mainHandStack, player) ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
    }

    @Override
    public BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        // Mark creative-mode destruction so the block entity's removal can skip dropping the decorated board item itself.
        if (player.isCreative() && level.getBlockEntity(pos) instanceof DecoratedCuttingBoardBlockEntity decoratedBoard) {
            decoratedBoard.setDestroyedByCreativePlayer(true);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Direction direction) {
        if (!(level.getBlockEntity(pos) instanceof DecoratedCuttingBoardBlockEntity cuttingBoard)) {
            return 0;
        }
        ItemStack storedStack = cuttingBoard.getStoredItem();
        if (!storedStack.isEmpty()) {
            float proportions = (float) storedStack.getCount() / Math.min(cuttingBoard.getMaxStackSize(), storedStack.getMaxStackSize());
            return Mth.floor(proportions * 14.0F) + 1;
        }
        return 0;
    }

    @Override
    protected @NotNull ItemStack getCloneItemStack(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean includeData) {
        BlockEntity be = level.getBlockEntity(pos);
        return be instanceof DecoratedCuttingBoardBlockEntity decoratedBoard
                ? decoratedBoard.getItem()
                : super.getCloneItemStack(level, pos, state, includeData);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof DecoratedCuttingBoardBlockEntity decoratedBoard) {
            decoratedBoard.loadFromItemStack(stack);
        }
    }

    @Override
    protected void spawnDestroyParticles(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state) {
        BlockState particleState = null;

        // Render destroy particles using the underlying cutting board's wood, not the decorated board item.
        if (level.getBlockEntity(pos) instanceof DecoratedCuttingBoardBlockEntity decoratedBoard) {
            ItemStack board = decoratedBoard.getCuttingBoard();
            if (board.getItem() instanceof BlockItem blockItem) {
                particleState = blockItem.getBlock().defaultBlockState();
            }
        }

        // Fallback to oak planks if something goes wrong.
        if (particleState == null) {
            particleState = Blocks.OAK_PLANKS.defaultBlockState();
        }

        level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(particleState));
    }

    /**
     * Mirror of FD Refabricated 3.6.16's CuttingBoardBlock.ToolCarvingEvent for our decorated variant.
     * FD's own handler only matches CuttingBoardBlockEntity, so we register our own to handle DecoratedCuttingBoardBlockEntity.
     * Registered via {@link UseBlockCallback#EVENT} in the mod initializer.
     */
    public static class ToolCarvingEvent
    {
        public static InteractionResult onSneakPlaceTool(Player player, Level level, InteractionHand hand, BlockHitResult hit) {
            if (player.isSpectator())
                return InteractionResult.PASS;

            BlockPos pos = hit.getBlockPos();
            ItemStack heldStack = player.getMainHandItem();

            if (player.isSecondaryUseActive() && !heldStack.isEmpty() && level.getBlockEntity(pos) instanceof DecoratedCuttingBoardBlockEntity cuttingBoard) {
                if (heldStack.has(DataComponents.TOOL) ||
                        heldStack.getItem() instanceof TridentItem ||
                        heldStack.getItem() instanceof ShearsItem) {
                    boolean success = cuttingBoard.carveToolOnBoard(player.getAbilities().instabuild ? heldStack.copy() : heldStack);
                    if (success) {
                        // The board takes a copy of the tool.
                        if (!player.getAbilities().instabuild) {
                            heldStack.shrink(1);
                        }
                        Vec3 centerPos = pos.getCenter();
                        level.playSound(null, centerPos.x(), centerPos.y(), centerPos.z(), SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 0.8F);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.PASS;
        }
    }
}
