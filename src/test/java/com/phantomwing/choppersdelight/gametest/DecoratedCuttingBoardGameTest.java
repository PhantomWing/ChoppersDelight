package com.phantomwing.choppersdelight.gametest;

import com.phantomwing.choppersdelight.block.ModBlocks;
import com.phantomwing.choppersdelight.block.entity.DecoratedCuttingBoardBlockEntity;
import com.phantomwing.choppersdelight.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

import static com.phantomwing.choppersdelight.gametest.CuttingBoardGameTest.BOARD;
import static com.phantomwing.choppersdelight.gametest.CuttingBoardGameTest.countAround;

public final class DecoratedCuttingBoardGameTest {
    private DecoratedCuttingBoardGameTest() {
    }

    public static void decoratedBoardIsNotDecoratedAgain(GameTestHelper helper) {
        ItemStack banner = new ItemStack(Items.WHITE_BANNER);

        ItemStack decorated = TestCompat.craft(helper.getLevel(), new ItemStack(ModItems.OAK_CUTTING_BOARD.get()), banner);
        helper.assertTrue(decorated.is(ModItems.DECORATED_CUTTING_BOARD.get()),
                "An oak cutting board and a banner should make a decorated cutting board, made " + decorated);

        ItemStack again = TestCompat.craft(helper.getLevel(), decorated, banner);
        helper.assertTrue(again.isEmpty(), "A decorated cutting board and a banner shouldn't craft anything, made " + again);
        helper.succeed();
    }

    public static void removingTheBannerGivesBothBack(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        ItemStack banner = flowerBanner(level);
        ItemStack decorated = cherryBoardWith(level, banner);

        ItemStack result = TestCompat.craft(level, decorated);
        helper.assertTrue(ItemStack.isSameItemSameComponents(result, banner),
                "A decorated cutting board alone should give back its banner with its pattern, gave " + result);
        List<ItemStack> left = TestCompat.craftingRemainder(level, decorated);
        helper.assertTrue(left.stream().anyMatch(stack -> stack.is(ModItems.CHERRY_CUTTING_BOARD.get())),
                "Taking the banner off should leave the cherry cutting board in the grid, left " + left);
        helper.succeed();
    }

    public static void decoratedBoardsOfOneDesignStack(GameTestHelper helper) {
        ItemStack first = cherryBoardWith(helper.getLevel(), flowerBanner(helper.getLevel()));
        ItemStack second = cherryBoardWith(helper.getLevel(), flowerBanner(helper.getLevel()));
        helper.assertTrue(ItemStack.isSameItemSameComponents(first, second), "Two decorated cutting boards of one design should stack");
        ItemStack other = TestCompat.craft(helper.getLevel(), new ItemStack(ModItems.CHERRY_CUTTING_BOARD.get()), new ItemStack(Items.WHITE_BANNER));
        helper.assertTrue(!ItemStack.isSameItemSameComponents(first, other), "Decorated cutting boards of two designs shouldn't stack");
        helper.succeed();
    }

    public static void aDecoratedBoardIsNamedAfterItsWood(GameTestHelper helper) {
        ItemStack decorated = cherryBoardWith(helper.getLevel(), flowerBanner(helper.getLevel()));
        String expected = TestCompat.nameKey(new ItemStack(ModItems.CHERRY_CUTTING_BOARD.get()));
        String name = TestCompat.nameKey(decorated);
        helper.assertTrue(name.equals(expected), "A decorated cherry cutting board should be named " + expected + ", is " + name);
        helper.succeed();
    }

    public static void aBrokenDecoratedBoardKeepsItsDesign(GameTestHelper helper) {
        ItemStack decorated = place(helper);
        helper.getLevel().destroyBlock(helper.absolutePos(BOARD), true);
        helper.succeedWhen(() -> {
            List<ItemStack> dropped = itemsAround(helper);
            helper.assertTrue(dropped.stream().anyMatch(stack -> ItemStack.isSameItemSameComponents(stack, decorated)),
                    "Breaking the decorated cutting board should drop it with its wood and banner, dropped " + dropped);
            long boards = dropped.stream().filter(stack -> stack.is(ModItems.DECORATED_CUTTING_BOARD.get())).count();
            helper.assertTrue(boards == 1, "Breaking it should drop one decorated cutting board, dropped " + boards);
        });
    }

    public static void aBrokenDecoratedBoardDropsWhatItHolds(GameTestHelper helper) {
        place(helper);
        board(helper).addItem(new ItemStack(Items.CARROT));
        helper.getLevel().destroyBlock(helper.absolutePos(BOARD), true);
        helper.succeedWhen(() -> {
            int carrots = countAround(helper, BOARD, Items.CARROT);
            helper.assertTrue(carrots == 1, "Breaking the decorated cutting board should drop the carrot on it, dropped " + carrots);
        });
    }

    public static void pickingADecoratedBoardGivesItsDesign(GameTestHelper helper) {
        ItemStack decorated = place(helper);
        ItemStack picked = TestCompat.pickBlock(helper.getLevel(), helper.absolutePos(BOARD));
        helper.assertTrue(ItemStack.isSameItemSameComponents(picked, decorated),
                "Picking the decorated cutting board should give it with its wood and banner, gave " + picked);
        helper.succeed();
    }

    public static void aDecoratedBoardCutsCakeWithAKnife(GameTestHelper helper) {
        place(helper);
        DecoratedCuttingBoardBlockEntity board = board(helper);
        board.addItem(new ItemStack(Items.CAKE));
        helper.assertTrue(board.processStoredItemUsingTool(new ItemStack(TestCompat.item("farmersdelight:iron_knife")), null),
                "A knife should cut the cake on a decorated cutting board");
        helper.assertTrue(board.isEmpty(), "The cake should be gone from the decorated cutting board once cut");
        Item slice = TestCompat.item("farmersdelight:cake_slice");
        helper.succeedWhen(() -> {
            int slices = countAround(helper, BOARD, slice);
            helper.assertTrue(slices == 7, "Expected 7 cake slices, found " + slices);
        });
    }

    /** Pipes and hoppers once found nothing to take on Fabric: the board's storage wasn't the one they asked. */
    public static void aHopperTakesTheItemOffADecoratedBoard(GameTestHelper helper) {
        BlockPos hopper = BOARD;
        BlockPos above = BOARD.above();
        helper.setBlock(hopper, Blocks.HOPPER);
        ItemStack decorated = cherryBoardWith(helper.getLevel(), flowerBanner(helper.getLevel()));
        placeAt(helper, above, decorated);
        DecoratedCuttingBoardBlockEntity board = (DecoratedCuttingBoardBlockEntity) helper.getLevel().getBlockEntity(helper.absolutePos(above));
        board.addItem(new ItemStack(Items.APPLE));
        helper.succeedWhen(() -> {
            Container container = (Container) helper.getLevel().getBlockEntity(helper.absolutePos(hopper));
            helper.assertTrue(container.countItem(Items.APPLE) == 1, "The hopper under the decorated cutting board should take its apple");
            helper.assertTrue(board.isEmpty(), "The decorated cutting board should be empty once the hopper took its apple");
        });
    }

    public static void aDecoratedBoardGivesAComparatorSignal(GameTestHelper helper) {
        place(helper);
        BlockPos at = helper.absolutePos(BOARD);
        helper.assertTrue(TestCompat.analogSignal(helper.getLevel(), at) == 0, "An empty decorated cutting board should give no signal");
        board(helper).addItem(new ItemStack(Items.CARROT));
        int signal = TestCompat.analogSignal(helper.getLevel(), at);
        helper.assertTrue(signal > 0, "A decorated cutting board holding a carrot should give a signal, gives " + signal);
        helper.succeed();
    }

    /** The decorated board has a carving handler of its own: Farmer's Delight's only finds its own block entity. */
    public static void sneakingWithAToolCarvesItIntoADecoratedBoard(GameTestHelper helper) {
        place(helper);
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        player.setShiftKeyDown(true);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
        TestCompat.useItemOn(player, helper.absolutePos(BOARD));

        DecoratedCuttingBoardBlockEntity board = board(helper);
        helper.assertTrue(board.isItemCarvingBoard(), "Sneaking with an axe should carve it into the decorated cutting board");
        helper.assertTrue(board.getStoredItem().is(Items.IRON_AXE), "The board should hold the axe, holds " + board.getStoredItem());
        helper.assertTrue(!board.canAddItem(new ItemStack(Items.CARROT)), "A board with a tool carved in shouldn't take anything else");

        player.setShiftKeyDown(false);
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        TestCompat.useItemOn(player, helper.absolutePos(BOARD));
        helper.assertTrue(board.isEmpty(), "An empty hand should take the axe off the board, which holds " + board.getStoredItem());
        helper.assertTrue(!board.isItemCarvingBoard(), "The board shouldn't count as carved once the axe is off");
        helper.succeed();
    }

    private static ItemStack flowerBanner(ServerLevel level) {
        return TestCompat.patternedBanner(level, Items.WHITE_BANNER, BannerPatterns.FLOWER, DyeColor.PINK);
    }

    private static ItemStack cherryBoardWith(ServerLevel level, ItemStack banner) {
        return TestCompat.craft(level, new ItemStack(ModItems.CHERRY_CUTTING_BOARD.get()), banner);
    }

    /** Places a decorated cherry board with a pink flower banner, as a player does from its item, and returns that item. */
    private static ItemStack place(GameTestHelper helper) {
        ItemStack decorated = cherryBoardWith(helper.getLevel(), flowerBanner(helper.getLevel()));
        placeAt(helper, BOARD, decorated);
        return decorated;
    }

    private static void placeAt(GameTestHelper helper, BlockPos pos, ItemStack decorated) {
        helper.setBlock(pos, ModBlocks.DECORATED_CUTTING_BOARD.get());
        BlockPos at = helper.absolutePos(pos);
        BlockState state = helper.getLevel().getBlockState(at);
        state.getBlock().setPlacedBy(helper.getLevel(), at, state, null, decorated);
    }

    private static DecoratedCuttingBoardBlockEntity board(GameTestHelper helper) {
        return (DecoratedCuttingBoardBlockEntity) helper.getLevel().getBlockEntity(helper.absolutePos(BOARD));
    }

    private static List<ItemStack> itemsAround(GameTestHelper helper) {
        BlockPos at = helper.absolutePos(BOARD);
        AABB around = new AABB(at.getX() - 2, at.getY() - 12, at.getZ() - 2, at.getX() + 3, at.getY() + 3, at.getZ() + 3);
        return helper.getLevel().getEntitiesOfClass(ItemEntity.class, around, entity -> true).stream()
                .map(ItemEntity::getItem)
                .toList();
    }
}
