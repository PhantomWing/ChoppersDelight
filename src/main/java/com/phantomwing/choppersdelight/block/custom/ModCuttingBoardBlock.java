package com.phantomwing.choppersdelight.block.custom;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.block.ModBlockEntityTypes;
import com.phantomwing.choppersdelight.block.entity.ModCuttingBoardBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.registry.ModSounds;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class ModCuttingBoardBlock extends CuttingBoardBlock
{
    public ModCuttingBoardBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return ModBlockEntityTypes.MOD_CUTTING_BOARD.get().create(pos, state);
    }

    /**
     * Mirror of FD 1.3.1 CuttingBoardBlock#use, but matching against {@link ModCuttingBoardBlockEntity}.
     */
    @Override
    public @Nonnull InteractionResult use(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof ModCuttingBoardBlockEntity cuttingBoard)) {
            return InteractionResult.PASS;
        }

        ItemStack mainHandStack = player.getMainHandItem();

        if (mainHandStack.isEmpty()) {
            if (cuttingBoard.isEmpty() || level.isClientSide) {
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
            if (level.isClientSide) {
                return InteractionResult.CONSUME;
            }
            ItemStack remainderStack = cuttingBoard.addItem(player.getAbilities().instabuild ? mainHandStack.copy() : mainHandStack);
            if (!player.isCreative()) {
                player.setItemSlot(EquipmentSlot.MAINHAND, remainderStack);
            }
            Vec3 centerPos = pos.getCenter();
            level.playSound(null, centerPos.x(), centerPos.y(), centerPos.z(), ModSounds.BLOCK_CUTTING_BOARD_PLACE.get(), SoundSource.BLOCKS, 1.0F, 0.8F);
            return InteractionResult.SUCCESS;
        } else {
            if (cuttingBoard.processStoredItemUsingTool(mainHandStack, player)) {
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.is(newState.getBlock())) {
            return;
        }

        if (level.getBlockEntity(pos) instanceof ModCuttingBoardBlockEntity board) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), board.getStoredItem());
            level.updateNeighbourForOutputSignal(pos, this);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public int getAnalogOutputSignal(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof ModCuttingBoardBlockEntity cuttingBoard)) {
            return 0;
        }
        ItemStack storedStack = cuttingBoard.getStoredItem();
        if (!storedStack.isEmpty()) {
            float proportions = (float) storedStack.getCount() / Math.min(cuttingBoard.getMaxStackSize(), storedStack.getMaxStackSize());
            return Mth.floor(proportions * 14.0F) + 1;
        }
        return 0;
    }

    /**
     * Mirror of FD 1.3.1's CuttingBoardBlock.ToolCarvingEvent for our wood-variant boards.
     * FD's own handler only matches CuttingBoardBlockEntity, so we need our own to handle ModCuttingBoardBlockEntity.
     */
    @Mod.EventBusSubscriber(modid = ChoppersDelight.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ToolCarvingEvent
    {
        @SubscribeEvent
        @SuppressWarnings("unused")
        public static void onSneakPlaceTool(PlayerInteractEvent.RightClickBlock event) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();

            if (!(level.getBlockEntity(pos) instanceof ModCuttingBoardBlockEntity cuttingBoard)) {
                return;
            }

            Player player = event.getEntity();
            ItemStack heldStack = player.getMainHandItem();

            if (!player.isSecondaryUseActive() || heldStack.isEmpty()) {
                return;
            }

            if (!(heldStack.getItem() instanceof TieredItem
                    || heldStack.getItem() instanceof TridentItem
                    || heldStack.getItem() instanceof ShearsItem)) {
                return;
            }

            if (cuttingBoard.carveToolOnBoard(player.getAbilities().instabuild ? heldStack.copy() : heldStack)) {
                if (!player.isCreative()) {
                    player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                }
                Vec3 centerPos = pos.getCenter();
                level.playSound(null, centerPos.x(), centerPos.y(), centerPos.z(), ModSounds.BLOCK_CUTTING_BOARD_CARVE.get(), SoundSource.BLOCKS, 1.0F, 0.8F);
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }
}
