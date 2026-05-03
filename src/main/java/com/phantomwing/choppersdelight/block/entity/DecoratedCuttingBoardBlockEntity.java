package com.phantomwing.choppersdelight.block.entity;

import com.phantomwing.choppersdelight.block.ModBlockEntityTypes;
import com.phantomwing.choppersdelight.block.custom.DecoratedCuttingBoardBlock;
import com.phantomwing.choppersdelight.component.ModDataComponents;
import com.phantomwing.choppersdelight.item.ModItems;
import com.phantomwing.choppersdelight.item.custom.DecoratedCuttingBoardItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;
import vectorwing.farmersdelight.common.registry.ModAdvancements;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class DecoratedCuttingBoardBlockEntity extends SyncedBlockEntity implements Clearable
{
    private final ItemStackHandler inventory;
    private final LazyOptional<IItemHandler> inputHandler;
    private ResourceLocation lastRecipeID;
    private boolean isItemCarvingBoard;

    // Decoration
    private ItemStack cuttingBoard = ItemStack.EMPTY;
    private ItemStack banner = ItemStack.EMPTY;

    public DecoratedCuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.DECORATED_CUTTING_BOARD.get(), pos, state);

        inventory = createHandler();
        inputHandler = LazyOptional.of(() -> inventory);
        isItemCarvingBoard = false;
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);

        isItemCarvingBoard = compound.getBoolean("IsItemCarved");
        inventory.deserializeNBT(compound.getCompound("Inventory"));

        this.cuttingBoard = compound.contains("CuttingBoardStack") ?
                ItemStack.of(compound.getCompound("CuttingBoardStack")) : new ItemStack(vectorwing.farmersdelight.common.registry.ModItems.CUTTING_BOARD.get());
        this.banner = compound.contains("BannerStack") ?
                ItemStack.of(compound.getCompound("BannerStack")) : new ItemStack(Items.WHITE_BANNER);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);

        compound.put("Inventory", inventory.serializeNBT());
        compound.putBoolean("IsItemCarved", isItemCarvingBoard);

        if (!this.cuttingBoard.isEmpty()) {
            compound.put("CuttingBoardStack", this.cuttingBoard.save(new CompoundTag()));
        }

        if (!this.banner.isEmpty()) {
            compound.put("BannerStack", this.banner.save(new CompoundTag()));
        }
    }

    public boolean processStoredItemUsingTool(ItemStack toolStack, @Nullable Player player) {
        if (level == null) return false;

        if (isItemCarvingBoard) return false;

        Optional<CuttingBoardRecipe> matchingRecipe = getMatchingRecipe(new RecipeWrapper(inventory), toolStack, player);

        matchingRecipe.ifPresent(recipe -> {
            List<ItemStack> results = recipe.rollResults(level.random,
                    EnchantmentHelper.getTagEnchantmentLevel(Enchantments.BLOCK_FORTUNE, toolStack),
                    new RecipeWrapper(inventory));
            for (ItemStack resultStack : results) {
                Direction direction = getBlockState().getValue(DecoratedCuttingBoardBlock.FACING).getCounterClockWise();
                ItemUtils.spawnItemEntity(level, resultStack.copy(),
                        worldPosition.getX() + 0.5 + (direction.getStepX() * 0.2), worldPosition.getY() + 0.2, worldPosition.getZ() + 0.5 + (direction.getStepZ() * 0.2),
                        direction.getStepX() * 0.2F, 0.0F, direction.getStepZ() * 0.2F);
            }
            if (player != null) {
                toolStack.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                player.awardStat(Stats.ITEM_USED.get(toolStack.getItem()));
            } else {
                if (toolStack.hurt(1, level.random, null)) {
                    toolStack.setCount(0);
                }
            }
            if (level instanceof ServerLevel serverLevel) {
                spawnCuttingParticles(serverLevel, getBlockPos(), getStoredItem());
            }
            playProcessingSound(recipe.getSoundEventID(), toolStack, getStoredItem());
            inventory.extractItem(0, 1, false);
            if (player instanceof ServerPlayer) {
                ModAdvancements.CUTTING_BOARD.trigger((ServerPlayer) player);
                if (!getStoredItem().isEmpty()) {
                    player.displayClientMessage(TextUtils.block("cutting_board.remaining_items", getStoredItem().getCount()), true);
                } else {
                    player.displayClientMessage(Component.empty(), true);
                }
            }
        });

        return matchingRecipe.isPresent();
    }

    private Optional<CuttingBoardRecipe> getMatchingRecipe(RecipeWrapper recipeWrapper, ItemStack toolStack, @Nullable Player player) {
        if (level == null) return Optional.empty();

        if (lastRecipeID != null) {
            Recipe<RecipeWrapper> recipe = ((RecipeManagerAccessor) level.getRecipeManager())
                    .getRecipeMap(ModRecipeTypes.CUTTING.get())
                    .get(lastRecipeID);
            if (recipe instanceof CuttingBoardRecipe && recipe.matches(recipeWrapper, level) && ((CuttingBoardRecipe) recipe).getTool().test(toolStack)) {
                return Optional.of((CuttingBoardRecipe) recipe);
            }
        }

        List<CuttingBoardRecipe> recipeList = level.getRecipeManager().getRecipesFor(ModRecipeTypes.CUTTING.get(), recipeWrapper, level);
        if (recipeList.isEmpty()) {
            if (player != null)
                player.displayClientMessage(TextUtils.block("cutting_board.invalid_item"), true);
            return Optional.empty();
        }

        Optional<CuttingBoardRecipe> recipe = recipeList.stream().filter(cuttingRecipe -> cuttingRecipe.getTool().test(toolStack)).findFirst();
        if (recipe.isEmpty()) {
            if (player != null)
                player.displayClientMessage(TextUtils.block("cutting_board.invalid_tool"), true);
            return Optional.empty();
        }

        lastRecipeID = recipe.get().getId();
        return recipe;
    }

    public void spawnCuttingParticles(ServerLevel level, BlockPos pos, ItemStack stack) {
        level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, 5, 0.1, 0.1, 0.1, 0.05D);
    }

    public void playProcessingSound(String soundEventID, ItemStack tool, ItemStack boardItem) {
        SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundEventID));

        if (sound != null) {
            playSound(sound, 1.0F, 1.0F);
        } else if (tool.is(Tags.Items.SHEARS)) {
            playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
        } else if (tool.is(CommonTags.Items.TOOLS_KNIVES)) {
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

    public IItemHandler getInventory() {
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
    @Nonnull
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap.equals(ForgeCapabilities.ITEM_HANDLER)) {
            return inputHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        inputHandler.invalidate();
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
        this.cuttingBoard = DecoratedCuttingBoardItem.getCuttingBoardStack(stack);
        this.banner = DecoratedCuttingBoardItem.getBannerStack(stack);
    }

    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(ModItems.DECORATED_CUTTING_BOARD.get());
        CompoundTag decoratedData = itemStack.getOrCreateTagElement(ModDataComponents.DECORATED_CUTTING_BOARD_DATA);
        decoratedData.put(ModDataComponents.DECORATED_CUTTING_BOARD_CUTTING_BOARD_DATA, this.cuttingBoard.save(new CompoundTag()));
        decoratedData.put(ModDataComponents.DECORATED_CUTTING_BOARD_BANNER_DATA, this.banner.save(new CompoundTag()));

        return itemStack;
    }

    public ItemStack getCuttingBoard() {
        return this.cuttingBoard;
    }

    public ItemStack getBanner() {
        return this.banner;
    }
}
