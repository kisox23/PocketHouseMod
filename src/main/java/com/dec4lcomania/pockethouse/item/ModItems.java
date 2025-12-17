package com.dec4lcomania.pockethouse.item;

import com.dec4lcomania.pockethouse.PocketHouseMod;
import com.dec4lcomania.pockethouse.item.custom.PocketHouseItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    //register the item in the game
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PocketHouseMod.MODID);
    //deferred item
    public static final DeferredItem<Item> POCKET_HOUSE = ITEMS.register("pocket_house",
            () -> new PocketHouseItem(new Item.Properties()));
    //call for the event bus
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
