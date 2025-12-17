package com.example.examplemod.block.custom;

import com.example.examplemod.block.entity.HouseCoreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class HouseButtonBlock extends ButtonBlock {
    public HouseButtonBlock(Properties properties) {
        super(
                BlockSetType.STONE,
                20,
                properties
                        .strength(-1.0F, 3600000.0F)
                        .noLootTable()
        );
    }

    @Override
    public void press(BlockState state, Level level, BlockPos pos, Player player) {
        super.press(state, level, pos, player);

        if (level.isClientSide()) return;

        System.out.println("Triggered!");
        triggerDespawn(level, pos);
    }

    private void triggerDespawn(Level level, BlockPos center) {
        BlockPos.betweenClosed(
            center.offset(-5, -5, -5),
            center.offset(5, 5, 5)
        ).forEach(pos -> {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof HouseCoreBlockEntity core) {
                core.despawnHouse();
            }
        });
    }
}
