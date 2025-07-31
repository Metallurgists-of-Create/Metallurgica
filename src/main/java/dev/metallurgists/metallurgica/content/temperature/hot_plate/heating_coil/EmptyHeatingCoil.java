package dev.metallurgists.metallurgica.content.temperature.hot_plate.heating_coil;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Couple;

public class EmptyHeatingCoil extends HeatingCoilType{
    @Override
    public double temperatureCapacity() {
        return 0;
    }

    @Override
    public Couple<PartialModel> largeCoil() {
        return null;
    }

    @Override
    public Couple<PartialModel> mediumCoil() {
        return null;
    }

    @Override
    public Couple<PartialModel> smallCoil() {
        return null;
    }
}
