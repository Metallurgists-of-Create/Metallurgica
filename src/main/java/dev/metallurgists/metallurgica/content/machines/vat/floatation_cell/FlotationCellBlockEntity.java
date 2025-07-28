package dev.metallurgists.metallurgica.content.machines.vat.floatation_cell;

import com.drmangotea.tfmg.content.machinery.vat.base.IVatMachine;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlock;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import dev.metallurgists.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class FlotationCellBlockEntity extends SmartBlockEntity implements IVatMachine, IHaveGoggleInformation {
    SmartFluidTankBehaviour internalTank;

    LerpedFloat vatFluidHeight;
    VatBlockEntity vatController;

    public FlotationCellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public FluidStack getInternalFluid() {
        return internalTank.getPrimaryHandler().getFluid();
    }

    @Override
    public void tick() {
        if (level == null) return;
        var beAbove = level.getBlockEntity(getBlockPos().above());
        if (beAbove instanceof VatBlockEntity vatBe) {
            var vatController = vatBe.getControllerBE();
            this.vatController = vatController;
            this.vatFluidHeight = vatController.getFluidLevel()[0];
        }
    }

    //@OnlyIn(Dist.CLIENT)
    //private void bubbleParticles() {
    //    if (level == Minecraft.getInstance().level)
    //        if (!FluidShowerBlockEntity.isRenderEntityWithinDistance(getBlockPos()))
    //            return;
    //    ParticleOptions particle = ParticleTypes.BUBBLE;
    //}

    @Override
    public void vatUpdated(VatBlockEntity be) {
        this.vatFluidHeight = be.getFluidLevel()[0];
        this.vatController = be.getControllerBE();
    }

    public int getWorkPercentage() {
        float multiplier = getCatalyst() != null ? getCatalyst().getEfficiencyMultiplier() : 1.0f;
        return (int) (100 * multiplier);
    }

    public void updateVentSize(int vatWidth, BlockPos vatPos) {
    }

    public IVatMachine.PositionRequirement getPositionRequirement() {
        return PositionRequirement.BOTTOM_CENTER;
    }

    @Override
    public String getOperationId() {
        return "metallurgica:flotation_cell";
    }

    @Override
    public boolean canOperate(VatBlockEntity vatBlockEntity) {
        return getCatalyst() != null;
    }

    private FlotationCatalyst getCatalyst() {
        if (getLevel() == null) return null;
        List<FlotationCatalyst> recipes = getLevel().getRecipeManager().getAllRecipesFor(MetallurgicaRecipeTypes.flotation_catalyst.getType());
        return recipes.stream().filter(r -> r.matches(getInternalFluid())).findFirst().orElse(null);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(internalTank = SmartFluidTankBehaviour.single(this, 2000)
                .allowInsertion()
                .forbidExtraction().whenFluidUpdates(this::updateAttachment));
    }

    private void updateAttachment() {
        if (this.hasLevel()) {
            VatBlock.updateVatState(this.getBlockState(), this.getLevel(), this.getBlockPos().relative(Direction.UP));
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (side == Direction.DOWN && isFluidHandlerCap(cap))
            return internalTank.getCapability().cast();

        return super.getCapability(cap, side);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(ForgeCapabilities.FLUID_HANDLER));
    }
}
