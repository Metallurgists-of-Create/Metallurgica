package dev.metallurgists.metallurgica.compat.rutile.materials;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.item.SheetFlag;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.registry.RutileElements;
import dev.metallurgists.rutile.registry.RutileFlagKeys;
import dev.metallurgists.rutile.registry.flags.DustFlag;
import dev.metallurgists.rutile.registry.flags.IngotFlag;
import dev.metallurgists.rutile.registry.flags.NuggetFlag;
import dev.metallurgists.rutile.registry.flags.StorageBlockFlag;

import static dev.metallurgists.metallurgica.compat.rutile.materials.MetallurgicaMaterials.*;

public class CompoundMaterials {

    public static void modify() {

    }

    public static void register() {

    }

    public static final Material MagnesiumOxide = new Material.Builder(Metallurgica.asResource("magnesium_oxide"))
            .composition(RutileElements.MAGNESIUM, 1, RutileElements.OXYGEN, 1)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag(),
                        new StorageBlockFlag(),
                        new SheetFlag(),
                        new DustFlag()
                ).buildAndRegister();

    public static final Material PotassiumNitrate = new Material.Builder(Metallurgica.asResource("potassium_nitrate"))
            .composition(RutileElements.POTASSIUM, 1, RutileElements.NITROGEN, 1, RutileElements.OXYGEN, 3)
                .existingIds(RutileFlagKeys.DUST, "tfmg:nitrate_dust")
                .addFlags(
                        new DustFlag()
                ).buildAndRegister();

    public static final Material CalciumCarbonite = new Material.Builder(Metallurgica.asResource("calcium_carbonate"))
            .composition(RutileElements.CALCIUM, 1, RutileElements.CARBON, 1, RutileElements.OXYGEN, 3)
                .existingIds(RutileFlagKeys.DUST, "tfmg:limesand")
                .addFlags(
                        new DustFlag()
                ).buildAndRegister();
}
