package com.dec4lcomania.pockethouse.block;

import com.dec4lcomania.pockethouse.PocketHouseMod;
import com.dec4lcomania.pockethouse.block.custom.HouseButtonBlock;
import com.dec4lcomania.pockethouse.block.custom.HouseCoreBlock;
import com.dec4lcomania.pockethouse.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(PocketHouseMod.MODID);

    public static final DeferredBlock<Block> HOUSE_CORE_BLOCK = registerBlock("house_core_block",
            () -> new HouseCoreBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> HOUSE_BUTTON_BLOCK = registerBlock("house_button_block",
            () -> new HouseButtonBlock(BlockBehaviour.Properties.of()));

    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    public static <T extends Block> void registerBlockItem(String name, Supplier<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
