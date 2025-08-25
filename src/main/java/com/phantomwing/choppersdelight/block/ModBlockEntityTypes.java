package com.phantomwing.choppersdelight.block;

import com.phantomwing.choppersdelight.ChoppersDelight;
import com.phantomwing.choppersdelight.Compatibility;
import com.phantomwing.choppersdelight.block.entity.DecoratedCuttingBoardBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@EventBusSubscriber(modid = ChoppersDelight.MOD_ID)
public class ModBlockEntityTypes
{
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ChoppersDelight.MOD_ID);

    public static final Supplier<BlockEntityType<DecoratedCuttingBoardBlockEntity>> DECORATED_CUTTING_BOARD = TILES.register("decorated_cutting_board", () -> BlockEntityType.Builder.of(DecoratedCuttingBoardBlockEntity::new,
        ModBlocks.DECORATED_CUTTING_BOARD.get()
    ).build(null));

    public static void register(IEventBus eventBus) {
        TILES.register(eventBus);
    }

    @SubscribeEvent
    public static void onRegisterFarmersDelightBlockEntityTypes(final BlockEntityTypeAddBlocksEvent event) {
        Stream<Block> blocks = ModBlocks.MINECRAFT_CUTTING_BOARDS.stream().map(Supplier::get);

        if (Compatibility.IsBiomesOPlentyLoaded()) {
            blocks = Stream.concat(blocks, ModBlocks.BIOMES_O_PLENTY_CUTTING_BOARDS.stream().map(Supplier::get));
        }

        if (Compatibility.IsBiomesWeveGoneLoaded()) {
            blocks = Stream.concat(blocks, ModBlocks.BIOMES_WEVE_GONE_CUTTING_BOARDS.stream().map(Supplier::get));
        }

        event.modify(vectorwing.farmersdelight.common.registry.ModBlockEntityTypes.CUTTING_BOARD.get(), blocks.toArray(Block[]::new));
    }
}