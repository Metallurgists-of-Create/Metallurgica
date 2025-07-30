package dev.metallurgists.metallurgica.compat.rutile.handlers;

import com.simibubi.create.content.kinetics.mixer.CompactingRecipe;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import dev.metallurgists.rutile.api.material.base.Material;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CreateRecipeHelpers {


    public static void compact(@NotNull Consumer<FinishedRecipe> provider, ItemLike input, ItemLike output, int amountIn, Material material, String recipeId) {
        ProcessingRecipeBuilder<CompactingRecipe> builder = new RuntimeBuilder<>(CompactingRecipe::new, provider, recipeId);
        for (int i = 0; i < amountIn; i++) {
            builder.require(input);
        }
        builder.output(output).build();
    }

    public static void decompact(@NotNull Consumer<FinishedRecipe> provider, ItemLike input, ItemLike output, int amountOut, Material material, String recipeId) {
        ProcessingRecipeBuilder<CuttingRecipe> builder = new RuntimeBuilder<>(CuttingRecipe::new, provider, recipeId);
        builder.require(input);
        builder.output(output, amountOut).build();
    }
}
