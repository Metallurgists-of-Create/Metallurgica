package dev.metallurgists.metallurgica.compat.rutile.materials;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.rutile.element.MetallurgicaElements;
import dev.metallurgists.metallurgica.compat.rutile.flags.MetallurgicaFlagKeys;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.block.CasingFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.block.CogWheelFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.block.LargeCogWheelFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.block.SheetmetalFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.fluid.MoltenFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.item.*;
import dev.metallurgists.metallurgica.registry.MetallurgicaTags;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.registry.RutileElements;
import dev.metallurgists.rutile.registry.RutileFlagKeys;
import dev.metallurgists.rutile.registry.RutileMaterials;
import dev.metallurgists.rutile.registry.flags.DustFlag;
import dev.metallurgists.rutile.registry.flags.IngotFlag;
import dev.metallurgists.rutile.registry.flags.NuggetFlag;
import dev.metallurgists.rutile.registry.flags.StorageBlockFlag;

import static dev.metallurgists.metallurgica.compat.rutile.materials.MetallurgicaMaterials.*;
import static dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey.*;
import static dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey.STORAGE_BLOCK;

public class MetalMaterials {

    // Damn there's a lot to modify. I sure hope nothing goes wrong.
    public static void modify() {
        // Iron
        RutileMaterials.Iron.setFlag(MetallurgicaFlagKeys.SHEET, new SheetFlag("create"));
        RutileMaterials.Iron.setFlag(MetallurgicaFlagKeys.MOLTEN, new MoltenFlag(1538.0));

        // Gold
        RutileMaterials.Gold.setNameAlternative(MetallurgicaFlagKeys.SHEET, "golden");
        RutileMaterials.Gold.setFlag(MetallurgicaFlagKeys.SHEET, new SheetFlag("create"));
        RutileMaterials.Gold.setFlag(MetallurgicaFlagKeys.MOLTEN, new MoltenFlag(1064.2));
        RutileMaterials.Gold.setFlag(RutileFlagKeys.DUST, new DustFlag());
        RutileMaterials.Gold.setFlag(MetallurgicaFlagKeys.MINERAL, new MineralFlag(true));
        RutileMaterials.Gold.setFlag(MetallurgicaFlagKeys.RUBBLE, new RubbleFlag().crushing().bonusChance(0.15f));

        //Copper
        RutileMaterials.Copper.setFlag(RutileFlagKeys.NUGGET, new NuggetFlag("create"));
        RutileMaterials.Copper.setFlag(MetallurgicaFlagKeys.SHEET, new SheetFlag("create"));
        RutileMaterials.Copper.setFlag(MetallurgicaFlagKeys.MOLTEN, new MoltenFlag(1084.6));
        RutileMaterials.Copper.setFlag(RutileFlagKeys.DUST, new DustFlag());
        RutileMaterials.Copper.setFlag(MetallurgicaFlagKeys.CASING, new CasingFlag("create"));
        RutileMaterials.Copper.setFlag(MetallurgicaFlagKeys.WIRE, new WireFlag("tfmg"));
        RutileMaterials.Copper.setFlag(MetallurgicaFlagKeys.SPOOL, new SpoolFlag("tfmg"));
        RutileMaterials.Copper.setFlag(MetallurgicaFlagKeys.MINERAL, new MineralFlag(true));
        RutileMaterials.Copper.setFlag(MetallurgicaFlagKeys.RUBBLE, new RubbleFlag().crushing().bonusChance(0.15f));
        RutileMaterials.Copper.setFlag(MetallurgicaFlagKeys.COG_WHEEL, new CogWheelFlag());
        RutileMaterials.Copper.setFlag(MetallurgicaFlagKeys.LARGE_COG_WHEEL, new LargeCogWheelFlag());

    }

    public static void register() {

    }

    public static final Material Netherium = new Material.Builder(Metallurgica.asResource("netherium"))
            .element(MetallurgicaElements.NETHERIUM)
                .meltingPoint(3962.0)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new WireFlag(),
                        new MoltenFlag(3962.0),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Aluminum = new Material.Builder(Metallurgica.asResource("aluminum"))
            .element(RutileElements.ALUMINUM)
                .existingIds(MetallurgicaFlagKeys.CASING, "tfmg:industrial_aluminum_casing")
                .meltingPoint(660.3)
                .addFlags(
                        new NuggetFlag("tfmg"),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new SheetFlag("tfmg"),
                        new MoltenFlag(660.3),
                        new DustFlag(),
                        new CasingFlag("tfmg"),
                        new SheetmetalFlag(),
                        new WireFlag("tfmg"),
                        new SpoolFlag("tfmg"),
                        new CogWheelFlag("tfmg"), new LargeCogWheelFlag("tfmg")
                ).buildAndRegister();

    public static final Material Scandium = new Material.Builder(Metallurgica.asResource("scandium"))
            .element(RutileElements.SCANDIUM)
                .meltingPoint(1541.0)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(1541.0),
                        new DustFlag(),
                        new SpoolFlag(0.0124, 0xedebd8)
                ).buildAndRegister();

    public static final Material Lead = new Material.Builder(Metallurgica.asResource("lead"))
            .element(RutileElements.LEAD)
                .meltingPoint(327.5)
                .addFlags(
                        new NuggetFlag("tfmg"),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new SheetFlag("tfmg"),
                        new MoltenFlag(327.5),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Silver = new Material.Builder(Metallurgica.asResource("silver"))
            .element(RutileElements.SILVER)
                .meltingPoint(961.8)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(961.8),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Nickel = new Material.Builder(Metallurgica.asResource("nickel"))
            .element(RutileElements.NICKEL)
                .meltingPoint(1455.0)
                .addFlags(
                        new NuggetFlag("tfmg"),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new SheetFlag("tfmg"),
                        new MoltenFlag(1455.0),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Tin = new Material.Builder(Metallurgica.asResource("tin"))
            .element(RutileElements.TIN)
                .meltingPoint(231.9)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(231.9),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Zinc = new Material.Builder(Metallurgica.asResource("zinc"))
            .element(RutileElements.ZINC)
                .meltingPoint(419.5)
                .addFlags(
                        new NuggetFlag("create"),
                        new IngotFlag("create"),
                        new StorageBlockFlag("create"),
                        new SheetFlag(),
                        new MoltenFlag(419.5),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Platinum = new Material.Builder(Metallurgica.asResource("platinum"))
            .element(RutileElements.PLATINUM)
                .meltingPoint(1768.3)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(1768.3),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Titanium = new Material.Builder(Metallurgica.asResource("titanium"))
            .element(RutileElements.TITANIUM)
                .meltingPoint(1668.0)
                .addFlags(
                        new NuggetFlag().requiresCompacting(),
                        new IngotFlag().requiresCompacting(),
                        new StorageBlockFlag().requiresDecompacting(),
                        new SheetFlag().pressTimes(3),
                        new MoltenFlag(1668.0),
                        new DustFlag(),
                        new SheetmetalFlag().requiresCompacting()
                ).buildAndRegister();

    public static final Material Uranium = new Material.Builder(Metallurgica.asResource("uranium"))
            .element(RutileElements.URANIUM)
                .meltingPoint(1132.3)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(1132.3),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Lithium = new Material.Builder(Metallurgica.asResource("lithium"))
            .element(RutileElements.LITHIUM)
                .meltingPoint(180.5)
                .addFlags(
                        new NuggetFlag("tfmg"),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new SheetFlag(),
                        new MoltenFlag(180.5),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Magnesium = new Material.Builder(Metallurgica.asResource("magnesium"))
            .element(RutileElements.MAGNESIUM)
                .meltingPoint(650.0)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(650.0),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Tungsten = new Material.Builder(Metallurgica.asResource("tungsten"))
            .element(RutileElements.TUNGSTEN)
                .meltingPoint(3422.0)
                .addFlags(
                        new NuggetFlag().requiresCompacting(),
                        new IngotFlag().requiresCompacting(),
                        new StorageBlockFlag().requiresDecompacting(),
                        new SheetFlag().pressTimes(4),
                        new MoltenFlag(3422.0),
                        new DustFlag(),
                        new SheetmetalFlag().requiresCompacting(),
                        new CogWheelFlag().variant("industrial"),
                        new LargeCogWheelFlag().variant("industrial")
                ).buildAndRegister();

    public static final Material Osmium = new Material.Builder(Metallurgica.asResource("osmium"))
            .element(RutileElements.OSMIUM)
                .meltingPoint(3033.0)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(3033.0),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Thorium = new Material.Builder(Metallurgica.asResource("thorium"))
            .element(RutileElements.THORIUM)
                .meltingPoint(1750.0)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(1750.0),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Tantalum = new Material.Builder(Metallurgica.asResource("tantalum"))
            .element(RutileElements.TANTALUM)
                .meltingPoint(3020.0)
                .addFlags(
                        new NuggetFlag().requiresCompacting(),
                        new IngotFlag().requiresCompacting(),
                        new StorageBlockFlag().requiresDecompacting(),
                        new SheetFlag().pressTimes(2),
                        new MoltenFlag(3020.0),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Sodium = new Material.Builder(Metallurgica.asResource("sodium"))
            .element(RutileElements.SODIUM)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag().useColumnModel(),
                        new CasingFlag().appliesOn(MetallurgicaTags.forgeItemTag("storage_blocks/plastic")),
            new SheetFlag(),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Chromium = new Material.Builder(Metallurgica.asResource("chromium"))
            .element(RutileElements.CHROMIUM)
                .meltingPoint(1907.0)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(1907.0),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Vanadium = new Material.Builder(Metallurgica.asResource("vanadium"))
            .element(RutileElements.VANADIUM)
                .meltingPoint(1910.0)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(1910.0),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Manganese = new Material.Builder(Metallurgica.asResource("manganese"))
            .element(RutileElements.MANGANESE)
                .meltingPoint(1246.0)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(1246.0),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Potassium = new Material.Builder(Metallurgica.asResource("potassium"))
            .element(RutileElements.POTASSIUM)
                .meltingPoint(63.5)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new SheetFlag(),
                        new MoltenFlag(63.5),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Beryllium = new Material.Builder(Metallurgica.asResource("beryllium"))
            .element(RutileElements.BERYLLIUM)
                .meltingPoint(1287.0)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new SheetFlag(),
                        new MoltenFlag(1287.0),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Radium = new Material.Builder(Metallurgica.asResource("radium"))
            .element(RutileElements.RADAIUM)
                .meltingPoint(700.00)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(700.00),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Arsenic = new Material.Builder(Metallurgica.asResource("arsenic"))
            .element(RutileElements.ARSENIC)
                .addFlags(
                        new IngotFlag()
                ).buildAndRegister();

    public static final Material Cadmium = new Material.Builder(Metallurgica.asResource("cadmium"))
            .element(RutileElements.CADMIUM)
                .meltingPoint(321.07)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new SheetFlag(),
                        new MoltenFlag(321.07),
                        new DustFlag()
                ).buildAndRegister();
}
