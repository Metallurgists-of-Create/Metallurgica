package dev.metallurgists.metallurgica.foundation.data.runtime.recipe;

import dev.metallurgists.metallurgica.foundation.data.runtime.recipe.handler.*;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.IRecipeHandler;
import dev.metallurgists.metallurgica.registry.material.MetMaterials;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;
import java.util.function.Consumer;

public class MetallurgicaRecipes {
    public static final Set<ResourceLocation> RECIPE_FILTERS = new ObjectOpenHashSet<>();

    public static void recipeAddition(Consumer<FinishedRecipe> originalConsumer) {
        Consumer<FinishedRecipe> consumer = recipe -> {
            if (!RECIPE_FILTERS.contains(recipe.getId())) {
                originalConsumer.accept(recipe);
            }
        };

        for (Material material : MetMaterials.registeredMaterials.values()) {
            for (FlagKey<?> flagKey : material.getFlags().getFlagKeys()) {
                var flag = material.getFlag(flagKey);
                if (flag instanceof IRecipeHandler handler) {
                    handler.run(consumer, material);
                }
            }
            PressingRecipeHandler.run(consumer, material);
            SequencedAssemblyHandler.run(consumer, material);
            StorageRecipeHandler.run(consumer, material);

            ScrappingHandler.run(consumer, material);
        }

    }

    public static void recipeRemoval() {
        RECIPE_FILTERS.clear();

        MetallurgicaRecipeRemoval.init(RECIPE_FILTERS::add);
    }
}
