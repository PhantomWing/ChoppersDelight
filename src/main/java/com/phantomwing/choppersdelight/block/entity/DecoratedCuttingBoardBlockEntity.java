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
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipeInput;
import vectorwing.farmersdelight.common.registry.ModAdvancements;
import vectorwing.farmersdelight.common.registry.ModRecipePropertySets;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;
import vectorwing.farmersdelight.refabricated.inventory.ItemStackHandler;

import java.util.List;
import java.util.Optional;

/**
 * Mirror of FD Refabricated 3.4.2's CuttingBoardBlockEntity, which holds one item at a time, plus the decoration.
 */
public class DecoratedCuttingBoardBlockEntity extends SyncedBlockEntity
{
    private final ItemStackHandler inventory;
    private final RecipeManager.CachedCheck<CuttingBoardRecipeInput, CuttingBoardRecipe> quickCheck;
    private boolean isItemCarvingBoard;

    // Decoration
    private ItemStack cuttingBoard = ItemStack.EMPTY;
    private ItemStack banner = ItemStack.EMPTY;

    // Transient flag set by the block when a creative-mode player breaks it, so the removal can skip dropping the decorated item.
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
    public void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        isItemCarvingBoard = input.getBooleanOr("IsItemCarved", false);
        inventory.deserialize(input.childOrEmpty("Inventory"));

        this.cuttingBoard = input.read("CuttingBoardStack", ItemStack.CODEC)
                .orElseGet(() -> new ItemStack(vectorwing.farmersdelight.common.registry.ModItems.CUTTING_BOARD.get()));
        this.banner = input.read("BannerStack", ItemStack.CODEC)
                .orElseGet(() -> new ItemStack(Items.WHITE_BANNER));
    }

    @Override
    public void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        inventory.serialize(output.child("Inventory"));
        output.putBoolean("IsItemCarved", isItemCarvingBoard);

        if (!this.cuttingBoard.isEmpty()) {
            output.store("CuttingBoardStack", ItemStack.CODEC, this.cuttingBoard);
        }

        if (!this.banner.isEmpty()) {
            output.store("BannerStack", ItemStack.CODEC, this.banner);
        }
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        if (level != null) {
            // Items on the board always drop, even in creative, as Farmer's Delight's own board does.
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), getStoredItem());
            // The decorated cutting board keeps its decoration, unless a creative player broke it.
            if (!destroyedByCreativePlayer) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), getItem());
            }
        }

        super.preRemoveSideEffects(pos, state);
    }

    public boolean processStoredItemUsingTool(ItemStack toolStack, @Nullable Player player) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return player != null
                    && player.level().recipeAccess().propertySet(ModRecipePropertySets.CUTTING_BOARD_INPUT).test(getStoredItem())
                    && player.level().recipeAccess().propertySet(ModRecipePropertySets.CUTTING_BOARD_TOOL).test(toolStack);
        }

        if (isItemCarvingBoard) return false;

        Optional<RecipeHolder<CuttingBoardRecipe>> matchingRecipe = getMatchingRecipe(toolStack, player, serverLevel);

        matchingRecipe.ifPresent(recipe -> {
            List<ItemStack> results = recipe.value().rollResults(level.random,
                    EnchantmentHelper.getItemEnchantmentLevel(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), toolStack));
            for (ItemStack resultStack : results) {
                Direction direction = getBlockState().getValue(DecoratedCuttingBoardBlock.FACING).getCounterClockWise();
                ItemUtils.spawnItemEntity(level, resultStack.copy(),
                        worldPosition.getX() + 0.5 + (direction.getStepX() * 0.2), worldPosition.getY() + 0.2, worldPosition.getZ() + 0.5 + (direction.getStepZ() * 0.2),
                        direction.getStepX() * 0.2F, 0.0F, direction.getStepZ() * 0.2F);
            }
            if (player instanceof ServerPlayer serverPlayer) {
                toolStack.hurtAndBreak(1, serverLevel, serverPlayer, (item) -> {
                });
            }

            playProcessingSound(recipe.value().getSoundEvent().orElse(null), toolStack, getStoredItem());
            removeItem();
            if (player instanceof ServerPlayer serverPlayer) {
                ModAdvancements.USE_CUTTING_BOARD.get().trigger(serverPlayer);
            }
        });

        return matchingRecipe.isPresent();
    }

    private Optional<RecipeHolder<CuttingBoardRecipe>> getMatchingRecipe(ItemStack toolStack, @Nullable Player player, ServerLevel serverLevel) {
        Optional<RecipeHolder<CuttingBoardRecipe>> recipe = quickCheck.getRecipeFor(new CuttingBoardRecipeInput(getStoredItem(), toolStack), serverLevel);

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
        } else if (tool.is(ConventionalItemTags.SHEAR_TOOLS)) {
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

    public ItemStackHandler getInventory() {
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

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider provider) {
        return saveCustomOnly(provider);
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
