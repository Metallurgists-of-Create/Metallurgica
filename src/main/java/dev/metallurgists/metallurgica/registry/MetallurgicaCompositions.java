package dev.metallurgists.metallurgica.registry;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.data.custom.composition.fluid.FluidCompositionBuilder;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import dev.metallurgists.rutile.api.composition.ElementData;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.composition.data.CompositionBuilder;
import dev.metallurgists.rutile.api.composition.data.FinishedComposition;
import dev.metallurgists.rutile.api.composition.element.Element;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static dev.metallurgists.rutile.registry.RutileElements.*;
import static dev.metallurgists.rutile.api.composition.SubComposition.builder;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MetallurgicaCompositions implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected final PackOutput.PathProvider compositionPathProvider;

    public MetallurgicaCompositions(DataGenerator pGenerator) {
        this.compositionPathProvider = pGenerator.getPackOutput().createPathProvider(PackOutput.Target.DATA_PACK, "metallurgica_utilities/compositions");
    }

    public static void register(DataGenerator gen) {
        MetallurgicaCompositions metallurgicaCompositions = new MetallurgicaCompositions(gen);
        gen.addProvider(true, new DataProvider() {
            
            @Override
            public CompletableFuture<?> run(CachedOutput cachedOutput) {
                return metallurgicaCompositions.run(cachedOutput);
            }
            
            @Override
            public String getName() {
                return "Metallurgica's Composition Provider";
            }
        });
    }
    
    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        Set<ResourceLocation> set = Sets.newHashSet();
        List<CompletableFuture<?>> list = new ArrayList();
        this.buildCompositions((pFinishedComposition) -> {
            if (!set.add(pFinishedComposition.getId())) {
                throw new IllegalStateException("Duplicate composition " + pFinishedComposition.getId());
            } else {
                list.add(DataProvider.saveStable(cachedOutput, pFinishedComposition.serializeComposition(), this.compositionPathProvider.json(pFinishedComposition.getId())));
            }
        });
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }
    
    
    protected void buildCompositions(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        metallurgicaCompositions(pFinishedCompositionConsumer);
        materialCompositions(pFinishedCompositionConsumer);
        compatCompositions(pFinishedCompositionConsumer);
        bucketCompositions(pFinishedCompositionConsumer);
    }
    
    protected void bucketCompositions(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        createComposition(pFinishedCompositionConsumer, getBucket("chlorine"), ElementData.createComposition(builder().element(data(CHLORINE))));
        
        //Acids
        createComposition(pFinishedCompositionConsumer, getBucket("hydrochloric_acid"), ElementData.createComposition(
                builder().element(data(HYDROGEN)),
                        builder().element(data(CHLORINE))));
        createComposition(pFinishedCompositionConsumer, getBucket("sulfuric_acid"), ElementData.createComposition(
                builder().element(data(HYDROGEN, 2)),
                        builder().element(data(SULFUR, 1)),
                        builder().element(data(OXYGEN, 4))));
        createComposition(pFinishedCompositionConsumer, getBucket("sodium_hydroxide"), ElementData.createComposition(
                builder().element(data(SODIUM)),
                        builder().element(data(OXYGEN, 1)),
                        builder().element(data(HYDROGEN, 1))));
        createComposition(pFinishedCompositionConsumer, getBucket("sodium_hypochlorite"), ElementData.createComposition(
                builder().element(data(SODIUM)),
                        builder().element(data(OXYGEN, 1)),
                        builder().element(data(CHLORINE))));
        
        //Chlorides
        createComposition(pFinishedCompositionConsumer, getBucket("crude_titanium_tetrachloride"), ElementData.createComposition(
                builder().element(data(IRON), data(CHLORINE)).setAmount(6),
                builder().element(data(TITANIUM), data(CHLORINE, 4)).setAmount(24),
                builder().element(data(TIN), data(CHLORINE, 4)).setAmount(9),
                builder().element(data(SILICON), data(CHLORINE, 4)).setAmount(3)
        ));
        
        createComposition(pFinishedCompositionConsumer, getBucket("titanium_tetrachloride"), ElementData.createComposition(
                builder().element(data(TITANIUM)),
                builder().element(data(CHLORINE, 4))));
        createComposition(pFinishedCompositionConsumer, getBucket("silicon_tetrachloride"), ElementData.createComposition(
                builder().element(data(SILICON)),
                builder().element(data(CHLORINE, 4))));
        createComposition(pFinishedCompositionConsumer, getBucket("tin_tetrachloride"), ElementData.createComposition(
                builder().element(data(TIN)),
                builder().element(data(CHLORINE, 4))));
        
        createComposition(pFinishedCompositionConsumer, getBucket("iron_chloride"), ElementData.createComposition(
                builder().element(data(IRON)),
                builder().element(data(CHLORINE))));
        createComposition(pFinishedCompositionConsumer, getBucket("magnesium_chloride"), ElementData.createComposition(
                builder().element(data(MAGNESIUM)),
                builder().element(data(CHLORINE))));
    }
    
    protected void metallurgicaCompositions(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.salt.get(), ElementData.createComposition(
                builder().element(data(SODIUM)),
                builder().element(data(CHLORINE))));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.magnesiumChloride.get(), ElementData.createComposition(
                builder().element(data(MAGNESIUM)),
                builder().element(data(CHLORINE))));

        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.calciumPowder.get(), ElementData.createComposition(builder().element(data(CALCIUM))));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.calciumOxide.get(), ElementData.createComposition(builder().element(data(CALCIUM), data(OXYGEN))));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.sodiumCarbonate.get(), ElementData.createComposition(
                builder().element(data(SODIUM, 2)),
                builder().element(data(CARBON)),
                builder().element(data(OXYGEN, 3))));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.ammoniumChloride.get(), ElementData.createComposition(
                builder().element(data(NITROGEN)),
                builder().element(data(HYDROGEN, 4)),
                builder().element(data(CHLORINE))));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.vanadiumPentoxide.get(), ElementData.createComposition(
                builder().element(data(VANADIUM, 2)),
                builder().element(data(OXYGEN, 5))));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.ammoniumMetavanadate.get(), ElementData.createComposition(
                builder().element(data(NITROGEN)),
                builder().element(data(HYDROGEN, 4)),
                builder().element(data(VANADIUM)),
                builder().element(data(OXYGEN, 3))));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.sodiumOrthovanadate.get(), ElementData.createComposition(
                builder().element(data(SODIUM, 3)),
                builder().element(data(VANADIUM)),
                builder().element(data(OXYGEN, 4))));


        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.aluminumNugget.get(), ElementData.createComposition(builder().element(data(ALUMINUM))));
        //createComposition(pFinishedCompositionConsumer, MaterialHelper.get(MetMaterials.ALUMINUM, SHEET), ElementData.createComposition(builder().element(data(COPPER))));
        
        //createComposition(pFinishedCompositionConsumer, MaterialHelper.get(MetMaterials.BRONZE, INGOT), ElementData.createComposition(
        //        builder().element(data(COPPER, 7)),
        //        builder().element(data(TIN, 2))));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.bronzeNugget.get(), ElementData.createComposition(data(COPPER, 7), data(TIN, 2)));
        //createComposition(pFinishedCompositionConsumer, MaterialHelper.get(MetMaterials.BRONZE, SHEET), ElementData.createComposition(
        //        builder().element(data(COPPER, 7)),
        //        builder().element(data(TIN, 2))));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.bronzeDust.get(), ElementData.createComposition(data(COPPER, 7), data(TIN, 2)));
        
        //createComposition(pFinishedCompositionConsumer, MaterialHelper.get(MetMaterials.ARSENICAL_BRONZE, INGOT), ElementData.createComposition(
        //        builder().element(data(COPPER, 4)),
        //        builder().element(data(TIN, 1)),
        //        builder().element(data(ARSENIC, 3))));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.arsenicalBronzeNugget.get(), ElementData.createComposition(
        //        builder().element(data(COPPER, 4)),
        //        builder().element(data(TIN, 1)),
        //        builder().element(data(ARSENIC, 3))));
        //createComposition(pFinishedCompositionConsumer, MaterialHelper.get(MetMaterials.ARSENICAL_BRONZE, SHEET), ElementData.createComposition(
        //        builder().element(data(COPPER, 4)),
        //        builder().element(data(TIN, 1)),
        //        builder().element(data(ARSENIC, 3))));
        
        //createComposition(pFinishedCompositionConsumer, MaterialHelper.get(MetMaterials.TITANIUM_ALUMINIDE, INGOT), ElementData.createComposition(
        //        builder().element(data(TITANIUM, 2)),
        //        builder().element(data(ALUMINUM))));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.titaniumAluminideNugget.get(), ElementData.createComposition(data(TITANIUM, 2), data(ALUMINUM)));
        //createComposition(pFinishedCompositionConsumer, MaterialHelper.get(MetMaterials.TITANIUM_ALUMINIDE, SHEET), ElementData.createComposition(
        //        builder().element(data(TITANIUM, 2)),
        //        builder().element(data(ALUMINUM))));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.titaniumAluminideDust.get(), ElementData.createComposition(data(TITANIUM, 2), data(ALUMINUM)));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.semiPressedTitaniumAluminideSheet.get(), ElementData.createComposition(data(TITANIUM, 2), data(ALUMINUM)));

        //createComposition(pFinishedCompositionConsumer, MaterialHelper.get(MetMaterials.TITANIUM, INGOT), ElementData.createComposition(builder().element(data(TITANIUM))));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.titaniumNugget.get(), ElementData.createComposition(builder().element(data(TITANIUM))));
        //createComposition(pFinishedCompositionConsumer, MaterialHelper.get(MetMaterials.TITANIUM, SHEET), ElementData.createComposition(builder().element(data(TITANIUM))));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.semiPressedTitaniumSheet.get(), ElementData.createComposition(data(TITANIUM)));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.pigIron.get(), ElementData.createComposition(
                builder().element(data(IRON, 3)),
                builder().element(data(CARBON))));
        
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.hornblendeShard.get(), ElementData.createComposition(
                builder().element(data(CALCIUM)),
                builder().element(data(SODIUM, 2)),
                builder().element(
                data(MAGNESIUM),
                data(IRON),
                data(ALUMINUM)).setAmount(5),
                builder().element(
                data(ALUMINUM),
                data(SILICON)).setAmount(8),
                builder().element(data(OXYGEN, 22)),
                builder().element(
                data(OXYGEN),
                data(HYDROGEN)).setAmount(2)
        ));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.plagioclaseShard.get(), ElementData.createComposition(
                builder().element(data(SODIUM)),
                builder().element(data(ALUMINUM)),
                builder().element(data(SILICON, 3)),
                builder().element(data(OXYGEN, 8))
        ));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.biotiteShard.get(), ElementData.createComposition(
                builder().element(
                data(POTASSIUM)),
                builder().element(
                data(MAGNESIUM),
                data(IRON)).setAmount(3),
                builder().element(data(ALUMINUM)),
                builder().element(data(SILICON, 3)),
                builder().element(data(OXYGEN, 10)),
                builder().element(
                data(FLUORINE),
                data(OXYGEN),
                data(HYDROGEN)).setAmount(2)
        ));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.clinopyroxeneShard.get(), ElementData.createComposition(
                builder().element(
                data(CALCIUM),
                data(MAGNESIUM),
                data(IRON),
                data(SODIUM)),
                builder().element(
                data(MAGNESIUM),
                data(IRON),
                data(ALUMINUM)),
                builder().element(
                data(SILICON),
                data(ALUMINUM)).setAmount(2),
                builder().element(
                data(OXYGEN, 6))
        ));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.orthopyroxeneShard.get(), ElementData.createComposition(
                builder().element(
                        data(MAGNESIUM),
                        data(IRON),
                        data(CALCIUM)),
                builder().element(
                        data(MAGNESIUM),
                        data(IRON),
                        data(ALUMINUM)),
                builder().element(
                        data(SILICON),
                        data(ALUMINUM)).setAmount(2),
                builder().element(
                        data(OXYGEN, 6))
        ));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.amphiboleShard.get(), ElementData.createComposition(
                builder().element(data(MAGNESIUM, 7)),
                builder().element(data(SILICON, 8)),
                builder().element(data(OXYGEN, 22)),
                builder().element(
                data(OXYGEN),
                data(HYDROGEN)).setAmount(2)
        ));
    }
    
    protected void materialCompositions(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        //createComposition(pFinishedCompositionConsumer, MetallurgicaOre.MAGNETITE.ORE.raw().get(), ElementData.createComposition(
        //        builder().element(data(IRON, 3)),
        //        builder().element(data(OXYGEN, 4))));
        
        //createComposition(pFinishedCompositionConsumer, MetallurgicaOre.BAUXITE.ORE.raw().get(), ElementData.createComposition(
        //        builder().element(data(ALUMINUM, 2)),
        //        builder().element(data(OXYGEN, 3)),
        //        builder().element(data(HYDROGEN, 2)),
        //        builder().element(data(OXYGEN, 2))));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.washedAlumina.get(), ElementData.createComposition(
                builder().element(data(ALUMINUM, 2)),
                builder().element(data(OXYGEN, 3))));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.alumina.get(), ElementData.createComposition(
                builder().element(data(ALUMINUM, 2)),
                builder().element(data(OXYGEN, 3))));

        //createComposition(pFinishedCompositionConsumer, MetallurgicaOre.CASSITERITE.ORE.raw().get(), ElementData.createComposition(
        //        builder().element(data(TIN)),
        //        builder().element(data(OXYGEN, 2))));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.alluvialCassiterite.get(), ElementData.createComposition(
                builder().element(data(TIN)),
                builder().element(data(OXYGEN, 2))));
        
        //createComposition(pFinishedCompositionConsumer, MetallurgicaOre.NATIVE_COPPER.ORE.raw().get(), ElementData.createComposition(builder().element(data(COPPER))));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaOre.MALACHITE.ORE.raw().get(), ElementData.createComposition(
        //        builder().element(data(COPPER, 2)),
        //        builder().element(data(CARBON)),
        //        builder().element(data(OXYGEN, 3)),
        //        builder().element(data(HYDROGEN, 2))));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.copperOxide.get(), ElementData.createComposition(
                builder().element(data(COPPER)),
                builder().element(data(OXYGEN))));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.copperRubble.get(), ElementData.createComposition(builder().element(data(COPPER))));
        
        //createComposition(pFinishedCompositionConsumer, MetallurgicaOre.FLUORITE.ORE.raw().get(), ElementData.createComposition(
        //        builder().element(data(CALCIUM)),
        //        builder().element(data(FLUORINE, 2))));
        createComposition(pFinishedCompositionConsumer, MetallurgicaItems.fluoriteCluster.get(), ElementData.createComposition(
                builder().element(data(CALCIUM)),
                builder().element(data(FLUORINE, 2))));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.fluoritePowder.get(), ElementData.createComposition(
        //        builder().element(data(CALCIUM)),
        //        builder().element(data(FLUORINE, 2))));
        
        //createComposition(pFinishedCompositionConsumer, MetallurgicaOre.RUTILE.ORE.raw().get(), ElementData.createComposition(
        //        builder().element(data(TITANIUM)),
        //        builder().element(data(OXYGEN, 2))));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaItems.rutilePowder.get(), ElementData.createComposition(
        //        builder().element(data(TITANIUM)),
        //        builder().element(data(OXYGEN, 2))));
        
        //createComposition(pFinishedCompositionConsumer, MetallurgicaOre.NATIVE_GOLD.ORE.raw().get(), ElementData.createComposition(builder().element(data(GOLD))));
        
        //createComposition(pFinishedCompositionConsumer, MetallurgicaOre.SMITHSONITE.ORE.raw().get(), ElementData.createComposition(
        //        builder().element(data(ZINC)),
        //        builder().element(data(CARBON)),
        //        builder().element(data(OXYGEN, 3))));
        //createComposition(pFinishedCompositionConsumer, MetallurgicaOre.SPHALERITE.ORE.raw().get(), ElementData.createComposition(
        //        builder().element(data(ZINC), data(IRON)),
        //        builder().element(data(SULFUR))));
    }
    
    protected void compatCompositions(Consumer<FinishedComposition> pFinishedCompositionConsumer) {
        //createComposition(pFinishedCompositionConsumer, Items.IRON_INGOT, ElementData.createComposition(builder().element(data(IRON))));
        //createComposition(pFinishedCompositionConsumer, Items.IRON_NUGGET, ElementData.createComposition(builder().element(data(IRON))));
        //createComposition(pFinishedCompositionConsumer, Items.GOLD_INGOT, ElementData.createComposition(builder().element(data(GOLD))));
        //createComposition(pFinishedCompositionConsumer, Items.GOLD_NUGGET, ElementData.createComposition(builder().element(data(GOLD))));
        //createComposition(pFinishedCompositionConsumer, Items.COPPER_INGOT, ElementData.createComposition(builder().element(data(COPPER))));
        //createComposition(pFinishedCompositionConsumer, AllItems.COPPER_NUGGET.get(), ElementData.createComposition(builder().element(data(COPPER))));
        //createComposition(pFinishedCompositionConsumer, AllItems.ZINC_INGOT.get(), ElementData.createComposition(builder().element(data(ZINC))));
        //createComposition(pFinishedCompositionConsumer, AllItems.ZINC_NUGGET.get(), ElementData.createComposition(builder().element(data(ZINC))));
        //createComposition(pFinishedCompositionConsumer, TFMGItems.ALUMINUM_INGOT.get(), ElementData.createComposition(builder().element(data(ALUMINUM))));
        
        //createComposition(pFinishedCompositionConsumer, AllItems.BRASS_INGOT.get(), ElementData.createComposition(
        //        builder().element(data(COPPER, 3)),
        //        builder().element(data(ZINC, 2))));
        //createComposition(pFinishedCompositionConsumer, AllItems.BRASS_NUGGET.get(), ElementData.createComposition(
        //        builder().element(data(COPPER, 3)),
        //        builder().element(data(ZINC, 2))));
        //createComposition(pFinishedCompositionConsumer, AllItems.BRASS_SHEET.get(), ElementData.createComposition(
        //        builder().element(data(COPPER, 3)),
        //        builder().element(data(ZINC, 2))));
        
        //createComposition(pFinishedCompositionConsumer, TFMGItems.SULFUR_DUST.get(), ElementData.createComposition(builder().element(data(SULFUR))));
        //createComposition(pFinishedCompositionConsumer, TFMGBlocks.SULFUR.get(), ElementData.createComposition(builder().element(data(SULFUR))));
        
        //createComposition(pFinishedCompositionConsumer, TFMGItems.NITRATE_DUST.get(), ElementData.createComposition(
        //        builder().element(data(POTASSIUM)),
        //        builder().element(data(NITROGEN)),
        //        builder().element(data(OXYGEN, 3))));
        
        //createComposition(pFinishedCompositionConsumer, TFMGItems.LIMESAND.get(), ElementData.createComposition(
        //        builder().element(data(CALCIUM)),
        //        builder().element(data(CARBON)),
        //        builder().element(data(OXYGEN, 3))));
    }
    
    private ElementData data(Element element, int amount) {
        return new ElementData(element.getId(), amount);
    }
    
    private ElementData data(Element element) {
        return new ElementData(element.getId(), 1);
    }
    
    public static Item getBucket(String name) {
        return Metallurgica.registrate.get(name+"_bucket", ForgeRegistries.ITEMS.getRegistryKey()).get();
    }
    public static FluidStack getFluidStack(Fluid fluid) {
        return new FluidStack(fluid, 1);
    }
    public static FluidStack getFluidStack(Fluid fluid, CompoundTag tag) {
        FluidStack stack = new FluidStack(fluid, 1);
        stack.setTag(tag);
        return stack;
    }
    protected static void createComposition(Consumer<FinishedComposition> pFinishedCompositionConsumer, ItemLike item, List<SubComposition> subCompositions) {
        CompositionBuilder.create(item, subCompositions).save(pFinishedCompositionConsumer);
    }
    
    @Override
    public String getName() {
        return "Metallurgica Composition Provider";
    }
}
