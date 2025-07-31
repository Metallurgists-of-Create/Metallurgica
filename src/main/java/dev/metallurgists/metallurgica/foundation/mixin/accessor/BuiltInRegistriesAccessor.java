package dev.metallurgists.metallurgica.foundation.mixin.accessor;

import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BuiltInRegistries.class)
public interface BuiltInRegistriesAccessor {
    @Accessor("WRITABLE_REGISTRY")
    static WritableRegistry<WritableRegistry<?>> metallurgica$getWRITABLE_REGISTRY() {
        throw new AssertionError();
    }
}
