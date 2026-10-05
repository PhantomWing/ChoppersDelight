package com.phantomwing.choppersdelight.gametest;

import com.phantomwing.choppersdelight.item.ModItems;
import com.phantomwing.choppersdelight.ui.ModCreativeModeTab;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class CuttingBoardGameTest {
    private CuttingBoardGameTest() {
    }

    /**
     * A server-side mod (Polymer) builds the tab where there is no client level. The preset decorated boards
     * once all fell back to the same plain stack there, and the tab refused the duplicates.
     */
    public static void creativeTabBuildsWithoutAClient(GameTestHelper helper) {
        CreativeModeTab tab = ModCreativeModeTab.MOD_TAB.get();
        try {
            TestCompat.buildContents(tab, helper.getLevel());
        } catch (IllegalStateException e) {
            helper.fail("The creative tab couldn't be built on the server: " + e.getMessage());
        }

        long presets = tab.getDisplayItems().stream()
                .filter(stack -> stack.is(ModItems.DECORATED_CUTTING_BOARD.get()))
                .count();
        helper.assertTrue(presets == 4, "Expected the 4 preset decorated cutting boards in the tab, found " + presets);
        helper.succeed();
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
}
