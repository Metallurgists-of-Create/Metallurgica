package dev.metallurgists.metallurgica.foundation.block_entity;

import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import net.minecraft.core.Direction;

public interface IMultiBlockEntityEnergyContainer extends IMultiBlockEntityContainer {

    default float getResistance() {
        return 0.0f;
    }

    default int getVoltageGeneration() {
        return 0;
    }

    default int getPowerGeneration() {
        return 0;
    }

    default int getFrequencyGeneration() {
        return 0;
    }

    default int getNetworkResistance() {
        return 0;
    }

    default boolean hasElectricitySlot(Direction direction) {
        return true;
    }
}
