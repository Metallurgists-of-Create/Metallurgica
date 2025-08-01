package dev.metallurgists.metallurgica.foundation.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesMixin {
    static {
        MetallurgicaBuiltInRegistries.init();
    }

    @WrapOperation(method = "validate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;forEach(Ljava/util/function/Consumer;)V"))
    private static <T extends Registry<?>> void metallurgica$ourRegistriesAreNotEmpty(Registry<T> instance, Consumer<T> consumer, Operation<Void> original) {
        Consumer<T> callback = (t) -> {
            if (!t.key().location().getNamespace().equals(Metallurgica.ID))
                consumer.accept(t);
        };

        original.call(instance, callback);
    }
}
