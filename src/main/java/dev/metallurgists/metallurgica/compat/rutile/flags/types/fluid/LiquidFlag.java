package dev.metallurgists.metallurgica.compat.rutile.flags.types.fluid;


import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.VirtualFluidBuilder;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.rutile.flags.MetallurgicaFlagKeys;
import dev.metallurgists.metallurgica.compat.rutile.flags.objects.VirtualMaterialFluid;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.FluidFlag;
import dev.metallurgists.rutile.api.material.flag.types.IFluidRegistry;
import dev.metallurgists.rutile.api.material.registry.fluid.IMaterialFluid;
import dev.metallurgists.rutile.api.material.registry.fluid.MaterialFluidType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class LiquidFlag extends FluidFlag {

    public LiquidFlag(String existingNamespace) {
        super("%s", existingNamespace);
    }

    public LiquidFlag() {
        this("");
    }

    @Override
    public FlagKey<?> getKey() {
        return MetallurgicaFlagKeys.LIQUID;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    @Override
    public FluidEntry<? extends IMaterialFluid> registerFluid(@NotNull Material material, IFluidRegistry flag, @NotNull AbstractRegistrate<?> registrate) {
        return materialVirtualFluid(this.getIdPattern().formatted(material.getName()), Metallurgica.asResource("fluid/thin_fluid_still"), Metallurgica.asResource("fluid/thin_fluid_flow"), material, flag, registrate)
                .register();
    }

    @ApiStatus.Internal
    private FluidBuilder<VirtualMaterialFluid, AbstractRegistrate<?>> materialVirtualFluid(String name, ResourceLocation still, ResourceLocation flow, Material material, IFluidRegistry flag, AbstractRegistrate<?> registrate) {
        return registrate.entry(name, c -> new VirtualFluidBuilder<>(registrate, registrate, name, c, still, flow, MaterialFluidType.create(material, flag), (p) -> VirtualMaterialFluid.createSource(p, material, flag), (p) -> VirtualMaterialFluid.createFlowing(p, material, flag)));
    }
}
