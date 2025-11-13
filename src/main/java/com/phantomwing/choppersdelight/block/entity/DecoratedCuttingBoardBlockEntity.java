package com.phantomwing.choppersdelight.block.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.block.ModBlockEntityTypes;
import com.phantomwing.choppersdelight.block.custom.DecoratedCuttingBoardBlock;
import com.phantomwing.choppersdelight.component.DecoratedCuttingBoardData;
import com.phantomwing.choppersdelight.component.ModDataComponents;
import com.phantomwing.choppersdelight.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipeInput;
import vectorwing.farmersdelight.common.registry.ModAdvancements;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = ChoppersDelight.MOD_ID)
public class DecoratedCuttingBoardBlockEntity extends SyncedBlockEntity
{
    private final ItemStackHandler inventory;
    private final RecipeManager.CachedCheck<CuttingBoardRecipeInput, CuttingBoardRecipe> quickCheck;
    private ResourceLocation lastRecipeID;
    private boolean isItemCarvingBoard;

    // Decoration
    private ItemStack cuttingBoard = ItemStack.EMPTY;
    private ItemStack banner = ItemStack.EMPTY;

    public DecoratedCuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.DECORATED_CUTTING_BOARD.get(), pos, state);

        inventory = createHandler();
        isItemCarvingBoard = false;
        quickCheck = RecipeManager.createCheck(ModRecipeTypes.CUTTING.get());
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntityTypes.DECORATED_CUTTING_BOARD.get(),
                (be, context) -> be.getInventory()
        );
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(compoundTag, provider);

        isItemCarvingBoard = compoundTag.getBoolean("IsItemCarved");
        inventory.deserializeNBT(provider, compoundTag.getCompound("Inventory"));

        this.cuttingBoard = compoundTag.contains("CuttingBoardStack") ?
                ItemStack.parseOptional(provider, compoundTag.getCompound("CuttingBoardStack")) : new ItemStack(vectorwing.farmersdelight.common.registry.ModItems.CUTTING_BOARD.get());
        this.banner = compoundTag.contains("BannerStack") ?
                ItemStack.parseOptional(provider, compoundTag.getCompound("BannerStack")) : new ItemStack(Items.WHITE_BANNER);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(compoundTag, provider);

        compoundTag.put("Inventory", inventory.serializeNBT(provider));
        compoundTag.putBoolean("IsItemCarved", isItemCarvingBoard);

        if (!this.cuttingBoard.isEmpty()) {
            compoundTag.put("CuttingBoardStack", this.cuttingBoard.save(provider, new CompoundTag()));
        }

        if (!this.banner.isEmpty()) {
            compoundTag.put("BannerStack", this.banner.save(provider, new CompoundTag()));
        }
    }

    public boolean processStoredItemUsingTool(ItemStack toolStack, @Nullable Player player) {
        if (level == null) return false;

        if (isItemCarvingBoard) return false;

        Optional<RecipeHolder<CuttingBoardRecipe>> matchingRecipe = getMatchingRecipe(toolStack, player);

        matchingRecipe.ifPresent(recipe -> {
            List<ItemStack> results = recipe.value().rollResults(level.random, EnchantmentHelper.getTagEnchantmentLevel(level.holder(Enchantments.FORTUNE).get(), toolStack));
            for (ItemStack resultStack : results) {
                Direction direction = getBlockState().getValue(DecoratedCuttingBoardBlock.FACING).getCounterClockWise();
                ItemUtils.spawnItemEntity(level, resultStack.copy(),
                        worldPosition.getX() + 0.5 + (direction.getStepX() * 0.2), worldPosition.getY() + 0.2, worldPosition.getZ() + 0.5 + (direction.getStepZ() * 0.2),
                        direction.getStepX() * 0.2F, 0.0F, direction.getStepZ() * 0.2F);
            }

            if (!level.isClientSide) {
                toolStack.hurtAndBreak(1, (ServerLevel) level, player, (item) -> {
                });
            }

            playProcessingSound(recipe.value().getSoundEvent().orElse(null), toolStack, getStoredItem());

            removeItem();

            if (player instanceof ServerPlayer) {
                ModAdvancements.USE_CUTTING_BOARD.get().trigger((ServerPlayer) player);
            }
        });

        return matchingRecipe.isPresent();
    }

    private Optional<RecipeHolder<CuttingBoardRecipe>> getMatchingRecipe(ItemStack toolStack, @Nullable Player player) {
        if (level == null) return Optional.empty();

        Optional<RecipeHolder<CuttingBoardRecipe>> recipe = quickCheck.getRecipeFor(new CuttingBoardRecipeInput(getStoredItem(), toolStack), level);

        if (recipe.isPresent()) {
            if (recipe.get().value().getTool().test(toolStack)) {
                return recipe;
            } else if (player != null) {
                player.displayClientMessage(TextUtils.getTranslation("block.cutting_board.invalid_tool"), true);
            }
        } else if (player != null) {
            player.displayClientMessage(TextUtils.getTranslation("block.cutting_board.invalid_item"), true);
        }

        return Optional.empty();
    }

    public void playProcessingSound(@Nullable SoundEvent sound, ItemStack tool, ItemStack boardItem) {
        if (sound != null) {
            playSound(sound, 1.0F, 1.0F);
        } else if (tool.is(Tags.Items.TOOLS_SHEAR)) {
            playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
        } else if (tool.is(CommonTags.TOOLS_KNIFE)) {
            playSound(ModSounds.BLOCK_CUTTING_BOARD_KNIFE.get(), 0.8F, 1.0F);
        } else if (boardItem.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            SoundType soundType = block.defaultBlockState().getSoundType();
            playSound(soundType.getBreakSound(), 1.0F, 0.8F);
        } else {
            playSound(SoundEvents.WOOD_BREAK, 1.0F, 0.8F);
        }
    }

    public void playSound(SoundEvent sound, float volume, float pitch) {
        if (level != null)
            level.playSound(null, worldPosition.getX() + 0.5F, worldPosition.getY() + 0.5F, worldPosition.getZ() + 0.5F, sound, SoundSource.BLOCKS, volume, pitch);
    }

    public boolean addItem(ItemStack itemStack) {
        if (isEmpty() && !itemStack.isEmpty()) {
            inventory.setStackInSlot(0, itemStack.split(1));
            isItemCarvingBoard = false;
            inventoryChanged();
            return true;
        }
        return false;
    }

    public boolean carveToolOnBoard(ItemStack tool) {
        if (addItem(tool)) {
            isItemCarvingBoard = true;
            return true;
        }
        return false;
    }

    public ItemStack removeItem() {
        if (!isEmpty()) {
            isItemCarvingBoard = false;
            ItemStack item = getStoredItem().split(1);
            inventoryChanged();
            return item;
        }
        return ItemStack.EMPTY;
    }

    public IItemHandler getInventory() {
        return inventory;
    }

    public ItemStack getStoredItem() {
        return inventory.getStackInSlot(0);
    }

    public boolean isEmpty() {
        return inventory.getStackInSlot(0).isEmpty();
    }

    public boolean isItemCarvingBoard() {
        return isItemCarvingBoard;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler()
        {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }
        };
    }

    public void loadFromItemStack(ItemStack stack) {
        DecoratedCuttingBoardData data = stack.get(ModDataComponents.DECORATED_CUTTING_BOARD_DATA.get());

        if (data != null) {
            this.cuttingBoard = data.cuttingBoard().copy();
            this.banner = data.banner().copy();
        } else {
            // Set default state (this is how it appears in Creative Mode inventory)
            this.cuttingBoard = new ItemStack(vectorwing.farmersdelight.common.registry.ModItems.CUTTING_BOARD.get());
            this.banner = new ItemStack(Items.WHITE_BANNER);
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Nonnull
    @Override
    public CompoundTag getUpdateTag(@Nonnull HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag, provider);
        return tag;
    }

    public ItemStack getItem() {
        ItemStack itemstack = new ItemStack(ModItems.DECORATED_CUTTING_BOARD.get());
        itemstack.set(ModDataComponents.DECORATED_CUTTING_BOARD_DATA.get(),
                new DecoratedCuttingBoardData(this.cuttingBoard.copy(), this.banner.copy()));
        return itemstack;
    }

    public ItemStack getCuttingBoard() {
        return this.cuttingBoard;
    }

    public ItemStack getBanner() {
        return this.banner;
    }
}
