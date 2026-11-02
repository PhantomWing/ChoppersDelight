package com.phantomwing.choppersdelight.gametest;

import net.minecraft.gametest.framework.GameTestHelper;

import java.util.List;
import java.util.function.Consumer;

/**
 * Every game test on this line. A test is its body and its entry here, and both read the same on
 * every line: how the list is registered changed at 1.21.5 and belongs to {@link GameTestRegistration}
 * alone, so porting a test never touches a registration class or a JSON.
 */
public final class GameTests {
    public static final List<Test> ALL = List.of(
            test("creative_tab_builds_without_a_client", CuttingBoardGameTest::creativeTabBuildsWithoutAClient),
            test("every_board_is_crafted_from_its_planks", CuttingBoardGameTest::everyBoardIsCraftedFromItsPlanks),
            test("every_board_drops_itself", CuttingBoardGameTest::everyBoardDropsItself),
            test("every_board_is_tagged_and_mined_with_an_axe", CuttingBoardGameTest::everyBoardIsTaggedAndMinedWithAnAxe),
            test("every_board_burns_as_fuel", CuttingBoardGameTest::everyBoardBurnsAsFuel),
            test("every_board_cuts_beef_with_a_knife", CuttingBoardGameTest::everyBoardCutsBeefWithAKnife),
            test("a_renamed_board_keeps_its_name", CuttingBoardGameTest::aRenamedBoardKeepsItsName),
            test("sneaking_with_a_tool_carves_it_into_a_board", CuttingBoardGameTest::sneakingWithAToolCarvesItIntoABoard),
            test("decorated_board_is_not_decorated_again", DecoratedCuttingBoardGameTest::decoratedBoardIsNotDecoratedAgain),
            test("removing_the_banner_gives_both_back", DecoratedCuttingBoardGameTest::removingTheBannerGivesBothBack),
            test("decorated_boards_of_one_design_stack", DecoratedCuttingBoardGameTest::decoratedBoardsOfOneDesignStack),
            test("a_decorated_board_is_named_after_its_wood", DecoratedCuttingBoardGameTest::aDecoratedBoardIsNamedAfterItsWood),
            test("a_broken_decorated_board_keeps_its_design", DecoratedCuttingBoardGameTest::aBrokenDecoratedBoardKeepsItsDesign),
            test("a_broken_decorated_board_drops_what_it_holds", DecoratedCuttingBoardGameTest::aBrokenDecoratedBoardDropsWhatItHolds),
            test("picking_a_decorated_board_gives_its_design", DecoratedCuttingBoardGameTest::pickingADecoratedBoardGivesItsDesign),
            test("a_decorated_board_cuts_cake_with_a_knife", DecoratedCuttingBoardGameTest::aDecoratedBoardCutsCakeWithAKnife),
            test("a_hopper_takes_the_item_off_a_decorated_board", DecoratedCuttingBoardGameTest::aHopperTakesTheItemOffADecoratedBoard),
            test("a_decorated_board_gives_a_comparator_signal", DecoratedCuttingBoardGameTest::aDecoratedBoardGivesAComparatorSignal),
            test("sneaking_with_a_tool_carves_it_into_a_decorated_board", DecoratedCuttingBoardGameTest::sneakingWithAToolCarvesItIntoADecoratedBoard));

    private GameTests() {
    }

    /** A required test in the mod's {@code empty} structure, with vanilla's default of 100 ticks. */
    private static Test test(String name, Consumer<GameTestHelper> body) {
        return new Test(name, body, "empty", 100);
    }

    /** One test: its id in the mod's namespace, its body, the structure it runs in and its time limit. */
    public record Test(String name, Consumer<GameTestHelper> body, String structure, int maxTicks) {
        public Test maxTicks(int ticks) {
            return new Test(name, body, structure, ticks);
        }
    }
}
