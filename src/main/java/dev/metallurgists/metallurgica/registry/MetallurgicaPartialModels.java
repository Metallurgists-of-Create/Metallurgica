package dev.metallurgists.metallurgica.registry;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.metallurgists.metallurgica.content.temperature.hot_plate.heating_coil.HeatingCoilType;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaHeatingCoils;
import net.createmod.catnip.data.Couple;
import net.minecraft.util.StringRepresentable;

import java.util.Map;

public class MetallurgicaPartialModels {
    public static final PartialModel

            channelSide = block("channel/block_side"),
            shakerPlatform = block("shaking_table/platform"),
            ceramicMixerStirrer = block("ceramic_mixer/stirrer"),
            workedMetal = block("metalworking/worked_metal"),
            smallNozzle = block("nozzle/small"),
            mediumNozzle = block("nozzle/medium"),
            largeNozzle = block("nozzle/large")
            ;

    public static final Map<Size, Couple<PartialModel>> nickelHeatingCoil = heatingCoil("nickel");
    
    private static PartialModel block(String path) {
        return PartialModel.of(Metallurgica.asResource("block/" + path));
    }

    private static Map<Size, Couple<PartialModel>> heatingCoil(String type) {
        return Map.of(
                Size.SMALL, heatingCoil(type, Size.SMALL),
                Size.MEDIUM, heatingCoil(type, Size.MEDIUM),
                Size.LARGE, heatingCoil(type, Size.LARGE)
        );
    }

    private static Couple<PartialModel> heatingCoil(String type, Size size) {
        return Couple.create(
                heatingCoil(type, size, false),
                heatingCoil(type, size, true)
        );
    }

    private static PartialModel heatingCoil(String type, Size size, boolean border) {
        String prefix = "block/heating_coil/" + type + "/";
        String suffix = border ? "_border" : "";
        return PartialModel.of(Metallurgica.asResource(prefix + size.getSerializedName() + suffix));
    }
    
    public static void init() {
        // init static fields
    }

    public enum Size implements StringRepresentable {
        SMALL,
        MEDIUM,
        LARGE
        ;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}
