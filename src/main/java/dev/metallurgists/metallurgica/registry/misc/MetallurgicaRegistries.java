package dev.metallurgists.metallurgica.registry.misc;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.element.Element;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.HashMap;
import java.util.Map;

public class MetallurgicaRegistries {
    //public static final MetallurgicaRegistry.RL<Conductor> CONDUCTOR = new MetallurgicaRegistry.RL<>(Metallurgica.asResource("conductor"));
    public static final Map<ResourceLocation, Element> registeredElements = new HashMap<>();

    public static final ResourceKey<Registry<Element>> ELEMENT_KEY = Metallurgica.registrate().makeRegistry("element", () -> new RegistryBuilder<Element>().hasTags().allowModification().setDefaultKey(Metallurgica.asResource("null")));
    public static final ResourceKey<Registry<Material>> MATERIAL_KEY = Metallurgica.registrate().makeRegistry("material", () -> new RegistryBuilder<Material>().hasTags().setDefaultKey(Metallurgica.asResource("null")));


    public static void register() {
    }
}
