package dev.metallurgists.metallurgica.registry.misc;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.content.temperature.hot_plate.heating_coil.HeatingCoilType;
import dev.metallurgists.metallurgica.content.temperature.hot_plate.heating_coil.SimpleHeatingCoil;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.registry.MetallurgicaPartialModels;

import java.util.function.Supplier;

public class MetallurgicaHeatingCoils {
    private static final MetallurgicaRegistrate REGISTRATE = Metallurgica.registrate();

    public static final RegistryEntry<SimpleHeatingCoil> NICKEL = simple("nickel", () -> new SimpleHeatingCoil(1455).withModels(MetallurgicaPartialModels.nickelHeatingCoil));

    private static <T extends HeatingCoilType> RegistryEntry<T> simple(String name, Supplier<T> supplier) {
        return REGISTRATE.heatingCoil(name, supplier).register();
    }

    public static void register() {
    }
}
