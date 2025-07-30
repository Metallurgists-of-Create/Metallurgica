package dev.metallurgists.metallurgica.compat.rutile.flags.objects;

import com.simibubi.create.content.fluids.VirtualFluid;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.types.IFluidRegistry;
import dev.metallurgists.rutile.api.material.registry.fluid.IMaterialFluid;

public class VirtualMaterialFluid extends VirtualFluid implements IMaterialFluid {
    public final Material material;
    public final IFluidRegistry fluidFlag;

    public VirtualMaterialFluid(Properties properties, boolean source, Material material, IFluidRegistry fluidFlag) {
        super(properties, source);
        this.material = material;
        this.fluidFlag = fluidFlag;
    }

    public static VirtualMaterialFluid createSource(Properties properties, Material material, IFluidRegistry fluidFlag) {
        return new VirtualMaterialFluid(properties, true, material, fluidFlag);
    }

    public static VirtualMaterialFluid createFlowing(Properties properties, Material material, IFluidRegistry fluidFlag) {
        return new VirtualMaterialFluid(properties, false, material, fluidFlag);
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public IFluidRegistry getFlag() {
        return this.fluidFlag;
    }
}
