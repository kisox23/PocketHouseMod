package com.dec4lcomania.pockethouse.event;

import com.dec4lcomania.pockethouse.PocketHouseMod;
import com.dec4lcomania.pockethouse.HouseManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber(modid = PocketHouseMod.MODID)
public class HouseEvents {
    @SubscribeEvent
    public static void onHarvestDrops(BlockDropsEvent event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();

        if (level.isClientSide) return;

        //se sta nel box della casa E NON è un blocco della casa
        if (HouseManager.isInsideHouse(level, pos) && !HouseManager.isDroppable(pos)) {
            HouseManager.removeBlock(pos);
            event.getDrops().clear();
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        Level level = (Level) event.getLevel();
        if (level.isClientSide()) return;
        HouseManager.clearLevel(level);
    }
}

