package com.phantomwing.choppersdelight.gametest;

import com.phantomwing.choppersdelight.itemGroup.ModItemGroups;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
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
    /** The game-test server, kept by {@link GameTestRegistration} as it starts: fuel is the server's from 1.21.2. */
    static MinecraftServer server;

    private TestCompat() {
    }

    /**
     * Registers the mod's creative tab on the game-test server, from the test mod's initializer, before the registries
     * freeze. The mod registers it from its client entrypoint, which a server doesn't run; on 1.21.1 Every Compat's
     * module, which this line's dev runs don't have, registered it there too.
     */
    static void registerModTab() {
        ModItemGroups.registerModItemGroups();
    }

    /** The mod's creative tab. */
    public static CreativeModeTab modTab() {
        return ModItemGroups.MOD_TAB.get();
    }

    /** Builds a creative tab's contents with the server's registries, as a server-side mod would. */
    public static void buildContents(CreativeModeTab tab, ServerLevel level) {
        tab.buildContents(new CreativeModeTab.ItemDisplayParameters(level.enabledFeatures(), true, level.registryAccess()));
    }

    /** An item by its id, `namespace:path`. {@code Registry#get} gives an Optional holder from 1.21.2, and ResourceLocation is Identifier from 1.21.11. */
    public static Item item(String id) {
        return BuiltInRegistries.ITEM.getValue(Identifier.parse(id));
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

    /** The level's RecipeManager is the server's from 1.21.2; a level only has a RecipeAccess. */
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
     * The translation key the game names a stack by. From 1.21.2 a name is the item name component, which
     * {@code Item#getName(ItemStack)} gives, rather than a description id a stack can override.
     */
    public static String nameKey(ItemStack stack) {
        Component name = stack.getHoverName();
        return name.getContents() instanceof TranslatableContents translatable ? translatable.getKey() : name.getString();
    }

    /** How many ticks a stack burns in a furnace, 0 where it isn't fuel. From 1.21.2 fuel is the server's FuelValues. */
    public static int burnTime(ItemStack stack) {
        return server.fuelValues().burnDuration(stack);
    }

    /** What a block drops when broken by hand, by its loot table. */
    public static List<ItemStack> drops(ServerLevel level, BlockPos pos) {
        return Block.getDrops(level.getBlockState(pos), level, pos, level.getBlockEntity(pos));
    }

    /**
     * The stack picking a block with the middle button gives. From 1.21.4 the block's own is protected and takes
     * whether to keep its data, which a plain pick doesn't.
     */
    public static ItemStack pickBlock(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).getCloneItemStack(level, pos, false);
    }

    /** The comparator signal a block gives. From 1.21.9 it takes the side a comparator reads it from. */
    public static int analogSignal(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).getAnalogOutputSignal(level, pos, Direction.NORTH);
    }

    /** Fails the test. {@code GameTestHelper.fail} takes only a Component from 1.21.5 to 1.21.8; Component works on every version since. */
    public static void fail(GameTestHelper helper, String message) {
        helper.fail(Component.literal(message));
    }

    /** Fails the test unless {@code condition} holds. {@code GameTestHelper.assertTrue} takes only a Component from 1.21.5 to 1.21.10; Component works on every version since. */
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

    /**
     * Whether a mock player can be made yet. On 1.21.6 to 1.21.8 placing one waits for the entities around the world
     * spawn, so the tests wait for them first. From 1.21.9 placing a player waits for nothing (a login waits in
     * PrepareSpawnTask, which a mock player skips), and nothing keeps the spawn area loaded, so waiting for it could only
     * time out.
     */
    public static boolean entitiesLoadedAroundSpawn(ServerLevel level, int radius) {
        return true;
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
        player.gameMode.useItemOn(player, player.level(), player.getMainHandItem(), InteractionHand.MAIN_HAND,
                new BlockHitResult(Vec3.atCenterOf(pos).relative(Direction.UP, 0.5), Direction.UP, pos, false));
    }
}
