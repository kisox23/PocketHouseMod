package com.dec4lcomania.pockethouse.block.custom;

import com.dec4lcomania.pockethouse.block.entity.HouseCoreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HouseCoreBlock extends Block implements EntityBlock {
    public HouseCoreBlock(Properties properties) {
        super(properties
                .strength(-1.0F, 360000.0F)
                .noLootTable());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HouseCoreBlockEntity(pos, state);
    }

}
