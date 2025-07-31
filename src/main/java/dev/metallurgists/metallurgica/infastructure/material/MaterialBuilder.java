package dev.metallurgists.metallurgica.infastructure.material;

import dev.metallurgists.metallurgica.registry.material.MetMaterials;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaRegistries;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class MaterialBuilder<T extends Material, P> extends AbstractBuilder<Material, T, P, MaterialBuilder<T, P>> {

    public static <T extends Material, P> MaterialBuilder<T, P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<Material.Builder, T> factory) {
        return new MaterialBuilder<>(owner, parent, name, callback, factory);
    }

    private final NonNullFunction<Material.Builder, T> factory;

    private NonNullSupplier<Material.Builder> initialBuilder = () -> new Material.Builder(new ResourceLocation(getOwner().getModid(), getName()));
    private NonNullFunction<Material.Builder, Material.Builder> builderCallback = NonNullUnaryOperator.identity();

    public MaterialBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullFunction<Material.Builder, T> factory) {
        super(owner, parent, name, callback, MetallurgicaRegistries.BasicRegistries.MATERIAL_KEY);
        this.factory = factory;
    }

    public MaterialBuilder<T, P> builder(NonNullUnaryOperator<Material.Builder> func) {
        builderCallback = builderCallback.andThen(func);
        return this;
    }

    public MaterialBuilder<T, P> defaultLang() {
        return lang(Material::getDescriptionId);
    }

    public MaterialBuilder<T, P> lang(String name) {
        return lang(Material::getDescriptionId, name);
    }

    @Override
    protected @NonnullType T createEntry() {
        Material.Builder builder = this.initialBuilder.get();
        builder = builderCallback.apply(builder);
        return factory.apply(builder);
    }

    @Override
    protected RegistryEntry<T> createEntryWrapper(RegistryObject<T> delegate) {
        return new MaterialEntry<>(getOwner(), delegate);
    }

    @Override
    public MaterialEntry<T> register() {
        MetMaterials.registeredMaterials.put(new ResourceLocation(getOwner().getModid(), getName()), createEntry());
        return (MaterialEntry<T>) super.register();
    }
}
