package dev.metallurgists.metallurgica.registry;

import com.drmangotea.tfmg.TFMG;
import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.content.fluids.types.Acid;
import dev.metallurgists.metallurgica.content.fluids.types.RiverSandFluid.*;
import dev.metallurgists.metallurgica.content.fluids.types.RiverSandFluid;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.foundation.util.TextUtil;
import dev.metallurgists.metallurgica.registry.material.init.MetMaterialFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MetallurgicaFluids {
    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().setCreativeTab(MCreativeTabs.MAIN);

    public static final ResourceLocation AIR_RL = TFMG.asResource("fluid/air");
    public static final ResourceLocation NITROGEN_RL = Metallurgica.asResource("fluid/nitrogen");
    public static final ResourceLocation BFG_RL = Metallurgica.asResource("fluid/bfg");
    public static final ResourceLocation RIVER_SAND_RL = Metallurgica.asResource("fluid/river_sand");

    public static final FluidEntry<VirtualFluid>
            preheatedAir =             registrate.virtualFluid("preheated_air", AIR_RL, AIR_RL).lang("Preheated Air").register(),
            nitrogen =                 registrate.virtualFluid("nitrogen", NITROGEN_RL, NITROGEN_RL).lang("Nitrogen").register(),
            bfg =                      registrate.virtualFluid("bfg", AIR_RL, AIR_RL).lang("Blast Furnace Gas").register(),
            cryolite =                 registrate.tintedVirtualFluid("cryolite", 0xff90EE90).lang("Cryolite").register(),
            decontaminatedWater =      registrate.tintedVirtualFluid("decontaminated_water", 0xff90C6E3).lang("Decontaminated Water").register(),
            magnetiteFines =           registrate.tintedVirtualDust("magnetite_fines", 0xff696A76).lang("Magnetite Fines").register()
            
    ;
    //CHLORIDES
    public static final FluidEntry<VirtualFluid>
            //CRUDE
            crudeTitaniumTetrachloride = chloride("crude_titanium_tetrachloride", 0xff876253),
            //TETRACHLORIDE
            titaniumTetrachloride =    chloride("titanium_tetrachloride", 0xffA76F6F),
            siliconTetrachloride =    chloride("silicon_tetrachloride", 0xff42495E),
            tinTetrachloride =        chloride("tin_tetrachloride", 0xffC2D4D4),
            //CHLORIDES
            ironChloride =             chloride("iron_chloride", 0xff48180E),
            magnesiumChloride =         chloride("magnesium_chloride", 0xffEBDCA9)
                    
                    
                    ;

    public static final FluidEntry<Acid>
            hydrochloricAcid =         acid("hydrochloric_acid", 0xffAAFFAA, 1.1f),
            sulfuricAcid =             acid("sulfuric_acid", 0xffAAAAFF, 0.1f),
            sodiumHydroxide =          acid("sodium_hydroxide", 0xffC3D2D5, 14),
            sodiumHypochlorite =       acid("sodium_hypochlorite", 0xffE8f1C7, 1.1f);

    public static final FluidEntry<RiverSandFluid> riverSand =
            registrate.virtualFluid("river_sand", RiverSandFluidType::new, RiverSandFluid::createSource, RiverSandFluid::createFlowing).lang("River Sand").register();

    public static final FluidEntry<VirtualFluid>
            chlorine = gas("chlorine", 0xffDBD971, AllTags.forgeFluidTag("chlorine"));
    
    public static final FluidEntry<ForgeFlowingFluid.Flowing> claySlip = flowing("clay_slip", 0xff725537);
    public static final FluidEntry<ForgeFlowingFluid.Flowing> brine = flowing("brine", 0xff599fd3, AllTags.forgeFluidTag("brine"));

    public static Collection<RegistryEntry<Fluid>> ALL = registrate.getAll(ForgeRegistries.FLUIDS.getRegistryKey());

    public static List<FluidStack> getVirtualFluidStacks() {
        List<FluidStack> stacks = new ArrayList<>();
        for (RegistryEntry<Fluid> entry : ALL) {
            if (entry instanceof FluidEntry<?> fluidEntry) {
                if (fluidEntry.get() instanceof VirtualFluid virtualFluid) {
                    FluidStack stack = new FluidStack(virtualFluid.getSource(), FluidType.BUCKET_VOLUME);
                    stacks.add(stack);
                }
            }
        }
        return stacks;
    }
    
    @SafeVarargs
    public static FluidEntry<ForgeFlowingFluid.Flowing> flowing(String name, int color, TagKey<Fluid>... tags) {
        TagKey<Fluid> tag = FluidTags.create(Metallurgica.asResource(name));
        
        TagKey<Fluid>[] fluidTags = tags;
        
        if(tags.length==0){
            
            fluidTags = new TagKey[]{tag};
            
        }
        
        
        return  registrate.tintedFluid(name, color)
                .tag(tags)
                .register();
    }
    
    @SafeVarargs
    public static FluidEntry<Acid> acid(String name, int color, float ph, TagKey<Fluid>... tags) {
        TagKey<Fluid> tag = FluidTags.create(Metallurgica.asResource(name));
        
        TagKey<Fluid>[] fluidTags = tags;
        
        if(tags.length==0){
            
            fluidTags = new TagKey[]{tag};
            
        }
        
        return  registrate.acid(name, color, ph)
                .tag(tags)
                .tag(MetallurgicaTags.AllFluidTags.ACID.tag)
                .lang(TextUtil.fromId(name))
                .bucket()
                .lang(TextUtil.fromId(name)+" Barrel")
                .tag(AllTags.forgeItemTag("buckets/"+name))
                .build()
                .register();
    }
    
    @SafeVarargs
    public static FluidEntry<VirtualFluid> chloride(String name, int color, TagKey<Fluid>... tags) {
        TagKey<Fluid> tag = FluidTags.create(Metallurgica.asResource(name));
        
        TagKey<Fluid>[] fluidTags = tags;
        
        if(tags.length==0){
            
            fluidTags = new TagKey[]{tag};
            
        }
        
        return  registrate.tintedVirtualFluid(name, color, "slick_fluid")
                .tag(tags)
                .tag(MetallurgicaTags.AllFluidTags.CHLORIDE.tag)
                //  .source(GasFluid.Source::new)
                .lang(TextUtil.fromId(name))
                .bucket()
                .lang(TextUtil.fromId(name)+" Barrel")
                .tag(AllTags.forgeItemTag("buckets/"+name))
                .build()
                .register();
    }
    
    @SafeVarargs
    public static FluidEntry<VirtualFluid> gas(String name, int color, TagKey<Fluid>... tags){
        TagKey<Fluid> tag = FluidTags.create(Metallurgica.asResource(name));
        
        TagKey<Fluid>[] fluidTags = tags;
        
        if(tags.length==0){
            
            fluidTags = new TagKey[]{tag};
            
        }
        
        return  registrate.tintedVirtualFluid(name, color)
                .tag(tags)
                .tag(MetallurgicaTags.AllFluidTags.GAS.tag)
                //  .source(GasFluid.Source::new)
                .lang(TextUtil.fromId(name))
                .bucket()
                .lang(TextUtil.fromId(name)+" Tank")
                .tag(AllTags.forgeItemTag("buckets/"+name))
                .build()
                .register();
    }

    public static void registerFluidInteractions() {

    }

    public static void register() {
        //MetallurgicaRegistrate materialRegistrate = (MetallurgicaRegistrate) Metallurgica.registrate().setCreativeTab(MCreativeTabs.MATERIALS);
        //MetMaterialFluids.generateMaterialFluids(materialRegistrate);
        //MetMaterialFluids.MATERIAL_FLUIDS = MetMaterialFluids.MATERIAL_FLUIDS_BUILDER.build();
//
        //MetMaterialFluids.MATERIAL_FLUIDS_BUILDER = null;
    }
}
