package dev.metallurgists.metallurgica.foundation.data.recipe.tfmg;

import com.drmangotea.tfmg.datagen.recipes.builder.VatMachineRecipeBuilder;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import dev.metallurgists.metallurgica.compat.rutile.flags.MetallurgicaFlagKeys;
import dev.metallurgists.metallurgica.compat.rutile.materials.MetalMaterials;
import dev.metallurgists.metallurgica.compat.rutile.materials.MetallurgicaMaterials;
import dev.metallurgists.metallurgica.compat.rutile.materials.MineralMaterials;
import dev.metallurgists.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import dev.metallurgists.metallurgica.infastructure.material.MaterialHelper;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.registry.MetallurgicaFluids;
import dev.metallurgists.metallurgica.registry.MetallurgicaItems;
import dev.metallurgists.metallurgica.registry.material.MetMaterials;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import dev.metallurgists.rutile.util.helpers.MaterialHelpers;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;

public class VatGen extends MProcessingRecipeGen {

    GeneratedRecipe

    sodiumHydroxide = createVatRecipe("sodium_hydroxide", (b) -> (VatMachineRecipeBuilder)b
            .require(I.salt())
            .require(I.salt())
            .require(F.water(), 1000)
            .output(F.sodiumHydroxide(), 600)
            .output(F.chlorine(), 400)
            .requiresHeat(HeatCondition.HEATED)
            .duration(200), this.electrolysis()),

    sodiumHydroxideDecontaminated = createVatRecipe("sodium_hydroxide_from_decontaminated_water", (b) -> (VatMachineRecipeBuilder)b
            .require(I.salt())
            .require(I.salt())
            .require(F.decontaminatedWater(), 1000)
            .output(F.sodiumHydroxide(), 800)
            .output(F.chlorine(), 200)
            .requiresHeat(HeatCondition.HEATED)
            .duration(200), this.electrolysis()),

    magnesiumChloride = createVatRecipe("magnesium_chloride", (b) -> (VatMachineRecipeBuilder)b
            .require(MetallurgicaFluids.magnesiumChloride.get(), 500)
            .output(MaterialHelpers.getFluid(MetalMaterials.Magnesium, MetallurgicaFlagKeys.MOLTEN), 300)
            .output(F.chlorine(), 200)
            .requiresHeat(HeatCondition.HEATED)
            .duration(600), this.electrolysis()),

    sodiumOrthovanadate = createVatRecipe("vanadium_processing/sodium_othrovanadate", (b) -> (VatMachineRecipeBuilder)b
            .require(Ingredient.of(I.salt(), MetallurgicaItems.sodiumCarbonate))
            .require(MaterialHelpers.getItem(MineralMaterials.Vanadinite, MetallurgicaFlagKeys.RUBBLE))
            .output(MetallurgicaItems.sodiumOrthovanadate)
            .requiresHeat(HeatCondition.SUPERHEATED)
            .duration(400), this.mixing()),

    ammoniumMetavanadate = createVatRecipe("vanadium_processing/ammonium_metavanadate", (b) -> (VatMachineRecipeBuilder)b
            .require(MetallurgicaItems.sodiumOrthovanadate)
            .require(MetallurgicaItems.ammoniumChloride)
            .require(Fluids.WATER, 1000)
            .output(MetallurgicaItems.ammoniumMetavanadate)
            .duration(200), this.mixing()),

    ammoniumMetavanadateDecontaminated = createVatRecipe("vanadium_processing/ammonium_metavanadate_from_decontaminated_water", (b) -> (VatMachineRecipeBuilder)b
            .require(MetallurgicaItems.sodiumOrthovanadate)
            .require(MetallurgicaItems.ammoniumChloride)
            .require(MetallurgicaFluids.decontaminatedWater.get(), 1000)
            .output(MetallurgicaItems.ammoniumMetavanadate)
            .output(0.25f, MetallurgicaItems.ammoniumMetavanadate)
            .duration(200), this.mixing()),

    vanadiumPentoxide = createVatRecipe("vanadium_processing/vanadium_pentoxide", (b) -> (VatMachineRecipeBuilder)b
            .require(MetallurgicaItems.ammoniumMetavanadate)
            .require(MetallurgicaItems.ammoniumMetavanadate)
            .output(MetallurgicaItems.vanadiumPentoxide)
            .duration(200), this.arcBlasting()),

    vanadium = createVatRecipe("vanadium_processing/molten_vanadium", (b) -> (VatMachineRecipeBuilder)b
            .require(MetallurgicaItems.vanadiumPentoxide)
            .require(MetallurgicaItems.calciumPowder)
            .require(MetallurgicaItems.calciumPowder)
            .require(MetallurgicaItems.calciumPowder)
            .require(MetallurgicaItems.calciumPowder)
            .require(MetallurgicaItems.calciumPowder)
            .output(MaterialHelpers.getFluid(MetalMaterials.Vanadium, MetallurgicaFlagKeys.MOLTEN), MaterialHelper.FluidValues.ingots(2))
            .output(MetallurgicaItems.calciumOxide, 5)
            .duration(600), this.electrolysis())

            ;

    public VatGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return TFMGRecipeTypes.VAT_MACHINE_RECIPE;
    }

    public VatMachineRecipeBuilder.VatRecipeParams electrolysis() {
        VatMachineRecipeBuilder.VatRecipeParams params = new VatMachineRecipeBuilder.VatRecipeParams();
        params.machines.add("tfmg:electrode");
        params.machines.add("tfmg:electrode");
        params.allowedVatTypes = new ArrayList<>();
        params.allowedVatTypes.add("tfmg:steel_vat");
        params.allowedVatTypes.add("tfmg:firebrick_lined_vat");
        return params;
    }

    public VatMachineRecipeBuilder.VatRecipeParams mixing() {
        VatMachineRecipeBuilder.VatRecipeParams params = new VatMachineRecipeBuilder.VatRecipeParams();
        params.machines.add("tfmg:mixing");
        params.allowedVatTypes = new ArrayList<>();
        params.allowedVatTypes.add("tfmg:steel_vat");
        params.allowedVatTypes.add("tfmg:firebrick_lined_vat");
        return params;
    }

    public VatMachineRecipeBuilder.VatRecipeParams centrifuge() {
        VatMachineRecipeBuilder.VatRecipeParams params = new VatMachineRecipeBuilder.VatRecipeParams();
        params.machines.add("tfmg:centrifuge");
        return params;
    }

    public VatMachineRecipeBuilder.VatRecipeParams freezing() {
        VatMachineRecipeBuilder.VatRecipeParams params = new VatMachineRecipeBuilder.VatRecipeParams();
        params.machines.add("tfmg:freezing");
        return params;
    }

    public VatMachineRecipeBuilder.VatRecipeParams intenseFreezing() {
        VatMachineRecipeBuilder.VatRecipeParams params = new VatMachineRecipeBuilder.VatRecipeParams();
        params.machines.add("tfmg:freezing");
        params.machines.add("tfmg:freezing");
        params.machines.add("tfmg:freezing");
        return params;
    }

    public VatMachineRecipeBuilder.VatRecipeParams arcBlasting() {
        VatMachineRecipeBuilder.VatRecipeParams params = new VatMachineRecipeBuilder.VatRecipeParams();
        params.machines.add("tfmg:graphite_electrode");
        params.machines.add("tfmg:graphite_electrode");
        params.machines.add("tfmg:graphite_electrode");
        params.minSize = 9;
        params.allowedVatTypes = new ArrayList<>();
        params.allowedVatTypes.add("tfmg:firebrick_lined_vat");
        return params;
    }

    public VatMachineRecipeBuilder.VatRecipeParams flotation() {
        VatMachineRecipeBuilder.VatRecipeParams params = new VatMachineRecipeBuilder.VatRecipeParams();
        params.machines.add("tfmg:mixing");
        params.machines.add("metallurgica:flotation_cell");
        return params;
    }
}
