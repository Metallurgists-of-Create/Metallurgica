package dev.metallurgists.metallurgica.compat.rutile.handlers;

import dev.metallurgists.metallurgica.compat.rutile.handlers.material.ScrappingHandler;
import dev.metallurgists.rutile.api.dynamic_pack.data.recipe.handler.IRutileMaterialRecipeHandler;
import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraft.data.recipes.FinishedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class MetallurgicaMaterialRecipes implements IRutileMaterialRecipeHandler {
    @Override
    public void run(@NotNull Consumer<FinishedRecipe> consumer, @NotNull Material material) {
        ScrappingHandler.run(consumer, material);
    }
}
