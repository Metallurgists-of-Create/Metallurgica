package dev.metallurgists.metallurgica.compat.rutile.flags.objects;

import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.types.IFluidRegistry;
import lombok.Getter;

public class MoltenMetalFluid extends VirtualMaterialFluid {
    @Getter
    public double meltingPoint = 0;

    public MoltenMetalFluid(Properties properties, boolean source, Material material, IFluidRegistry fluidFlag) {
        super(properties, source, material, fluidFlag);
    }

    public static MoltenMetalFluid createSource(Properties properties, Material material, IFluidRegistry fluidFlag) {
        return new MoltenMetalFluid(properties, true, material, fluidFlag);
    }

    public static MoltenMetalFluid createFlowing(Properties properties, Material material, IFluidRegistry fluidFlag) {
        return new MoltenMetalFluid(properties, false, material, fluidFlag);
    }

    public MoltenMetalFluid meltingPoint(double meltingPoint) {
        this.meltingPoint = meltingPoint;
        return this;
    }
}
