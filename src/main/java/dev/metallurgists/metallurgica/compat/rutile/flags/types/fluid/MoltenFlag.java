package dev.metallurgists.metallurgica.compat.rutile.flags.types.fluid;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.VirtualFluidBuilder;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.rutile.flags.MetallurgicaFlagKeys;
import dev.metallurgists.metallurgica.compat.rutile.flags.objects.MoltenMetalFluid;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.FluidFlag;
import dev.metallurgists.rutile.api.material.flag.types.IFluidRegistry;
import dev.metallurgists.rutile.api.material.registry.fluid.IMaterialFluid;
import dev.metallurgists.rutile.api.material.registry.fluid.MaterialBucketItem;
import dev.metallurgists.rutile.api.material.registry.fluid.MaterialFluidType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class MoltenFlag extends FluidFlag {
    public double meltingPoint;

    public MoltenFlag(String existingNamespace) {
        super("molten_%s", existingNamespace);
        this.meltingPoint = 100; // Default melting point, can be overridden
    }

    public MoltenFlag(double meltingPoint) {
        this("");
        this.meltingPoint = meltingPoint;
    }

    @Override
    public FlagKey<?> getKey() {
        return MetallurgicaFlagKeys.MOLTEN;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    @Override
    public FluidEntry<? extends IMaterialFluid> registerFluid(@NotNull Material material, IFluidRegistry flag, @NotNull AbstractRegistrate<?> registrate) {
        return moltenMetal(this.getIdPattern().formatted(material.getName()), material, flag, meltingPoint, registrate)
                .bucket((sup, p) -> new MaterialBucketItem(sup, p, material, flag))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .build()
                .register();
    }

    @ApiStatus.Internal
    private FluidBuilder<MoltenMetalFluid, AbstractRegistrate<?>> moltenMetal(String name, Material material, IFluidRegistry flag, double moltenTemperature, AbstractRegistrate<?> registrate) {
        ResourceLocation still = Metallurgica.asResource("fluid/molten_metal_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/molten_metal_flow");
        return registrate.entry(name, c -> new VirtualFluidBuilder<>(registrate, registrate, name, c, still, flow,
                MaterialFluidType.create(material, flag, false),
                (p) -> MoltenMetalFluid.createSource(p, material, flag).meltingPoint(moltenTemperature),
                (p) -> MoltenMetalFluid.createFlowing(p, material, flag).meltingPoint(moltenTemperature)));
    }
}
