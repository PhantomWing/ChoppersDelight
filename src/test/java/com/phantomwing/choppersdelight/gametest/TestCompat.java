package com.phantomwing.choppersdelight.gametest;

import com.phantomwing.choppersdelight.itemGroup.ModItemGroups;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;

import java.util.List;
import java.util.Optional;

/**
 * The calls a test makes whose shape differs between Minecraft versions or modloaders, behind one signature, so
 * the test bodies copy across lines unchanged.
 */
public final class TestCompat {
    private TestCompat() {
    }

    /** The mod's creative tab. */
    public static CreativeModeTab modTab() {
        return ModItemGroups.MOD_TAB.get();
    }

    /** Builds a creative tab's contents with the server's registries, as a server-side mod would. */
    public static void buildContents(CreativeModeTab tab, ServerLevel level) {
        tab.buildContents(new CreativeModeTab.ItemDisplayParameters(level.enabledFeatures(), true, level.registryAccess()));
    }

    /** An item by its id, `namespace:path`. */
    public static Item item(String id) {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
    }

    /** What a crafting grid holding these stacks in one row makes, or empty when no recipe matches. */
    public static ItemStack craft(ServerLevel level, ItemStack... stacks) {
        return craftGrid(level, stacks.length, 1, stacks);
    }

    /** What a crafting grid this wide and tall makes, its stacks given row by row, or empty when no recipe matches. */
    public static ItemStack craftGrid(ServerLevel level, int width, int height, ItemStack... stacks) {
        CraftingInput input = CraftingInput.of(width, height, List.of(stacks));
        return recipeFor(level, input)
                .map(recipe -> recipe.value().assemble(input, level.registryAccess()))
                .orElse(ItemStack.EMPTY);
    }

    /** What a crafting grid holding these stacks in one row leaves in it once crafted, the empty slots included. */
    public static List<ItemStack> craftingRemainder(ServerLevel level, ItemStack... stacks) {
        CraftingInput input = CraftingInput.of(stacks.length, 1, List.of(stacks));
        return recipeFor(level, input)
                .map(recipe -> List.copyOf(recipe.value().getRemainingItems(input)))
                .orElse(List.of());
    }

    private static Optional<RecipeHolder<CraftingRecipe>> recipeFor(ServerLevel level, CraftingInput input) {
        return level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level);
    }

    /** A banner of this colour with one pattern on it. */
    public static ItemStack patternedBanner(ServerLevel level, Item banner, ResourceKey<BannerPattern> pattern, DyeColor color) {
        ItemStack stack = new ItemStack(banner);
        stack.set(DataComponents.BANNER_PATTERNS, new BannerPatternLayers.Builder()
                .add(level.registryAccess().registryOrThrow(Registries.BANNER_PATTERN).getHolderOrThrow(pattern), color)
                .build());
        return stack;
    }

    /** The translation key the game names a stack by. */
    public static String nameKey(ItemStack stack) {
        return stack.getDescriptionId();
    }

    /** How many ticks a stack burns in a furnace, 0 where it isn't fuel. */
    public static int burnTime(ItemStack stack) {
        Integer ticks = FuelRegistry.INSTANCE.get(stack.getItem());
        return ticks == null ? 0 : ticks;
    }

    /** What a block drops when broken by hand, by its loot table. */
    public static List<ItemStack> drops(ServerLevel level, BlockPos pos) {
        return Block.getDrops(level.getBlockState(pos), level, pos, level.getBlockEntity(pos));
    }

    /** The stack picking a block with the middle button gives. */
    public static ItemStack pickBlock(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock().getCloneItemStack(level, pos, state);
    }

    /** The comparator signal a block gives. */
    public static int analogSignal(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).getAnalogOutputSignal(level, pos);
    }

    /** Fails the test. {@code GameTestHelper.fail} takes only a Component from 1.21.5. */
    public static void fail(GameTestHelper helper, String message) {
        helper.fail(message);
    }

    /** Fails the test unless {@code condition} holds. {@code GameTestHelper.assertTrue} takes only a Component from 1.21.5. */
    public static void assertTrue(GameTestHelper helper, boolean condition, String message) {
        helper.assertTrue(condition, message);
    }

    /** Whether two stacks are the same item with the same data, as stacking asks. */
    public static boolean sameItemAndData(ItemStack first, ItemStack second) {
        return ItemStack.isSameItemSameComponents(first, second);
    }

    /** Names a stack as an anvil does. */
    public static void rename(ItemStack stack, String name) {
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(name));
    }

    /** A player on the server, in the test's level, who can click blocks. */
    public static ServerPlayer mockPlayer(GameTestHelper helper) {
        return helper.makeMockServerPlayerInLevel();
    }

    /** Whether the entities of the chunks around the world spawn have loaded, which a mock player waits for. */
    public static boolean entitiesLoadedAroundSpawn(ServerLevel level, int radius) {
        return ChunkPos.rangeClosed(new ChunkPos(level.getSharedSpawnPos()), radius)
                .allMatch(chunk -> level.areEntitiesLoaded(chunk.toLong()));
    }

    /** A plain cutting board's block entity, as the tests use it. Farmer's Delight's own on these lines. */
    public interface Board {
        void add(ItemStack stack);

        /** Cuts what the board holds with a tool, as a player's click does, answering whether a recipe matched. */
        boolean cut(ItemStack tool);

        boolean isEmpty();

        boolean isCarved();

        ItemStack stored();
    }

    /** The plain cutting board's block entity at a spot, or empty where there is none. */
    public static Optional<Board> boardAt(ServerLevel level, BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity board)) return Optional.empty();
        return Optional.of(new Board() {
            @Override
            public void add(ItemStack stack) {
                board.addItem(stack);
            }

            @Override
            public boolean cut(ItemStack tool) {
                return board.processStoredItemUsingTool(tool, null);
            }

            @Override
            public boolean isEmpty() {
                return board.isEmpty();
            }

            @Override
            public boolean isCarved() {
                return board.isItemCarvingBoard();
            }

            @Override
            public ItemStack stored() {
                return board.getStoredItem();
            }
        });
    }

    /** Right-clicks the top of a block with what the player holds, as the game does when a player clicks. */
    public static void useItemOn(ServerPlayer player, BlockPos pos) {
        player.gameMode.useItemOn(player, player.serverLevel(), player.getMainHandItem(), InteractionHand.MAIN_HAND,
                new BlockHitResult(Vec3.atCenterOf(pos).relative(Direction.UP, 0.5), Direction.UP, pos, false));
    }
}
