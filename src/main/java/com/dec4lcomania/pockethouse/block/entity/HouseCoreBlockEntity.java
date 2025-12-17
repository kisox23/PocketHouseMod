package com.dec4lcomania.pockethouse.block.entity;

import com.dec4lcomania.pockethouse.HouseManager;
import com.dec4lcomania.pockethouse.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.UUID;

public class HouseCoreBlockEntity extends BlockEntity {
    private BoundingBox box;
    private UUID houseID;
    private boolean despawned = false;

    public HouseCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HOUSE_CORE_BE.get(), pos, state);
    }

    public void init(BoundingBox box) {
        this.box = box;
        if (this.houseID == null)
            assignNewID();
        HouseManager.register(this);
    }

    public BoundingBox getBox() {
        return this.box;
    }

    public void despawnHouse() {
        System.out.println("Despawning initiated");
        if (despawned || level == null || level.isClientSide()) return;
        despawned = true;

        //togli blocchi dipendenti
        for (BlockPos pos : BlockPos.betweenClosed(
                box.minX(), box.minY(), box.minZ(),
                box.maxX(), box.maxY(), box.maxZ())) {
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();

            if (block instanceof TorchBlock ||
                block instanceof DoorBlock ||
                block instanceof BedBlock) {

                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2 | 32);
            }
        }

        //togli blocch indipendenti
        for (BlockPos pos : BlockPos.betweenClosed(
                box.minX(), box.minY(), box.minZ(),
                box.maxX(), box.maxY(), box.maxZ())) {

            if (HouseManager.isDroppable(pos))
                level.destroyBlock(pos, true, null);

            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }

        super.setRemoved();
        if (!level.isClientSide())
            HouseManager.unregister(this);

        Containers.dropItemStack(
                level,
                worldPosition.getX(),
                worldPosition.getY(),
                worldPosition.getZ(),
                new ItemStack(ModItems.POCKET_HOUSE.get())
        );
    }

    public void assignNewID() {
        if (this.houseID == null) {
            houseID = UUID.randomUUID();
            setChanged();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (houseID != null) {
            tag.putUUID("houseID", houseID);
        }

        if (box != null) {
            tag.putIntArray("houseBox", new int[]{
                    box.minX(), box.minY(), box.minZ(),
                    box.maxX(), box.maxY(), box.maxZ()
            });
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.hasUUID("houseID")) {
            houseID = tag.getUUID("houseID");
        }

        if (tag.contains("houseBox")) {
            int[] b = tag.getIntArray("houseBox");
            if (b.length == 6) {
                box = new BoundingBox(
                        b[0], b[1], b[2],
                        b[3], b[4], b[5]
                );
            }
        }
    }

    @Override
    public void onLoad() {
        if (!level.isClientSide() && box != null) {
            HouseManager.register(this);
        }
    }

}

//ordine logico
/*
1. spawna il blockentity
2. si registra
3. si chiude il mondo
4. si salva l'NBT
5. si riapre il mondo
6. si carica l'NBT
7. si riempiono i registri
 */
