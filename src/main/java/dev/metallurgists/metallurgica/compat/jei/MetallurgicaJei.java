package dev.metallurgists.metallurgica.compat.jei;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.jei.category.ceramic_mixing.CeramicMixingCategory;
import dev.metallurgists.metallurgica.compat.jei.category.composition.ElementCompositionCategory;
import dev.metallurgists.metallurgica.compat.jei.category.composition.ElementCompositionRecipe;
import dev.metallurgists.metallurgica.compat.jei.category.RecipeCategoryBuilder;
import dev.metallurgists.metallurgica.compat.jei.category.reaction.fluid.FluidReactionCategory;
import dev.metallurgists.metallurgica.compat.jei.category.scrapping.ScrappingCategory;
import dev.metallurgists.metallurgica.compat.jei.category.shaking.ShakingCategory;
import dev.metallurgists.metallurgica.compat.jei.custom.element.ElementIngredientHelper;
import dev.metallurgists.metallurgica.compat.jei.custom.element.ElementIngredientRenderer;
import dev.metallurgists.metallurgica.content.machines.shaking_table.ShakingRecipe;
import dev.metallurgists.metallurgica.content.primitive.ceramic.ceramic_mixing_pot.CeramicMixingRecipe;
import dev.metallurgists.metallurgica.infastructure.material.MaterialHelper;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.registry.MetallurgicaBlocks;
import dev.metallurgists.metallurgica.registry.MetallurgicaFluids;
import dev.metallurgists.metallurgica.registry.MetallurgicaItems;
import dev.metallurgists.metallurgica.registry.MetallurgicaRecipeTypes;
import dev.metallurgists.metallurgica.registry.material.MetMaterials;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaRegistries;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.helpers.IColorHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@JeiPlugin
public class MetallurgicaJei implements IModPlugin {

    private static final ResourceLocation ID = Metallurgica.asResource("jei_plugin");
    
    protected final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();
    protected IIngredientManager ingredientManager;
    
    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }
    
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        loadCategories(registration);
        registration.addRecipeCategories(allCategories.toArray(IRecipeCategory[]::new));
        registration.addRecipeCategories(new FluidReactionCategory(registration.getJeiHelpers().getGuiHelper()));
    }
    
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ingredientManager = registration.getIngredientManager();
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        allCategories.forEach(c -> c.registerRecipes(registration));
        //registration.addRecipes(MetallurgicaJeiRecipeTypes.FLUID_REACTION, recipeManager.getAllRecipesFor(MetallurgicaSpecialRecipes.FLUID_REACTION.get()));
    }
    
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        allCategories.forEach(c -> c.registerCatalysts(registration));
        registration.addRecipeCatalysts(MetallurgicaJeiRecipeTypes.FLUID_REACTION, new ItemStack(Items.BUCKET), new ItemStack(AllBlocks.BASIN));
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        IColorHelper colorHelper = registration.getColorHelper();
        registration.register(MetallurgicaJeiConstants.ELEMENT, MetallurgicaRegistries.BasicRegistries.registeredElements.values(), new ElementIngredientHelper(colorHelper), new ElementIngredientRenderer(16));
    }

    @Override
    public void registerIngredientAliases(IIngredientAliasRegistration registration) {
        registration.addAliases(
                MetallurgicaJeiConstants.ELEMENT,
                MetallurgicaRegistries.BasicRegistries.registeredElements.values(),
                "element");
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        List<FluidStack> fluidIngredients = new ArrayList<>();
        fluidIngredients.add(new FluidStack(MetallurgicaFluids.preheatedAir.get().getSource(), FluidType.BUCKET_VOLUME));
        fluidIngredients.add(new FluidStack(MetallurgicaFluids.nitrogen.get().getSource(), FluidType.BUCKET_VOLUME));
        fluidIngredients.add(new FluidStack(MetallurgicaFluids.bfg.get().getSource(), FluidType.BUCKET_VOLUME));
        fluidIngredients.add(new FluidStack(MetallurgicaFluids.cryolite.get().getSource(), FluidType.BUCKET_VOLUME));
        fluidIngredients.add(new FluidStack(MetallurgicaFluids.decontaminatedWater.get().getSource(), FluidType.BUCKET_VOLUME));
        fluidIngredients.add(new FluidStack(MetallurgicaFluids.magnetiteFines.get().getSource(), FluidType.BUCKET_VOLUME));

        jeiRuntime.getIngredientManager().addIngredientsAtRuntime(ForgeTypes.FLUID_STACK,fluidIngredients);
    }
    
    private static <T extends Recipe<?>> RecipeCategoryBuilder<T> builder(Class<T> cls) {
        return new RecipeCategoryBuilder<>(Metallurgica.ID, cls);
    }

    private void loadCategories(IRecipeCategoryRegistration registration) {
        allCategories.clear();
        allCategories.add(
                builder(CrushingRecipe.class)
                        .addTypedRecipes(AllRecipeTypes.CRUSHING)
                        .catalyst(AllBlocks.CRUSHING_WHEEL::get)
                        .doubleItemIcon(AllBlocks.CRUSHING_WHEEL.get(), MaterialHelper.getItem(MetMaterials.COPPER.get(), FlagKey.DUST))
                        .emptyBackground(177, 100)
                        .build("scrapping", ScrappingCategory::new)
        );
        allCategories.add(
                builder(ElementCompositionRecipe.class)
                        .addRecipes(() -> ElementCompositionCategory.COMPOSITIONS)
                        .emptyBackground(177, 50)
                        .build("item_compositions", ElementCompositionCategory::new)
        );
        allCategories.add(
                builder(ShakingRecipe.class)
                        .addTypedRecipes(MetallurgicaRecipeTypes.shaking)
                        .catalyst(MetallurgicaBlocks.shakingTable::get)
                        .itemIcon(MetallurgicaBlocks.shakingTable.get())
                        .emptyBackground(177, 70)
                        .build("shaking", ShakingCategory::new)
        );
        allCategories.add(
                builder(CeramicMixingRecipe.class)
                        .addTypedRecipes(MetallurgicaRecipeTypes.ceramic_mixing)
                        .catalyst(MetallurgicaBlocks.ceramicMixingPot::get)
                        .doubleItemIcon(MetallurgicaBlocks.ceramicMixingPot.get(), MetallurgicaItems.dirtyClayBall.get())
                        .emptyBackground(177, 103)
                        .build("ceramic_mixing", CeramicMixingCategory::new)
        );
    }
}
