package dev.metallurgists.metallurgica.compat.rutile.materials;

import dev.metallurgists.metallurgica.Metallurgica;

import dev.metallurgists.metallurgica.compat.rutile.flags.types.fluid.LiquidFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.fluid.MoltenFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.item.MineralFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.item.SheetFlag;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.registry.RutileElements;
import dev.metallurgists.rutile.registry.RutileFlagKeys;
import dev.metallurgists.rutile.registry.RutileMaterials;
import dev.metallurgists.rutile.registry.flags.*;

import static dev.metallurgists.metallurgica.compat.rutile.materials.MetallurgicaMaterials.*;
import static dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey.GEM;


public class NonMetalMaterials {

    public static void modify() {
        //Quartz
        RutileMaterials.Quartz.setFlag(RutileFlagKeys.DUST, new DustFlag());

        //Diamond
        RutileMaterials.Diamond.setFlag(RutileFlagKeys.DUST, new DustFlag());
        RutileMaterials.Diamond.setFlag(RutileFlagKeys.NUGGET, new NuggetFlag(true));
    }

    public static void register() {


    }

    public static final Material Silicon = new Material.Builder(Metallurgica.asResource("silicon"))
            .element(RutileElements.SILICON)
                .meltingPoint(1414.0)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag().useColumnModel(),
                        new SheetFlag(),
                        new MoltenFlag(1414.0),
                        new DustFlag(),
                        new LiquidFlag("tfmg")
                ).buildAndRegister();

    public static final Material Sulfur = new Material.Builder(Metallurgica.asResource("sulfur"))
            .element(RutileElements.SULFUR)
                .addFlags(
                        new DustFlag("tfmg", false)
                ).buildAndRegister();

    // Carbon Allotropes

    public static final Material Graphite = new Material.Builder(Metallurgica.asResource("graphite"))
            .element(RutileElements.CARBON)
                .meltingPoint(3652.0)
                .addFlags(
                        new MineralFlag(),
                        new MoltenFlag(3652.0),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material CoalCoke = new Material.Builder(Metallurgica.asResource("coal_coke"))
            .element(RutileElements.CARBON)
                .existingIds(RutileFlagKeys.GEM, "tfmg:coal_coke")
                .addFlags(
                        new StorageBlockFlag("tfmg"),
                        new GemFlag("tfmg"),
                        new DustFlag("tfmg", false)
                ).buildAndRegister();

    public static final Material Coal = new Material.Builder(Metallurgica.asResource("coal"))
            .element(RutileElements.CARBON)
                .existingIds(RutileFlagKeys.GEM, "minecraft:coal")
                .addFlags(
                        new GemFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Charcoal = new Material.Builder(Metallurgica.asResource("charcoal"))
            .element(RutileElements.CARBON)
                .existingIds(RutileFlagKeys.GEM, "minecraft:charcoal")
                .addFlags(
                        new GemFlag("minecraft"),
                        new DustFlag()
                ).buildAndRegister();
}
