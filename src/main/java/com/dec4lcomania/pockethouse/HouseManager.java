package com.dec4lcomania.pockethouse;

import com.dec4lcomania.pockethouse.block.entity.HouseCoreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

public class HouseManager {
    private static final Set<HouseCoreBlockEntity> ACTIVE_HOUSES = new HashSet<>();
    private static final Set<BlockPos> HOUSE_BLOCKS = new HashSet<>();

    public static boolean isDroppable(BlockPos blockPos) {
        return !HOUSE_BLOCKS.contains(blockPos);
    }

    public static void register(HouseCoreBlockEntity core) {
        ACTIVE_HOUSES.add(core);
        BlockPos.betweenClosed(
                core.getBox().minX(), core.getBox().minY(), core.getBox().minZ(),
                core.getBox().maxX(), core.getBox().maxY(), core.getBox().maxZ()
        ).forEach( pos -> {
            if (!core.getLevel().getBlockState(pos).isAir()) {
                HOUSE_BLOCKS.add(new BlockPos(pos));
            }
                }
        );
    }

    public static boolean isActive(HouseCoreBlockEntity core) {
        return ACTIVE_HOUSES.contains(core);
    }

    public static void unregister(HouseCoreBlockEntity core) {
        ACTIVE_HOUSES.remove(core);
        BlockPos.betweenClosed(
                core.getBox().minX(), core.getBox().minY(), core.getBox().minZ(),
                core.getBox().maxX(), core.getBox().maxY(), core.getBox().maxZ()
        ).forEach(pos -> {
            if (!core.getLevel().getBlockState(pos).isEmpty() && HOUSE_BLOCKS.contains(pos)) {
                HOUSE_BLOCKS.remove(pos);
            }
        });
    }

    public static boolean isInsideHouse(Level level, BlockPos pos) {
        for (HouseCoreBlockEntity core : ACTIVE_HOUSES) {
            if (core.getLevel() == level && core.getBox().isInside(pos)) {
                return true;
            }
        }
        return false;
    }

    public static void removeBlock(BlockPos pos) {
        HOUSE_BLOCKS.remove(pos);
    }

    public static void clearLevel(Level level) {
        ACTIVE_HOUSES.removeIf(core ->
            core.getLevel()== level
        );
    }
}