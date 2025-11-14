package com.phantomwing.choppersdelight.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.FileNotFoundException;
import java.util.Map;


@Mixin(RecipeManager.class)
public class CuttingBoardRecipeMixin {
    @Unique
    private static final String DEFAULT_CUTTING_BOARD_JSON = """
        {
          "type": "minecraft:crafting_shaped",
          "category": "misc",
          "key": {
            "#": {
              "item": "minecraft:spruce_planks"
            },
            "/": {
              "item": "minecraft:stick"
            }
          },
          "pattern": [
            "/##",
            "/##"
          ],
          "result": {
            "count": 1,
            "id": "farmersdelight:cutting_board"
          }
        }
    """;

    @Inject(
        method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
        at = @At("HEAD"))
    private void onApply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) throws FileNotFoundException {
        ResourceLocation targetId = ResourceLocation.fromNamespaceAndPath("farmersdelight", "cutting_board");
        JsonElement el = JsonParser.parseString(DEFAULT_CUTTING_BOARD_JSON);
        jsons.put(targetId, el);
    }
}