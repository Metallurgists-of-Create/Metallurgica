package dev.metallurgists.metallurgica.content.temperature.hot_plate.heating_coil;

import com.simibubi.create.api.registry.SimpleRegistry;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaBuiltInRegistries;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaRegistries;
import net.createmod.catnip.data.Couple;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public abstract class HeatingCoilType {
    public static final SimpleRegistry<Item, HeatingCoilType> BY_ITEM = SimpleRegistry.create();

    public final ResourceLocation getId() {
        return MetallurgicaBuiltInRegistries.HEATING_COIL_TYPE.getKey(this);
    }

    public static <T extends Item, P> NonNullUnaryOperator<ItemBuilder<T, P>> heatingCoil(RegistryEntry<? extends HeatingCoilType> coilType) {
        return builder -> builder.onRegisterAfter(MetallurgicaRegistries.HEATING_COIL_TYPE, item -> BY_ITEM.register(item, coilType.get()));
    }

    public abstract double temperatureCapacity();

    // Partial Models for rendering
    @OnlyIn(Dist.CLIENT)
    public abstract Couple<PartialModel> largeCoil();
    @OnlyIn(Dist.CLIENT)
    public abstract Couple<PartialModel> mediumCoil();
    @OnlyIn(Dist.CLIENT)
    public abstract Couple<PartialModel> smallCoil();

    @Nullable
    public static HeatingCoilType get(@Nullable ResourceLocation id) {
        if (id == null)
            return null;
        return MetallurgicaBuiltInRegistries.HEATING_COIL_TYPE.get(id);
    }

    public static HeatingCoilType get(Item item) {
        return BY_ITEM.get(item);
    }

    public void writeToNBT(CompoundTag tag) {
        ResourceLocation id = getId();
        tag.putString("id", id.toString());
    }

    public static HeatingCoilType readFromNBT(CompoundTag tag) {
        String id = tag.getString("id");
        return MetallurgicaBuiltInRegistries.HEATING_COIL_TYPE.get(ResourceLocation.tryParse(id));
    }
}
