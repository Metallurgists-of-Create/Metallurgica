package dev.metallurgists.metallurgica.content.temperature.hot_plate.heating_coil.behaviour;

import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.metallurgists.metallurgica.content.temperature.hot_plate.heating_coil.HeatingCoilType;
import dev.metallurgists.metallurgica.foundation.block_entity.behaviour.TankLiningBehaviour;
import dev.metallurgists.metallurgica.foundation.item.lining.tank_lining.TankLiningStats;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;

public class HeatingCoilBehaviour extends BlockEntityBehaviour {
    public static final BehaviourType<HeatingCoilBehaviour> TYPE = new BehaviourType<>();

    private ItemStack coilStack;
    private HeatingCoilType coilType;

    public HeatingCoilBehaviour(SmartBlockEntity be) {
        super(be);
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (isController()) {
            Containers.dropItemStack(getWorld(), getPos().getX(), getPos().getY(), getPos().getZ(), this.coilStack);
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

    }

    @Override
    public void tick() {
        super.tick();

    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        if (isController()) readNbt(nbt);
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        if (isController()) writeNbt(nbt);
    }

    public ItemStack getCoilStack() {
        if (isController()) {
            return getBlockEntity().getBehaviour(TYPE).coilStack;
        } else {
            SmartBlockEntity controllerBE = ((IMultiBlockEntityContainer) blockEntity).getControllerBE();
            if (controllerBE != null) {
                return controllerBE.getBehaviour(TYPE).coilStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public void setCoilStack(ItemStack coilStack) {
        if (isController()) {
            this.coilStack = coilStack;
            getBlockEntity().notifyUpdate();
        } else {
            SmartBlockEntity controllerBE = ((IMultiBlockEntityContainer) blockEntity).getControllerBE();
            if (controllerBE != null) {
                controllerBE.getBehaviour(TYPE).coilStack = coilStack;
                controllerBE.notifyUpdate();
            }
        }
    }

    public HeatingCoilType getCoilType() {
        if (isController()) {
            return getBlockEntity().getBehaviour(TYPE).coilType;
        } else {
            SmartBlockEntity controllerBE = ((IMultiBlockEntityContainer) blockEntity).getControllerBE();
            if (controllerBE != null) {
                return controllerBE.getBehaviour(TYPE).coilType;
            }
        }
        return null;
    }

    public void setCoilType(HeatingCoilType coilType) {
        if (isController()) {
            getBlockEntity().getBehaviour(TYPE).coilType = coilType;
        } else {
            SmartBlockEntity controllerBE = ((IMultiBlockEntityContainer) blockEntity).getControllerBE();
            if (controllerBE != null) {
                controllerBE.getBehaviour(TYPE).coilType = coilType;
            }
        }
    }

    public boolean hasCoil() {
        if (getCoilStack() == null) return false;
        return !getCoilStack().isEmpty();
    }

    public void setCoil(ItemStack coilStack) {
        setCoilType(HeatingCoilType.get(coilStack.getItem()));
        setCoilStack(coilStack);
        getBlockEntity().notifyUpdate();
    }

    public ItemStack removeCoil() {
        ItemStack coilStack = getCoilStack();
        if (coilStack.isEmpty()) return ItemStack.EMPTY;
        setCoilType(null);
        setCoilStack(ItemStack.EMPTY);
        getBlockEntity().notifyUpdate();
        return coilStack;
    }

    private SmartBlockEntity getBlockEntity() {
        if (blockEntity instanceof IMultiBlockEntityContainer multiBlockEntityContainer) {
            return multiBlockEntityContainer.getControllerBE();
        }
        return blockEntity;
    }

    private boolean isController() {
        if (blockEntity instanceof IMultiBlockEntityContainer multiBlockEntityContainer) {
            return multiBlockEntityContainer.isController();
        }
        return true;
    }

    private void writeNbt(CompoundTag nbt) {
        if (hasCoil()) {
            nbt.put("CoilStack", getCoilStack().save(new CompoundTag()));
            CompoundTag liningTag = new CompoundTag();
            getCoilType().writeToNBT(liningTag);
            nbt.put("CoilType", liningTag);
        } else {
            nbt.remove("CoilStack");
            nbt.remove("CoilType");
        }
    }

    private void readNbt(CompoundTag nbt) {
        if (nbt.contains("CoilStack")) {
            setCoilStack(ItemStack.of(nbt.getCompound("CoilStack")));
            if (nbt.contains("CoilType")) {
                setCoilType(HeatingCoilType.readFromNBT(nbt.getCompound("CoilType")));
            } else {
                setCoilType(null);
            }
        } else {
            setCoilStack(ItemStack.EMPTY);
            setCoilType(null);
        }
    }
}
