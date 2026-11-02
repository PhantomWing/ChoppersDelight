package com.phantomwing.choppersdelight.gametest;

import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.item.ModItems;
import com.phantomwing.choppersdelight.tags.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public final class CuttingBoardGameTest {
    /** Where a test puts its board: in the structure's own column, whose chunk is sure to tick entities. */
    static final BlockPos BOARD = new BlockPos(0, 2, 0);

    /** Each vanilla wood's board and the planks it is crafted from, Farmer's Delight's own as spruce's. */
    private static final String[][] PLANKS = {
            {"choppersdelight:oak_cutting_board", "minecraft:oak_planks"},
            {"choppersdelight:birch_cutting_board", "minecraft:birch_planks"},
            {"choppersdelight:jungle_cutting_board", "minecraft:jungle_planks"},
            {"choppersdelight:acacia_cutting_board", "minecraft:acacia_planks"},
            {"choppersdelight:dark_oak_cutting_board", "minecraft:dark_oak_planks"},
            {"choppersdelight:mangrove_cutting_board", "minecraft:mangrove_planks"},
            {"choppersdelight:cherry_cutting_board", "minecraft:cherry_planks"},
            {"choppersdelight:bamboo_cutting_board", "minecraft:bamboo_planks"},
            {"choppersdelight:crimson_cutting_board", "minecraft:crimson_planks"},
            {"choppersdelight:warped_cutting_board", "minecraft:warped_planks"},
            {"farmersdelight:cutting_board", "minecraft:spruce_planks"}};

    private CuttingBoardGameTest() {
    }

    /**
     * A server-side mod (Polymer) builds the tab where there is no client level. The preset decorated boards
     * once all fell back to the same plain stack there, and the tab refused the duplicates.
     */
    public static void creativeTabBuildsWithoutAClient(GameTestHelper helper) {
        CreativeModeTab tab = TestCompat.modTab();
        try {
            TestCompat.buildContents(tab, helper.getLevel());
        } catch (IllegalStateException e) {
            TestCompat.fail(helper, "The creative tab couldn't be built on the server: " + e.getMessage());
        }

        long presets = tab.getDisplayItems().stream()
                .filter(stack -> stack.is(ModItems.DECORATED_CUTTING_BOARD.get()))
                .count();
        TestCompat.assertTrue(helper, presets == 4, "Expected the 4 preset decorated cutting boards in the tab, found " + presets);
        helper.succeed();
    }

    /** Farmer's Delight's board takes any planks, so the same grid of another wood's planks must still make that wood's. */
    public static void everyBoardIsCraftedFromItsPlanks(GameTestHelper helper) {
        List<String> wrong = new ArrayList<>();
        for (String[] row : PLANKS) {
            ItemStack stick = new ItemStack(Items.STICK);
            ItemStack planks = new ItemStack(TestCompat.item(row[1]));
            ItemStack made = TestCompat.craftGrid(helper.getLevel(), 3, 2, stick, planks, planks, stick, planks, planks);
            if (!made.is(TestCompat.item(row[0]))) wrong.add(row[1] + " made " + (made.isEmpty() ? "nothing" : made.getItem()));
        }
        TestCompat.assertTrue(helper, wrong.isEmpty(), "Sticks and planks should make that wood's cutting board: " + String.join(", ", wrong));
        helper.succeed();
    }

    public static void everyBoardDropsItself(GameTestHelper helper) {
        List<String> wrong = new ArrayList<>();
        for (Supplier<Block> board : ModBlocks.MINECRAFT_CUTTING_BOARDS) {
            helper.setBlock(BOARD, board.get());
            List<ItemStack> drops = TestCompat.drops(helper.getLevel(), helper.absolutePos(BOARD));
            if (drops.size() != 1 || !drops.get(0).is(board.get().asItem())) wrong.add(board.get() + " dropped " + drops);
        }
        TestCompat.assertTrue(helper, wrong.isEmpty(), "Each cutting board should drop itself: " + String.join(", ", wrong));
        helper.succeed();
    }

    public static void everyBoardIsTaggedAndMinedWithAnAxe(GameTestHelper helper) {
        List<String> wrong = new ArrayList<>();
        for (Block board : boardsAndDecorated()) {
            if (!board.defaultBlockState().is(ModTags.Blocks.CUTTING_BOARDS)) wrong.add(board + " isn't in the block tag");
            if (!new ItemStack(board).is(ModTags.Items.CUTTING_BOARDS)) wrong.add(board + " isn't in the item tag");
            if (!board.defaultBlockState().is(BlockTags.MINEABLE_WITH_AXE)) wrong.add(board + " isn't mined with an axe");
        }
        TestCompat.assertTrue(helper, wrong.isEmpty(), String.join(", ", wrong));
        helper.succeed();
    }

    public static void everyBoardBurnsAsFuel(GameTestHelper helper) {
        List<String> wrong = new ArrayList<>();
        for (Block board : boardsAndDecorated()) {
            int ticks = TestCompat.burnTime(new ItemStack(board));
            if (ticks != 200) wrong.add(board + " burns " + ticks + " ticks");
        }
        TestCompat.assertTrue(helper, wrong.isEmpty(), "Each cutting board should burn 200 ticks, as Farmer's Delight's does: " + String.join(", ", wrong));
        helper.succeed();
    }

    /** Every board must have a cutting board's block entity: on Fabric the mod adds its boards to Farmer's Delight's type itself. */
    public static void everyBoardCutsBeefWithAKnife(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        Item mincedBeef = TestCompat.item("farmersdelight:minced_beef");
        int boards = 0;
        for (Supplier<Block> board : ModBlocks.MINECRAFT_CUTTING_BOARDS) {
            helper.setBlock(BOARD, board.get());
            Optional<TestCompat.Board> found = TestCompat.boardAt(level, helper.absolutePos(BOARD));
            if (found.isEmpty()) {
                TestCompat.fail(helper, board.get() + " has no cutting board block entity, has " + level.getBlockEntity(helper.absolutePos(BOARD)));
                return;
            }
            TestCompat.Board cuttingBoard = found.get();
            cuttingBoard.add(new ItemStack(Items.BEEF));
            TestCompat.assertTrue(helper, cuttingBoard.cut(new ItemStack(TestCompat.item("farmersdelight:iron_knife"))),
                    "A knife should cut beef on " + board.get());
            TestCompat.assertTrue(helper, cuttingBoard.isEmpty(), "The beef should be gone from " + board.get() + " once cut");
            boards++;
        }
        int expected = boards * 2;
        helper.succeedWhen(() -> {
            int made = countAround(helper, BOARD, mincedBeef);
            TestCompat.assertTrue(helper, made == expected, "Expected " + expected + " minced beef, found " + made);
        });
    }

    /** On Fabric the mod names Farmer's Delight's board Spruce Cutting Board, which must leave a name given in an anvil. */
    public static void aRenamedBoardKeepsItsName(GameTestHelper helper) {
        for (Item board : List.of(TestCompat.item("farmersdelight:cutting_board"), ModItems.OAK_CUTTING_BOARD.get())) {
            ItemStack stack = new ItemStack(board);
            TestCompat.rename(stack, "Grandma's Board");
            String name = stack.getHoverName().getString();
            TestCompat.assertTrue(helper, name.equals("Grandma's Board"), board + " renamed Grandma's Board is called " + name);
        }
        helper.succeed();
    }

    public static void sneakingWithAToolCarvesItIntoABoard(GameTestHelper helper) {
        helper.setBlock(BOARD, ModBlocks.OAK_CUTTING_BOARD.get());
        whenSpawnLoaded(helper, 80, () -> {
            ServerPlayer player = TestCompat.mockPlayer(helper);
            player.setShiftKeyDown(true);
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
            TestCompat.useItemOn(player, helper.absolutePos(BOARD));

            Optional<TestCompat.Board> found = TestCompat.boardAt(helper.getLevel(), helper.absolutePos(BOARD));
            if (found.isEmpty()) {
                TestCompat.fail(helper, "The oak cutting board has no cutting board block entity");
                return;
            }
            TestCompat.Board cuttingBoard = found.get();
            TestCompat.assertTrue(helper, cuttingBoard.isCarved(), "Sneaking with an axe should carve it into the oak cutting board");
            TestCompat.assertTrue(helper, cuttingBoard.stored().is(Items.IRON_AXE), "The board should hold the axe, holds " + cuttingBoard.stored());
            helper.succeed();
        });
    }

    /**
     * Runs {@code then} once the entities around the world spawn have loaded: on 1.21.6 to 1.21.8 a mock player waits
     * for them, and the level can't tick to load them while a test holds it.
     */
    static void whenSpawnLoaded(GameTestHelper helper, int ticksLeft, Runnable then) {
        if (TestCompat.entitiesLoadedAroundSpawn(helper.getLevel(), 2)) {
            then.run();
        } else if (ticksLeft <= 0) {
            TestCompat.fail(helper, "The entities around the world spawn never loaded, so no mock player could be made");
        } else {
            helper.runAfterDelay(1, () -> whenSpawnLoaded(helper, ticksLeft - 1, then));
        }
    }

    static List<Block> boardsAndDecorated() {
        List<Block> boards = new ArrayList<>();
        ModBlocks.MINECRAFT_CUTTING_BOARDS.forEach(board -> boards.add(board.get()));
        boards.add(ModBlocks.DECORATED_CUTTING_BOARD.get());
        return boards;
    }

    /** How many of an item lie around a spot, the fall below it included, as item entities. */
    static int countAround(GameTestHelper helper, BlockPos pos, Item item) {
        BlockPos at = helper.absolutePos(pos);
        AABB around = new AABB(at.getX() - 2, at.getY() - 12, at.getZ() - 2, at.getX() + 3, at.getY() + 3, at.getZ() + 3);
        return helper.getLevel().getEntitiesOfClass(ItemEntity.class, around, entity -> entity.getItem().is(item)).stream()
                .mapToInt(entity -> entity.getItem().getCount())
                .sum();
    }
}
