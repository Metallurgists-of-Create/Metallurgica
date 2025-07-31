package dev.metallurgists.metallurgica.content.temperature.hot_plate.heating_coil;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.metallurgists.metallurgica.registry.MetallurgicaPartialModels;
import net.createmod.catnip.data.Couple;

import java.util.Map;

public class SimpleHeatingCoil extends HeatingCoilType {
    private final double temperatureCapacity;

    private Couple<PartialModel> largeCoil, mediumCoil, smallCoil;

    public SimpleHeatingCoil(double temperatureCapacity) {
        this.temperatureCapacity = temperatureCapacity;
    }

    public SimpleHeatingCoil withModels(Couple<PartialModel> largeCoil, Couple<PartialModel> mediumCoil, Couple<PartialModel> smallCoil) {
        this.largeCoil = largeCoil;
        this.mediumCoil = mediumCoil;
        this.smallCoil = smallCoil;
        return this;
    }

    public SimpleHeatingCoil withModels(Map<MetallurgicaPartialModels.Size, Couple<PartialModel>> sizesMap) {
        this.largeCoil = sizesMap.get(MetallurgicaPartialModels.Size.LARGE);
        this.mediumCoil = sizesMap.get(MetallurgicaPartialModels.Size.MEDIUM);
        this.smallCoil = sizesMap.get(MetallurgicaPartialModels.Size.SMALL);
        return this;
    }

    @Override
    public double temperatureCapacity() {
        return this.temperatureCapacity;
    }

    @Override
    public Couple<PartialModel> largeCoil() {
        return this.largeCoil;
    }

    @Override
    public Couple<PartialModel> mediumCoil() {
        return this.mediumCoil;
    }

    @Override
    public Couple<PartialModel> smallCoil() {
        return this.smallCoil;
    }

}
