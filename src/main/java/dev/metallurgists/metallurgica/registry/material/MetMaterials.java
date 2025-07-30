package dev.metallurgists.metallurgica.registry.material;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.infastructure.material.MaterialEntry;
import dev.metallurgists.metallurgica.registry.MCreativeTabs;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static dev.metallurgists.metallurgica.foundation.util.TextUtil.toEnglishName;

public class MetMaterials {
    public static final Map<ResourceLocation, Material> registeredMaterials = new HashMap<>();

    //Base Metals
    public static MaterialEntry<Material> IRON;
    public static MaterialEntry<Material> GOLD;
    public static MaterialEntry<Material> COPPER;
    public static MaterialEntry<Material> NETHERIUM;
    public static MaterialEntry<Material> ALUMINUM;
    public static MaterialEntry<Material> SCANDIUM;
    public static MaterialEntry<Material> LEAD;
    public static MaterialEntry<Material> SILVER;
    public static MaterialEntry<Material> NICKEL;
    public static MaterialEntry<Material> TIN;
    public static MaterialEntry<Material> ZINC;
    public static MaterialEntry<Material> PLATINUM;
    public static MaterialEntry<Material> TITANIUM;
    public static MaterialEntry<Material> URANIUM;
    public static MaterialEntry<Material> LITHIUM;
    public static MaterialEntry<Material> MAGNESIUM;
    public static MaterialEntry<Material> TUNGSTEN;
    public static MaterialEntry<Material> OSMIUM;
    public static MaterialEntry<Material> THORIUM;
    public static MaterialEntry<Material> TANTALUM;
    public static MaterialEntry<Material> SODIUM;
    public static MaterialEntry<Material> CHROMIUM;
    public static MaterialEntry<Material> VANADIUM;
    public static MaterialEntry<Material> MANGANESE;
    public static MaterialEntry<Material> POTASSIUM;
    public static MaterialEntry<Material> BERYLLIUM;
    public static MaterialEntry<Material> RADIUM;
    public static MaterialEntry<Material> ARSENIC;
    public static MaterialEntry<Material> CADMIUM;

    //Non-Metals
    public static MaterialEntry<Material> NULL;
    public static MaterialEntry<Material> SILICON;
    public static MaterialEntry<Material> SULFUR;
    public static MaterialEntry<Material> QUARTZ;

    //Phosphorus Allotropes
    public static MaterialEntry<Material> WHITE_PHOSPHORUS;

    //Carbon Allotropes
    public static MaterialEntry<Material> GRAPHITE;
    public static MaterialEntry<Material> COAL_COKE;
    public static MaterialEntry<Material> COAL;
    public static MaterialEntry<Material> CHARCOAL;
    public static MaterialEntry<Material> DIAMOND;

    //Irons
    public static MaterialEntry<Material> CAST_IRON;
    public static MaterialEntry<Material> WROUGHT_IRON;
    public static MaterialEntry<Material> STEEL;


    //Alloys
    public static MaterialEntry<Material> TITANIUM_ALUMINIDE;
    public static MaterialEntry<Material> NETHERITE;
    public static MaterialEntry<Material> BRASS;
    public static MaterialEntry<Material> BRONZE;
    public static MaterialEntry<Material> ARSENICAL_BRONZE;
    public static MaterialEntry<Material> ANDESITE_ALLOY;
    public static MaterialEntry<Material> CONSTANTAN;

    //Minerals
    public static MaterialEntry<Material> MALACHITE;
    public static MaterialEntry<Material> MAGNETITE;
    public static MaterialEntry<Material> HEMATITE;
    public static MaterialEntry<Material> BAUXITE;
    public static MaterialEntry<Material> SPODUMENE;
    public static MaterialEntry<Material> SPHALERITE;
    public static MaterialEntry<Material> SMITHSONITE;
    public static MaterialEntry<Material> RUTILE;
    public static MaterialEntry<Material> POTASH;
    public static MaterialEntry<Material> CASSITERITE;
    public static MaterialEntry<Material> FLUORITE;
    public static MaterialEntry<Material> CUPRITE;
    public static MaterialEntry<Material> VANADINITE;

    //Compounds
    public static MaterialEntry<Material> MAGNESIUM_OXIDE;
    public static MaterialEntry<Material> POTASSIUM_NITRATE;
    public static MaterialEntry<Material> CALCIUM_CARBONATE;

    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().setCreativeTab(MCreativeTabs.MATERIALS);


    static MaterialEntry<Material> createMaterial(String name, NonNullUnaryOperator<Material.Builder> builder) {
        return null;//registrate.material(name, Material.Builder::buildAndRegister).builder(builder).lang(toEnglishName(name)).register();
    }

    public static void register(){

    }
}
