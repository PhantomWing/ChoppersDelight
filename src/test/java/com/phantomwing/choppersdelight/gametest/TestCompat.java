package com.phantomwing.choppersdelight.gametest;

import com.mojang.authlib.GameProfile;
import com.phantomwing.choppersdelight.block.entity.ModCuttingBoardBlockEntity;
import com.phantomwing.choppersdelight.ui.ModCreativeModeTab;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The calls a test makes whose shape differs between Minecraft versions or modloaders, behind one signature, so
 * the test bodies copy across lines unchanged.
 */
public final class TestCompat {
    /** The white banner. From 26.2 the coloured items are one collection, {@code Items.BANNER.pick(DyeColor.WHITE)}. */
    public static final Item WHITE_BANNER = Items.WHITE_BANNER;

    private TestCompat() {
    }

    /** The mod's creative tab. */
    public static CreativeModeTab modTab() {
        return ModCreativeModeTab.MOD_TAB.get();
    }

    /** Builds a creative tab's contents with the server's registries, as a server-side mod would. */
    public static void buildContents(CreativeModeTab tab, ServerLevel level) {
        tab.buildContents(new CreativeModeTab.ItemDisplayParameters(level.enabledFeatures(), true, level.registryAccess()));
    }

    /** An item by its id, `namespace:path`. */
    public static Item item(String id) {
        return BuiltInRegistries.ITEM.get(new ResourceLocation(id));
    }

    /** What a crafting grid holding these stacks in one row makes, or empty when no recipe matches. */
    public static ItemStack craft(ServerLevel level, ItemStack... stacks) {
        return craftGrid(level, stacks.length, 1, stacks);
    }

    /** What a crafting grid this wide and tall makes, its stacks given row by row, or empty when no recipe matches. */
    public static ItemStack craftGrid(ServerLevel level, int width, int height, ItemStack... stacks) {
        CraftingContainer grid = grid(width, height, stacks);
        return recipeFor(level, grid)
                .map(recipe -> recipe.assemble(grid, level.registryAccess()))
                .orElse(ItemStack.EMPTY);
    }

    /** What a crafting grid holding these stacks in one row leaves in it once crafted, the empty slots included. */
    public static List<ItemStack> craftingRemainder(ServerLevel level, ItemStack... stacks) {
        CraftingContainer grid = grid(stacks.length, 1, stacks);
        return recipeFor(level, grid)
                .map(recipe -> List.copyOf(recipe.getRemainingItems(grid)))
                .orElse(List.of());
    }

    /** A grid filled up front: without a menu, setting a slot would tell a menu that isn't there. */
    private static CraftingContainer grid(int width, int height, ItemStack... stacks) {
        return new TransientCraftingContainer(null, width, height, NonNullList.of(ItemStack.EMPTY, stacks));
    }

    private static Optional<CraftingRecipe> recipeFor(ServerLevel level, CraftingContainer grid) {
        return level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, grid, level);
    }

    /** A banner of this colour with one pattern on it, kept in its {@code BlockEntityTag} as a placed banner keeps it. */
    public static ItemStack patternedBanner(ServerLevel level, Item banner, ResourceKey<BannerPattern> pattern, DyeColor color) {
        ItemStack stack = new ItemStack(banner);
        CompoundTag blockEntityTag = stack.getOrCreateTagElement("BlockEntityTag");
        blockEntityTag.put("Patterns", new BannerPattern.Builder().addPattern(pattern, color).toListTag());
        return stack;
    }

    /** The translation key the game names a stack by. */
    public static String nameKey(ItemStack stack) {
        return stack.getDescriptionId();
    }

    /** How many ticks a stack burns in a furnace, 0 where it isn't fuel. */
    public static int burnTime(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, null);
    }

    /** What a block drops when broken by hand, by its loot table. */
    public static List<ItemStack> drops(ServerLevel level, BlockPos pos) {
        return Block.getDrops(level.getBlockState(pos), level, pos, level.getBlockEntity(pos));
    }

    /**
     * The stack picking a block with the middle button gives. Forge's client asks the block's own form, which is
     * handed what was hit and who picked, and the decorated board answers only that one.
     */
    public static ItemStack pickBlock(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getCloneItemStack(new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false), level, pos, null);
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
        return ItemStack.isSameItemSameTags(first, second);
    }

    /** Names a stack as an anvil does. */
    public static void rename(ItemStack stack, String name) {
        stack.setHoverName(Component.literal(name));
    }

    /**
     * A player on the server, in the test's level, who can click blocks. Forge's {@code makeMockServerPlayerInLevel}
     * fails on a connection with no channel, so this is one of Forge's fake players, a fresh one each time, in survival.
     */
    public static ServerPlayer mockPlayer(GameTestHelper helper) {
        return new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), "test-mock-player"));
    }

    /** Whether the entities of the chunks around the world spawn have loaded, which a mock player waits for. */
    public static boolean entitiesLoadedAroundSpawn(ServerLevel level, int radius) {
        return ChunkPos.rangeClosed(new ChunkPos(level.getSharedSpawnPos()), radius)
                .allMatch(chunk -> level.areEntitiesLoaded(chunk.toLong()));
    }

    /** Right-clicks the top of a block with what the player holds, as the game does when a player clicks. */
    public static void useItemOn(ServerPlayer player, BlockPos pos) {
        player.gameMode.useItemOn(player, player.serverLevel(), player.getMainHandItem(), InteractionHand.MAIN_HAND,
                new BlockHitResult(Vec3.atCenterOf(pos).relative(Direction.UP, 0.5), Direction.UP, pos, false));
    }

    /**
     * A plain cutting board's block entity, as the tests use it. The mod's own on this line: Forge 1.20.1 can't add
     * blocks to Farmer's Delight's block entity type, so the boards have a copy of it.
     */
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
        if (!(level.getBlockEntity(pos) instanceof ModCuttingBoardBlockEntity board)) return Optional.empty();
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
}
