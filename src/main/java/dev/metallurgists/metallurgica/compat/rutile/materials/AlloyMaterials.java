package dev.metallurgists.metallurgica.compat.rutile.materials;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.rutile.element.MetallurgicaElements;
import dev.metallurgists.metallurgica.compat.rutile.flags.MetallurgicaFlagKeys;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.block.CasingFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.block.CogWheelFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.block.LargeCogWheelFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.block.SheetmetalFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.fluid.MoltenFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.item.SemiPressedSheetFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.item.SheetFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.item.SpoolFlag;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.item.WireFlag;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.registry.RutileElements;
import dev.metallurgists.rutile.registry.flags.DustFlag;
import dev.metallurgists.rutile.registry.flags.IngotFlag;
import dev.metallurgists.rutile.registry.flags.NuggetFlag;
import dev.metallurgists.rutile.registry.flags.StorageBlockFlag;

import static dev.metallurgists.metallurgica.compat.rutile.materials.MetallurgicaMaterials.*;

public class AlloyMaterials {

    public static void modify() {

    }

    public static void register() {

    }

    public static final Material TitaniumAluminide = new Material.Builder(Metallurgica.asResource("titanium_aluminide"))
            .composition(RutileElements.TITANIUM, 2, RutileElements.ALUMINUM, 1)
                .meltingPoint(1447.0)
                .addFlags(
                        new NuggetFlag().requiresCompacting(),
                        new IngotFlag().requiresCompacting(),
                        new StorageBlockFlag().requiresDecompacting(),
                        new SheetFlag().pressTimes(3),
                        new MoltenFlag(1447.0),
                        new DustFlag(),
                        new SheetmetalFlag().requiresCompacting()
                ).buildAndRegister();

    public static final Material Netherite = new Material.Builder(Metallurgica.asResource("netherite"))
            .composition(MetallurgicaElements.NETHERIUM, 1, RutileElements.GOLD, 1)
                .meltingPoint(3562.0)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new SheetFlag().pressTimes(2),
                        new MoltenFlag(3562.0)
                ).buildAndRegister();

    public static final Material Brass = new Material.Builder(Metallurgica.asResource("brass"))
            .composition(RutileElements.COPPER, 3, RutileElements.ZINC, 1)
                .meltingPoint(920.0)
                .addFlags(
                        new NuggetFlag("create"),
                        new IngotFlag("create"),
                        new StorageBlockFlag("create"),
                        new SheetFlag("create"),
                        new CasingFlag("create"),
                        new MoltenFlag(920.0),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Bronze = new Material.Builder(Metallurgica.asResource("bronze"))
            .composition(RutileElements.COPPER, 7, RutileElements.TIN, 2)
                .meltingPoint(950.0)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(950.0),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material ArsenicalBronze = new Material.Builder(Metallurgica.asResource("arsenical_bronze"))
            .composition(RutileElements.COPPER, 4, RutileElements.TIN, 1, RutileElements.ARSENIC, 3)
                .meltingPoint(685.0)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new MoltenFlag(685.0),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material Constantan = new Material.Builder(Metallurgica.asResource("constantan"))
            .composition(RutileElements.COPPER, 1, RutileElements.NICKEL, 1)
                .addFlags(
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new NuggetFlag("tfmg"),
                        new WireFlag("tfmg"),
                        new SpoolFlag("tfmg"),
                        new SheetFlag(),
                        new DustFlag()
                ).buildAndRegister();

    // Irons
    public static final Material CastIron = new Material.Builder(Metallurgica.asResource("cast_iron"))
            .element(RutileElements.IRON)
                .addFlags(
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new NuggetFlag("tfmg"),
                        new SheetFlag("tfmg"),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material WroughtIron = new Material.Builder(Metallurgica.asResource("wrought_iron"))
            .composition(RutileElements.IRON, 3, RutileElements.CARBON, 1)
                .meltingPoint(1482.0)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag().useColumnModel(),
                        new SheetFlag(),
                        new MoltenFlag(1482.0)
                ).buildAndRegister();

    public static final Material Steel = new Material.Builder(Metallurgica.asResource("steel"))
            .element(RutileElements.IRON)
                .existingIds(MetallurgicaFlagKeys.SEMI_PRESSED_SHEET, "tfmg:unprocessed_heavy_plate", MetallurgicaFlagKeys.SHEET, "tfmg:heavy_plate")
                .addFlags(
                        new NuggetFlag("tfmg"),
                        new IngotFlag("tfmg"),
                        new SemiPressedSheetFlag("tfmg"),
                        new SheetFlag("tfmg"),
                        new StorageBlockFlag("tfmg"),
                        new CasingFlag("tfmg"),
                        new CogWheelFlag("tfmg"), new LargeCogWheelFlag("tfmg"),
                        new MoltenFlag("tfmg"),
                        new DustFlag()
                ).buildAndRegister();
}
