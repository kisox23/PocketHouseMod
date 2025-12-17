package com.example.examplemod.event;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.HouseManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

@EventBusSubscriber(modid = ExampleMod.MODID)
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
}

