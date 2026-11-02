package com.phantomwing.choppersdelight.gametest;

import com.phantomwing.choppersdelight.itemGroup.ModItemGroups;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
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
    /** The server the tests run on, for what only a server knows from 1.21.2 on. */
    private static MinecraftServer server;

    private TestCompat() {
    }

    /** Keeps the server once it starts. The test mod's initializer calls it, before any server is made. */
    public static void watchServer() {
        ServerLifecycleEvents.SERVER_STARTING.register(starting -> server = starting);
    }

    /** The mod's creative tab. */
    public static CreativeModeTab modTab() {
        return ModItemGroups.MOD_TAB.get();
    }

    /** Builds a creative tab's contents with the server's registries, as a server-side mod would. */
    public static void buildContents(CreativeModeTab tab, ServerLevel level) {
        tab.buildContents(new CreativeModeTab.ItemDisplayParameters(level.enabledFeatures(), true, level.registryAccess()));
    }

    /** An item by its id, `namespace:path`. Registry lookups were renamed in 1.21.2. */
    public static Item item(String id) {
        return BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(id));
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

    /** From 1.21.2 the recipes are the server's alone, a level only having what its client is told of them. */
    private static Optional<RecipeHolder<CraftingRecipe>> recipeFor(ServerLevel level, CraftingInput input) {
        return level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, level);
    }

    /** A banner of this colour with one pattern on it. Registry lookups were renamed in 1.21.2. */
    public static ItemStack patternedBanner(ServerLevel level, Item banner, ResourceKey<BannerPattern> pattern, DyeColor color) {
        ItemStack stack = new ItemStack(banner);
        stack.set(DataComponents.BANNER_PATTERNS, new BannerPatternLayers.Builder()
                .add(level.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN).getOrThrow(pattern), color)
                .build());
        return stack;
    }

    /**
     * The translation key the game names a stack by. From 1.21.2 a stack has no description id of its own: its name is
     * its item name component or what its item's getName makes of it, so the key is read off that name.
     */
    public static String nameKey(ItemStack stack) {
        Component name = stack.getHoverName();
        return name.getContents() instanceof TranslatableContents translatable ? translatable.getKey() : name.getString();
    }

    /**
     * How many ticks a stack burns in a furnace, 0 where it isn't fuel. From 1.21.2 the fuel values are the server's,
     * built with its registries, and the game-test server isn't Fabric's game instance, so it is the one kept here.
     */
    public static int burnTime(ItemStack stack) {
        return server.fuelValues().burnDuration(stack);
    }

    /** What a block drops when broken by hand, by its loot table. */
    public static List<ItemStack> drops(ServerLevel level, BlockPos pos) {
        return Block.getDrops(level.getBlockState(pos), level, pos, level.getBlockEntity(pos));
    }

    /** The stack picking a block with the middle button gives, without Ctrl's data, which it takes from 1.21.4. */
    public static ItemStack pickBlock(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).getCloneItemStack(level, pos, false);
    }

    /** The comparator signal a block gives. */
    public static int analogSignal(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).getAnalogOutputSignal(level, pos);
    }

    /** Fails the test. {@code GameTestHelper.fail} takes only a Component from 1.21.5. */
    public static void fail(GameTestHelper helper, String message) {
        helper.fail(Component.literal(message));
    }

    /** Fails the test unless {@code condition} holds. {@code GameTestHelper.assertTrue} takes only a Component from 1.21.5. */
    public static void assertTrue(GameTestHelper helper, boolean condition, String message) {
        helper.assertTrue(condition, Component.literal(message));
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
