package dev.metallurgists.metallurgica.registry.misc;

import com.mojang.serialization.Lifecycle;
import com.simibubi.create.impl.registry.MappedRegistryWithFreezeCallback;
import dev.metallurgists.metallurgica.content.temperature.hot_plate.heating_coil.HeatingCoilType;
import dev.metallurgists.metallurgica.foundation.mixin.accessor.BuiltInRegistriesAccessor;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

public class MetallurgicaBuiltInRegistries {
    public static final Registry<HeatingCoilType> HEATING_COIL_TYPE = simple(MetallurgicaRegistries.HEATING_COIL_TYPE);

    private static <T> Registry<T> simple(ResourceKey<Registry<T>> key) {
        return register(key, new MappedRegistry<>(key, Lifecycle.stable(), false));
    }

    private static <T> Registry<T> simpleWithFreezeCallback(ResourceKey<Registry<T>> key, Runnable freezeCallback) {
        return register(key, new MappedRegistryWithFreezeCallback<>(key, Lifecycle.stable(), freezeCallback));
    }

    private static <T> Registry<T> withIntrusiveHolders(ResourceKey<Registry<T>> key) {
        return register(key, new MappedRegistry<>(key, Lifecycle.stable(), true));
    }

    @SuppressWarnings("unchecked")
    private static <T> Registry<T> register(ResourceKey<Registry<T>> key, WritableRegistry<T> registry) {
        BuiltInRegistriesAccessor.metallurgica$getWRITABLE_REGISTRY().register(
                (ResourceKey<WritableRegistry<?>>) (Object) key, registry, Lifecycle.stable()
        );
        return registry;
    }

    @ApiStatus.Internal
    public static void init() {
        // make sure the class is loaded.
        // this method is called at the tail of BuiltInRegistries, injected by BuiltInRegistriesMixin.
    }
}
