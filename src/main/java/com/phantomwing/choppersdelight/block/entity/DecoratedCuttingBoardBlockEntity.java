package com.phantomwing.choppersdelight.block.entity;

import com.phantomwing.choppersdelight.block.ModBlockEntityTypes;
import com.phantomwing.choppersdelight.block.custom.DecoratedCuttingBoardBlock;
import com.phantomwing.choppersdelight.component.DecoratedCuttingBoardData;
import com.phantomwing.choppersdelight.component.ModDataComponents;
import com.phantomwing.choppersdelight.item.ModItems;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipeInput;
import vectorwing.farmersdelight.common.registry.ModAdvancements;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;
import vectorwing.farmersdelight.refabricated.inventory.ItemStackHandler;
import vectorwing.farmersdelight.refabricated.inventory.RecipeWrapper;

import java.util.List;
import java.util.Optional;

public class DecoratedCuttingBoardBlockEntity extends SyncedBlockEntity implements Clearable
{
    private final ItemStackHandler inventory;
    private final RecipeManager.CachedCheck<CuttingBoardRecipeInput, CuttingBoardRecipe> quickCheck;
    private boolean isItemCarvingBoard;

    // Decoration
    private ItemStack cuttingBoard = ItemStack.EMPTY;
    private ItemStack banner = ItemStack.EMPTY;

    // Transient flag set by the block when a creative-mode player breaks it, so onRemove can skip dropping the decorated item.
    private boolean destroyedByCreativePlayer = false;

    public DecoratedCuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.DECORATED_CUTTING_BOARD.get(), pos, state);

        inventory = createHandler();
        isItemCarvingBoard = false;
        quickCheck = RecipeManager.createCheck(ModRecipeTypes.CUTTING.get());

    }

    public static void init() {
        ItemStorage.SIDED.registerForBlockEntity(DecoratedCuttingBoardBlockEntity::getStorage, ModBlockEntityTypes.DECORATED_CUTTING_BOARD.get());
    }

    @NotNull
    public Storage<ItemVariant> getStorage(@Nullable Direction side) {
        return getInventory();
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag compound, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(compound, provider);

        isItemCarvingBoard = compound.getBoolean("IsItemCarved");
        inventory.deserializeNBT(provider, compound.getCompound("Inventory"));

        this.cuttingBoard = compound.contains("CuttingBoardStack") ?
                ItemStack.parseOptional(provider, compound.getCompound("CuttingBoardStack")) : new ItemStack(vectorwing.farmersdelight.common.registry.ModItems.CUTTING_BOARD.get());
        this.banner = compound.contains("BannerStack") ?
                ItemStack.parseOptional(provider, compound.getCompound("BannerStack")) : new ItemStack(Items.WHITE_BANNER);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(compound, provider);

        compound.put("Inventory", inventory.serializeNBT(provider));
        compound.putBoolean("IsItemCarved", isItemCarvingBoard);

        if (!this.cuttingBoard.isEmpty()) {
            compound.put("CuttingBoardStack", this.cuttingBoard.save(provider, new CompoundTag()));
        }

        if (!this.banner.isEmpty()) {
            compound.put("BannerStack", this.banner.save(provider, new CompoundTag()));
        }
    }

    public boolean processStoredItemUsingTool(ItemStack toolStack, @Nullable Player player) {
        if (level == null) return false;

        if (isItemCarvingBoard) return false;

        Optional<RecipeHolder<CuttingBoardRecipe>> matchingRecipe = getMatchingRecipe(toolStack, player);

        matchingRecipe.ifPresent(recipe -> {
            List<ItemStack> results = recipe.value().rollResults(level.random,
                    EnchantmentHelper.getItemEnchantmentLevel(level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE), toolStack),
                    new RecipeWrapper(inventory));
            for (ItemStack resultStack : results) {
                Direction direction = getBlockState().getValue(DecoratedCuttingBoardBlock.FACING).getCounterClockWise();
                ItemUtils.spawnItemEntity(level, resultStack.copy(),
                        worldPosition.getX() + 0.5 + (direction.getStepX() * 0.2), worldPosition.getY() + 0.2, worldPosition.getZ() + 0.5 + (direction.getStepZ() * 0.2),
                        direction.getStepX() * 0.2F, 0.0F, direction.getStepZ() * 0.2F);
            }
            if (!level.isClientSide) {
                toolStack.hurtAndBreak(1, (ServerLevel) level, (ServerPlayer) player, (item) -> {
                });
                if (player != null) {
                    player.awardStat(Stats.ITEM_USED.get(toolStack.getItem()));
                }
            }
            if (level instanceof ServerLevel serverLevel) {
                spawnCuttingParticles(serverLevel, getBlockPos(), getStoredItem());
            }
            playProcessingSound(recipe.value().getSoundEvent().orElse(null), toolStack, getStoredItem());
            inventory.extractItem(0, 1, false);
            if (player instanceof ServerPlayer) {
                ModAdvancements.USE_CUTTING_BOARD.get().trigger((ServerPlayer) player);
                if (!getStoredItem().isEmpty()) {
                    player.displayClientMessage(TextUtils.block("cutting_board.remaining_items", getStoredItem().getCount()), true);
                } else {
                    player.displayClientMessage(Component.empty(), true);
                }
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
                player.displayClientMessage(TextUtils.block("cutting_board.invalid_item"), true);
            }
        } else if (player != null) {
            player.displayClientMessage(TextUtils.block("cutting_board.invalid_tool"), true);
        }

        return Optional.empty();
    }

    public void spawnCuttingParticles(ServerLevel level, BlockPos pos, ItemStack stack) {
        level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, 5, 0.1, 0.1, 0.1, 0.05D);
    }

    public void playProcessingSound(@Nullable SoundEvent sound, ItemStack tool, ItemStack boardItem) {
        if (sound != null) {
            playSound(sound, 1.0F, 1.0F);
        } else if (tool.is(ConventionalItemTags.SHEAR_TOOLS)) {
            playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
        } else if (tool.is(CommonTags.Items.TOOLS_KNIFE)) {
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

    public boolean canAddItem(ItemStack addedStack) {
        if (isItemCarvingBoard || addedStack.isEmpty()) {
            return false;
        }
        return inventory.insertItem(0, addedStack.copy(), true).getCount() != addedStack.getCount();
    }

    public ItemStack addItem(ItemStack addedStack) {
        if (!isItemCarvingBoard) {
            return inventory.insertItem(0, addedStack.copy(), false);
        }
        return addedStack;
    }

    public ItemStack removeItem() {
        isItemCarvingBoard = false;
        return inventory.extractItem(0, getMaxStackSize(), false);
    }

    public boolean carveToolOnBoard(ItemStack toolStack) {
        if (toolStack.getItem() instanceof TieredItem || toolStack.getItem() instanceof TridentItem || toolStack.getItem() instanceof ShearsItem) {
            if (addItem(toolStack) == ItemStack.EMPTY) {
                isItemCarvingBoard = true;
                return true;
            }
        }
        return false;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ItemStack getStoredItem() {
        return inventory.getStackInSlot(0);
    }

    public int getMaxStackSize() {
        return inventory.getSlotLimit(0);
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
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }
        };
    }

    @Override
    public void clearContent() {
        ItemUtils.clearItems(inventory);
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

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
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

    public boolean isDestroyedByCreativePlayer() {
        return this.destroyedByCreativePlayer;
    }

    public void setDestroyedByCreativePlayer(boolean destroyedByCreativePlayer) {
        this.destroyedByCreativePlayer = destroyedByCreativePlayer;
    }
}
