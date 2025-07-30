package dev.metallurgists.metallurgica.registry.misc;

import dev.metallurgists.metallurgica.infastructure.element.Element;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class MetallurgicaRegistries {
    //public static final MetallurgicaRegistry.RL<Conductor> CONDUCTOR = new MetallurgicaRegistry.RL<>(Metallurgica.asResource("conductor"));
    public static final Map<ResourceLocation, Element> registeredElements = new HashMap<>();


    public static void register() {
    }
}
