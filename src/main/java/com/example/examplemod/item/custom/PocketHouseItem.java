package com.example.examplemod.item.custom;

import com.example.examplemod.block.ModBlocks;
import com.example.examplemod.block.entity.HouseCoreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class PocketHouseItem extends Item {
    public PocketHouseItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos clicked = context.getClickedPos();
        BlockState clickedState = level.getBlockState(clicked);

        Direction forward = player.getDirection();
        Direction right = forward.getClockWise();
        BlockPos origin = clicked.relative(forward, 6);
        origin = origin.relative(right, 3);

        BlockPos center = origin
                .relative(forward.getOpposite(), 3)
                .relative(right.getOpposite(), 3)
                .above();
        
        Rotation rotation;
        switch (forward) {
            case EAST:
                rotation = Rotation.CLOCKWISE_180;
                break;
            case NORTH:
                rotation = Rotation.CLOCKWISE_90;
                break;
            case SOUTH:
                rotation = Rotation.COUNTERCLOCKWISE_90;
                break;
            default:
                rotation = Rotation.NONE;
                break;
        }

        if (!canSpawnHouse(level, center)) {
            if (player != null)
                player.displayClientMessage(
                        Component.literal("Space is not enough!"),
                        true
                );
            return InteractionResult.FAIL;
        }

        //se è un blocco senza collisioni
        if (clickedState.getCollisionShape(level, clicked).isEmpty()) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            BoundingBox box = spawnHouse(level, origin.above(), rotation, player);
            level.setBlock(center, ModBlocks.HOUSE_CORE_BLOCK.get().defaultBlockState(), 3);
            level.setBlock(
                    origin.above().relative(forward.getOpposite(), 1),
                    ModBlocks.HOUSE_BUTTON_BLOCK.get().defaultBlockState()
                            .setValue(ButtonBlock.FACE, AttachFace.WALL)
                            .setValue(ButtonBlock.FACING, right),
                    3
            );

            BlockEntity be = level.getBlockEntity(center);
            if (be instanceof HouseCoreBlockEntity core) {
                core.init(box);
            }
            context.getItemInHand().shrink(1);
        }

        System.out.println("Center: " + center.getX() + ", " + center.getY() + ", " + center.getZ());
        System.out.println("Origin: " + origin.getX() + ", " + origin.getY() + ", " + origin.getZ());

        return InteractionResult.SUCCESS;
    }

    private boolean canSpawnHouse(Level level, BlockPos center) {
        int radius = 3;
        center = center.below();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                BlockPos pos = center.offset(dx, 0, dz);
                BlockState state = level.getBlockState(pos);
                if (state.isAir() || state.getCollisionShape(level, pos).isEmpty()) {
                    return false;
                }
                BlockPos above = pos.above();
                if (!level.getBlockState(above).isAir()) {
                    return false;
                }
            }
        }
        return true;
    }

    private BoundingBox spawnHouse(Level level, BlockPos origin, Rotation rotation, Player player) {
        ResourceLocation rl = ResourceLocation.fromNamespaceAndPath("examplemod", "pocket_house_structure");
        StructureTemplate template = level.getServer().getStructureManager().get(rl).orElseThrow();
        StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(rotation).setIgnoreEntities(false);

        level.playLocalSound(player, SoundEvents.FIREWORK_ROCKET_TWINKLE, SoundSource.AMBIENT, 50, 0);

        assert level instanceof ServerLevel;
        template.placeInWorld(
                (ServerLevelAccessor) level,
                origin,
                origin,
                settings,
                level.random,
                3);

        return template.getBoundingBox(settings, origin);
    }

}