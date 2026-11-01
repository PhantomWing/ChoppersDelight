package com.phantomwing.choppersdelight.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(RecipeManager.class)
public class CuttingBoardRecipeMixin {
    @Unique
    private static final String DEFAULT_CUTTING_BOARD_JSON = """
        {
          "type": "minecraft:crafting_shaped",
          "category": "misc",
          "key": {
            "#": "minecraft:spruce_planks",
            "/": "minecraft:stick"
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

    // Farmer's Delight's own recipe takes any planks, and its data is read after ours, so ours is put in place
    // once every pack has been read.
    @WrapOperation(
        method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/world/item/crafting/RecipeMap;",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/SimpleJsonResourceReloadListener;scanDirectory(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/FileToIdConverter;Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/util/Map;)V"))
    private <T> void overrideCuttingBoardRecipe(ResourceManager resourceManager, FileToIdConverter converter, DynamicOps<JsonElement> ops, Codec<T> codec, Map<Identifier, T> recipes, Operation<Void> original) {
        original.call(resourceManager, converter, ops, codec, recipes);

        Identifier targetId = Identifier.fromNamespaceAndPath("farmersdelight", "cutting_board");
        codec.parse(ops, JsonParser.parseString(DEFAULT_CUTTING_BOARD_JSON))
                .ifSuccess(recipe -> recipes.put(targetId, recipe));
    }
}
