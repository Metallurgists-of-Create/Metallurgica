package dev.metallurgists.metallurgica.registry.misc;

import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.content.temperature.hot_plate.heating_coil.HeatingCoilType;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.element.Element;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.HashMap;
import java.util.Map;

public class MetallurgicaRegistries {

    public static final ResourceKey<Registry<HeatingCoilType>> HEATING_COIL_TYPE = key("heating_coil_type");

    private static <T> ResourceKey<Registry<T>> key(String name) {
        return ResourceKey.createRegistryKey(Metallurgica.asResource(name));
    }

    public static class BasicRegistries {
        public static final Map<ResourceLocation, Element> registeredElements = new HashMap<>();

        public static final ResourceKey<Registry<Element>> ELEMENT_KEY = Metallurgica.registrate().makeRegistry("element", () -> new RegistryBuilder<Element>().hasTags().allowModification().setDefaultKey(Metallurgica.asResource("null")));
        public static final ResourceKey<Registry<Material>> MATERIAL_KEY = Metallurgica.registrate().makeRegistry("material", () -> new RegistryBuilder<Material>().hasTags().setDefaultKey(Metallurgica.asResource("null")));

        public static void register() {
        }
    }
}
