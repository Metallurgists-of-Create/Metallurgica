package dev.metallurgists.metallurgica.infastructure.material.registry.flags.fluid;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.fluid.IMaterialFluid;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.FluidFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.ISpecialAssetLocation;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.ISpecialLangSuffix;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.MaterialFlags;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IFluidRegistry;
import com.tterrag.registrate.util.entry.FluidEntry;
import org.jetbrains.annotations.NotNull;

public class LiquidFlag extends FluidFlag implements ISpecialAssetLocation, ISpecialLangSuffix {

    public LiquidFlag(String existingNamespace) {
        super("%s", existingNamespace);
    }

    public LiquidFlag() {
        this("metallurgica");
    }

    @Override
    public FluidEntry<? extends IMaterialFluid> registerFluid(@NotNull Material material, IFluidRegistry flag, @NotNull MetallurgicaRegistrate registrate) {
        return null; //registrate.materialVirtualFluid(this.getIdPattern().formatted(material.getName()), Metallurgica.asResource("fluid/thin_fluid_still"), Metallurgica.asResource("fluid/thin_fluid_flow"), material, flag).register();
    }

    @Override
    public FlagKey<?> getKey() {
        return FlagKey.LIQUID;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    @Override
    public String getAssetName() {
        return "liquid";
    }

    @Override
    public String getLangSuffix() {
        return "liquid";
    }
}
