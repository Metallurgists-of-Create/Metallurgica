package dev.metallurgists.metallurgica.content.temperature.hot_plate;

import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.metallurgists.metallurgica.content.temperature.hot_plate.heating_coil.HeatingCoilType;
import dev.metallurgists.metallurgica.content.temperature.hot_plate.heating_coil.behaviour.HeatingCoilBehaviour;
import dev.metallurgists.metallurgica.foundation.block_entity.IMultiBlockEntityEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Objects;

public class HotPlateBlockEntity extends ElectricBlockEntity implements IMultiBlockEntityEnergyContainer {

    private static final int MAX_SIZE = 3;

    protected BlockPos controller;
    protected BlockPos lastKnownPos;
    protected boolean updateConnectivity;
    protected boolean updateCapability;

    HeatingCoilBehaviour heatingCoilBehaviour;

    protected int width;
    protected int height;

    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;
    boolean running = false;

    public HotPlateBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.updateConnectivity = false;
        this.updateCapability = false;
        this.height = 1;
        this.width = 1;
        this.refreshCapability();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(heatingCoilBehaviour = new HeatingCoilBehaviour(this));
    }

    public InteractionResult setOrRemoveCoil(ItemStack stack, Player player) {
        HeatingCoilBehaviour coil = this.getBehaviour(HeatingCoilBehaviour.TYPE);
        if (stack.isEmpty()) {
            if (player.isShiftKeyDown() && coil.getCoilType() != null) {
                ItemStack coilStack = coil.removeCoil();
                if (!coilStack.isEmpty()) {
                    if (!player.getInventory().add(coilStack)) {
                        player.drop(coilStack, false);
                    }
                    return InteractionResult.SUCCESS;
                }
            } else return InteractionResult.PASS;
        }
        HeatingCoilType newType = HeatingCoilType.get(stack.getItem());
        if (newType != null) {
            ItemStack toSave = stack.copyWithCount(1);
            if (coil.getCoilType() != null) {
                if (coil.getCoilType() == newType) {
                    return InteractionResult.PASS; // No change
                } else {
                    ItemStack oldStack = coil.removeCoil();
                    if (!oldStack.isEmpty()) {
                        if (!player.getInventory().add(oldStack)) {
                            player.drop(oldStack, false);
                        }
                        coil.setCoil(toSave);
                        return InteractionResult.SUCCESS;
                    }
                }
            } else {
                if (!player.getAbilities().instabuild) {
                    toSave.shrink(1);
                }
                coil.setCoil(toSave);
                return InteractionResult.SUCCESS;
            }
        }
        sendDataImmediately();
        setChanged();
        return InteractionResult.PASS;
    }

    protected void updateConnectivity() {
        updateConnectivity = false;
        if (level.isClientSide)
            return;
        if (!isController())
            return;
        ConnectivityHandler.formMulti(this);
    }

    @Override
    public void initialize() {
        super.initialize();
        sendData();
        if (level.isClientSide)
            invalidateRenderBoundingBox();
    }

    public void lazyTick() {
        super.lazyTick();
        boolean wasRunning = this.running;

    }

    public void tick() {
        super.tick();
        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync)
                sendData();
        }

        if (lastKnownPos == null)
            lastKnownPos = getBlockPos();
        else if (!lastKnownPos.equals(worldPosition) && worldPosition != null) {
            onPositionChanged();
            return;
        }

        if (updateCapability) {
            updateCapability = false;
            refreshCapability();
        }
        if (updateConnectivity)
            updateConnectivity();
    }

    private void onPositionChanged() {
        this.removeController(true);
        this.lastKnownPos = this.worldPosition;
    }

    @Override
    public BlockPos getController() {
        return this.isController() ? this.worldPosition : this.controller;
    }

    @Override
    public HotPlateBlockEntity getControllerBE() {
        if (this.isController()) {
            return this;
        } else {
            BlockEntity blockEntity = this.level.getBlockEntity(this.controller);
            return blockEntity instanceof HotPlateBlockEntity ? (HotPlateBlockEntity)blockEntity : null;
        }
    }

    @Override
    public boolean isController() {
        return this.controller == null || this.worldPosition.getX() == this.controller.getX() && this.worldPosition.getY() == this.controller.getY() && this.worldPosition.getZ() == this.controller.getZ();
    }

    public void sendDataImmediately() {
        this.syncCooldown = 0;
        this.queuedSync = false;
        this.sendData();
    }

    public void sendData() {
        if (syncCooldown > 0) {
            queuedSync = true;
            return;
        }
        super.sendData();
        queuedSync = false;
        syncCooldown = SYNC_RATE;
    }

    @Override
    public void setController(BlockPos controller) {
        if (!this.level.isClientSide || this.isVirtual()) {
            if (!controller.equals(this.controller)) {
                this.controller = controller;
                this.refreshCapability();
                this.setChanged();
                this.sendData();
            }
        }
    }

    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        BlockPos controllerBefore = this.controller;
        int prevSize = width;
        int prevHeight = height;
        updateConnectivity = compound.contains("Uninitialized");
        controller = null;
        lastKnownPos = null;

        compound.getBoolean("IsRunning");

        if (compound.contains("LastKnownPos")) {
            this.lastKnownPos = NbtUtils.readBlockPos(compound.getCompound("LastKnownPos"));
        }
        if (compound.contains("Controller")) {
            this.controller = NbtUtils.readBlockPos(compound.getCompound("Controller"));
        }

        if (this.isController()) {
            width = compound.getInt("Size");
            height = compound.getInt("Height");
        }

        this.updateCapability = true;

        if (!clientPacket)
            return;

        boolean changeOfController = !Objects.equals(controllerBefore, controller);
        if (changeOfController || prevSize != width || prevHeight != height) {
            if (hasLevel())
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            invalidateRenderBoundingBox();
        }

        if (compound.contains("LazySync")) {

        }
    }

    protected AABB createRenderBoundingBox() {
        return (new AABB(this.getBlockPos())).inflate(3.0F);
    }

    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putBoolean("IsRunning", this.running);

        if (this.updateConnectivity) {
            compound.putBoolean("Uninitialized", true);
        }
        if (isController()) {
            compound.putInt("Size", width);
            compound.putInt("Height", height);
        }
        if (this.lastKnownPos != null) {
            compound.put("LastKnownPos", NbtUtils.writeBlockPos(this.lastKnownPos));
        }

        if (!this.isController()) {
            compound.put("Controller", NbtUtils.writeBlockPos(this.controller));
        }

        super.write(compound, clientPacket);

        if (!clientPacket)
            return;
        if (queuedSync)
            compound.putBoolean("LazySync", true);
    }

    @Override
    public void destroy() {
        super.destroy();
    }


    @Override
    public void removeController(boolean keepContents) {
        if (!this.level.isClientSide) {
            this.updateConnectivity = true;
            if (!keepContents) {
                //this.applyFluidTankSize(1);
            }

            this.controller = null;
            this.width = 1;
            this.height = 1;
            this.setChanged();
            this.refreshCapability();
            this.sendData();
        }
    }

    private void refreshCapability() {
        //LazyOptional<IFluidHandler> oldCap = this.fluidCapability;
        //this.fluidCapability = LazyOptional.of(this::handlerForCapability);
        //oldCap.invalidate();
    }

    @Override
    public BlockPos getLastKnownPos() {
        return this.lastKnownPos;
    }

    public static int getMaxHeight() {
        return 1;
    }

    @Override
    public void preventConnectivityUpdate() {
        this.updateConnectivity = false;
    }

    @Override
    public void notifyMultiUpdated() {
        BlockState state = this.getBlockState();

        this.setChanged();
    }

    @Override
    public Direction.Axis getMainConnectionAxis() {
        return Direction.Axis.Y;
    }

    @Override
    public int getMaxLength(Direction.Axis longAxis, int width) {
        return longAxis == Direction.Axis.Y ? getMaxHeight() : this.getMaxWidth();
    }

    @Override
    public int getMaxWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction != Direction.UP && direction != Direction.DOWN;
    }
}
