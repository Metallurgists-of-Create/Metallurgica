package dev.metallurgists.metallurgica.registry;

import com.simibubi.create.AllTags;
import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.content.items.sealed_storage.SealedBundleItem;
import dev.metallurgists.metallurgica.content.items.temperature.ThermometerItem;
import dev.metallurgists.metallurgica.content.metalworking.forging.hammer.ForgeHammerItem;
import dev.metallurgists.metallurgica.foundation.item.lining.tank_lining.TankLiningItem;
import dev.metallurgists.metallurgica.foundation.item.lining.tank_lining.TankLiningStats;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.foundation.item.MetallurgicaItem;
import dev.metallurgists.metallurgica.registry.material.init.MetMaterialItems;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaHeatingCoils;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaRegistries;
import net.minecraft.world.item.Item;

import static dev.metallurgists.metallurgica.content.temperature.hot_plate.heating_coil.HeatingCoilType.heatingCoil;

public class MetallurgicaItems {
    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().setCreativeTab(MCreativeTabs.MAIN);

    private static ItemEntry<SequencedAssemblyItem> sequencedIngredient(String name) {
        return registrate.item(name, SequencedAssemblyItem::new)
                .register();
    }

    //public static final ItemEntry<CableItem>
    //        aluminumWire = registrate.item("aluminum_wire", (p) -> new CableItem(p, Meta.aluminum)).register();

    public static final ItemEntry<Item>
            thermometer = registrate.item("thermometer", ThermometerItem::new, p->p.stacksTo(1));

    public static final ItemEntry<SealedBundleItem>
            sealedBundle = registrate.item("sealed_bundle", SealedBundleItem::new, p->p.stacksTo(1))
                    ;

    public static final ItemEntry<ForgeHammerItem>
            copperForgeHammer = registrate.item("copper_forge_hammer", ForgeHammerItem::new, p->p.stacksTo(1));
    //MISC ITEMS
    public static final ItemEntry<MetallurgicaItem>
            salt =                registrate.metallurgicaItem("salt", "salt"),
    //IRON PROCESSING
            pigIron =      registrate.metallurgicaItem("pig_iron_ingot", "ingots/pig_iron_ingot", "ingots"), //I REFUSE TO REGISTER THIS ANYWHERE OTHER THAN IRON PROCESSING
            impureIronBloom =      registrate.metallurgicaItem("impure_iron_bloom"),
            pureIronBloom =      registrate.metallurgicaItem("pure_iron_bloom"),
            ironSinter =      registrate.metallurgicaItem("iron_sinter"),

    //MAGNETITE PROCESSING
            magnetiteLumps =      registrate.metallurgicaItem("magnetite_lumps", "lumps/magnetite", "lumps"),
            fineMagnetiteTracedSand = registrate.metallurgicaItem("fine_magnetite_traced_sand", "dusts/magnetite_traced_sand", "dusts"),
            sandPile = registrate.metallurgicaItem("sand_pile", "dusts/sand", "dusts"),

    //TIN PROCESSING
            alluvialCassiterite = registrate.metallurgicaItem("alluvial_cassiterite", "alluvial_materials/cassiterite", "alluvial_materials"),

    //COPPER PROCESSING
            copperOxide = registrate.metallurgicaItem("copper_oxide", "dusts/copper_oxide", "dusts"),

    //BAUXITE PROCESSING
            washedAlumina =       registrate.metallurgicaItem("washed_alumina", "washed_materials/alumina", "washed_materials"),
            alumina =             registrate.metallurgicaItem("alumina", "alumina")
                    ;

    //Vanadium
    public static final ItemEntry<MetallurgicaItem>
            sodiumOrthovanadate=registrate.powder("sodium_orthovanadate"),
            ammoniumMetavanadate=registrate.powder("ammonium_metavanadate"),
            vanadiumPentoxide=registrate.powder("vanadium_pentoxide"),
            calciumOxide=registrate.powder("calcium_oxide")
            ;
    public static final ItemEntry<MetallurgicaItem>
            sodiumCarbonate=registrate.powder("sodium_carbonate"),
            ammoniumChloride=registrate.metallurgicaItem("ammonium_chloride")
                    ;

    public static final ItemEntry<MetallurgicaItem>
            calciumPowder=registrate.powder("calcium");
    
    //FLUORITE
    public static final ItemEntry<MetallurgicaItem>
            fluoriteCluster =     registrate.cluster("fluorite_cluster")
                    ;

    //MAGNESIUM
    public static final ItemEntry<MetallurgicaItem>
            magnesiumChloride =   registrate.metallurgicaItem("magnesium_chloride" )
                    ;
    //METALS


    
    //ALLOYS
    
    public static final ItemEntry<MetallurgicaItem>
            hornblendeShard =     registrate.metallurgicaItem("hornblende_shard", "shards/hornblende", "shards", "rock_shards"),
            plagioclaseShard =    registrate.metallurgicaItem("plagioclase_shard", "shards/plagioclase", "shards", "rock_shards"),
            biotiteShard =        registrate.metallurgicaItem("biotite_shard", "shards/biotite", "shards", "rock_shards"),
            clinopyroxeneShard =  registrate.metallurgicaItem("clinopyroxene_shard", "shards/clinopyroxene", "shards", "rock_shards"),
            orthopyroxeneShard =  registrate.metallurgicaItem("orthopyroxene_shard", "shards/orthopyroxene", "shards", "rock_shards"),
            quartzShard =         registrate.metallurgicaItem("quartz_shard", "shards/quartz", "shards", "rock_shards"),
            amphiboleShard =      registrate.metallurgicaItem("amphibole_shard", "shards/amphibole", "shards", "rock_shards")
                    ;
    
    //PRIMITIVE
    public static final ItemEntry<Item> dirtyClayBall = registrate.simpleItem("dirty_clay_ball", "dirty_clay_balls", "primitive_materials");

    public static final ItemEntry<Item>
            loosenedBauxite =     registrate.simpleItem("loosened_bauxite", "loosened_materials/bauxite", "loosened_materials"); //why is this a normal item??? Idk lol

    public static final ItemEntry<Item>
            nickelHeatingCoil = registrate.item("nickel_heating_coil", Item::new)
            .tag(AllTags.forgeItemTag("heating_coils"), AllTags.forgeItemTag("heating_coils"))
            .transform(heatingCoil(MetallurgicaHeatingCoils.NICKEL))
            .register();

    // Kiln
    public static final ItemEntry<MetallurgicaItem> ceramicClay = registrate.metallurgicaItem("ceramic_clay");
    public static final ItemEntry<MetallurgicaItem> unfiredKilnBrick = registrate.metallurgicaItem("unfired_kiln_brick", "ingots");

    public static final ItemEntry<TankLiningItem> rubberTankLining = registrate.item("rubber_tank_lining", (p) -> new TankLiningItem(p, new TankLiningStats(new TankLiningStats.LiningProperties(1200).corrosionResistant(1.0f))))
            .properties(p -> p.stacksTo(1))
            .register();

    public static void register() {
        MetallurgicaRegistrate materialRegistrate = (MetallurgicaRegistrate) Metallurgica.registrate().setCreativeTab(MCreativeTabs.MATERIALS);
        MetMaterialItems.generateMaterialItems(materialRegistrate);
        MetMaterialItems.MATERIAL_ITEMS = MetMaterialItems.MATERIAL_ITEMS_BUILDER.build();

        MetMaterialItems.MATERIAL_ITEMS_BUILDER = null;
    }
}
