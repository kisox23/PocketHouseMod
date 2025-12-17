package com.dec4lcomania.pockethouse.block.entity;

import com.dec4lcomania.pockethouse.PocketHouseMod;
import com.dec4lcomania.pockethouse.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, PocketHouseMod.MODID);

    public static final Supplier<BlockEntityType<HouseCoreBlockEntity>> HOUSE_CORE_BE =
            BLOCK_ENTITIES.register("house_core_be",
                    () -> BlockEntityType.Builder.of(
                            HouseCoreBlockEntity::new, ModBlocks.HOUSE_CORE_BLOCK.get()).build(null)
                    );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
