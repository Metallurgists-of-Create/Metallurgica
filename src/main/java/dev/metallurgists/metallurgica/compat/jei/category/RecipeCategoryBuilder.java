package dev.metallurgists.metallurgica.compat.jei.category;

import dev.metallurgists.metallurgica.foundation.util.MetalLang;
import com.simibubi.create.compat.jei.CreateJEI;
import com.simibubi.create.compat.jei.DoubleItemIcon;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CRecipes;
import mezz.jei.api.gui.drawable.IDrawable;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class RecipeCategoryBuilder<T extends Recipe<?>> {
    private final String modid;
    private final Class<? extends T> recipeClass;
    private Predicate<CRecipes> predicate = cRecipes -> true;

    private IDrawable background;
    private IDrawable icon;

    private final List<Consumer<List<T>>> recipeListConsumers = new ArrayList<>();
    private final List<Supplier<? extends ItemStack>> catalysts = new ArrayList<>();

    public RecipeCategoryBuilder(String modid, Class<? extends T> recipeClass) {
        this.modid = modid;
        this.recipeClass = recipeClass;
    }

    public RecipeCategoryBuilder<T> enableIf(Predicate<CRecipes> predicate) {
        this.predicate = predicate;
        return this;
    }

    public RecipeCategoryBuilder<T> enableWhen(Function<CRecipes, ConfigBase.ConfigBool> configValue) {
        predicate = c -> configValue.apply(c).get();
        return this;
    }

    public RecipeCategoryBuilder<T> addRecipeListConsumer(Consumer<List<T>> consumer) {
        recipeListConsumers.add(consumer);
        return this;
    }

    public RecipeCategoryBuilder<T> addRecipes(Supplier<Collection<? extends T>> collection) {
        return addRecipeListConsumer(recipes -> recipes.addAll(collection.get()));
    }

    @SuppressWarnings("unchecked")
    public RecipeCategoryBuilder<T> addAllRecipesIf(Predicate<Recipe<?>> pred) {
        return addRecipeListConsumer(recipes -> CreateJEI.consumeAllRecipes(recipe -> {
            if (pred.test(recipe)) {
                recipes.add((T) recipe);
            }
        }));
    }

    public RecipeCategoryBuilder<T> addAllRecipesIf(Predicate<Recipe<?>> pred, Function<Recipe<?>, T> converter) {
        return addRecipeListConsumer(recipes -> CreateJEI.consumeAllRecipes(recipe -> {
            if (pred.test(recipe)) {
                recipes.add(converter.apply(recipe));
            }
        }));
    }

    public <O extends Recipe<?>> RecipeCategoryBuilder<T> addTransformedRecipes(Supplier<RecipeType<O>> recipeType, Function<O, T> converter) {
        return addRecipeListConsumer(recipes -> CreateJEI.<O>consumeTypedRecipes(recipe -> recipes.add(converter.apply(recipe)), recipeType.get()));
    }

    public RecipeCategoryBuilder<T> addTypedRecipes(IRecipeTypeInfo recipeTypeEntry) {
        return addTypedRecipes(recipeTypeEntry::getType);
    }

    public RecipeCategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType) {
        return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipes::add, recipeType.get()));
    }

    public RecipeCategoryBuilder<T> addTypedRecipesIf(Supplier<RecipeType<? extends T>> recipeType, Predicate<Recipe<?>> pred) {
        return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipe -> {
            if (pred.test(recipe)) {
                recipes.add(recipe);
            }
        }, recipeType.get()));
    }

    public RecipeCategoryBuilder<T> addTypedRecipesExcluding(Supplier<RecipeType<? extends T>> recipeType,
                                                             Supplier<RecipeType<? extends T>> excluded) {
        return addRecipeListConsumer(recipes -> {
            List<Recipe<?>> excludedRecipes = CreateJEI.getTypedRecipes(excluded.get());
            CreateJEI.<T>consumeTypedRecipes(recipe -> {
                for (Recipe<?> excludedRecipe : excludedRecipes) {
                    if (CreateJEI.doInputsMatch(recipe, excludedRecipe)) {
                        return;
                    }
                }
                recipes.add(recipe);
            }, recipeType.get());
        });
    }

    public RecipeCategoryBuilder<T> removeRecipes(Supplier<RecipeType<? extends T>> recipeType) {
        return addRecipeListConsumer(recipes -> {
            List<Recipe<?>> excludedRecipes = CreateJEI.getTypedRecipes(recipeType.get());
            recipes.removeIf(recipe -> {
                for (Recipe<?> excludedRecipe : excludedRecipes) {
                    if (CreateJEI.doInputsMatch(recipe, excludedRecipe)) {
                        return true;
                    }
                }
                return false;
            });
        });
    }

    public RecipeCategoryBuilder<T> catalystStack(Supplier<ItemStack> supplier) {
        catalysts.add(supplier);
        return this;
    }

    public RecipeCategoryBuilder<T> catalyst(Supplier<ItemLike> supplier) {
        return catalystStack(() -> new ItemStack(supplier.get()
                .asItem()));
    }

    public RecipeCategoryBuilder<T> icon(IDrawable icon) {
        this.icon = icon;
        return this;
    }

    public RecipeCategoryBuilder<T> itemIcon(ItemLike item) {
        icon(new ItemIcon(() -> new ItemStack(item)));
        return this;
    }

    public RecipeCategoryBuilder<T> doubleItemIcon(ItemLike item1, ItemLike item2) {
        icon(new DoubleItemIcon(() -> new ItemStack(item1), () -> new ItemStack(item2)));
        return this;
    }

    public RecipeCategoryBuilder<T> background(IDrawable background) {
        this.background = background;
        return this;
    }

    public RecipeCategoryBuilder<T> emptyBackground(int width, int height) {
        background(new EmptyBackground(width, height));
        return this;
    }

    public CreateRecipeCategory<T> build(String name, CreateRecipeCategory.Factory<T> factory) {
        Supplier<List<T>> recipesSupplier;
        if (predicate.test(AllConfigs.server().recipes)) {
            recipesSupplier = () -> {
                List<T> recipes = new ArrayList<>();
                for (Consumer<List<T>> consumer : recipeListConsumers)
                    consumer.accept(recipes);
                return recipes;
            };
        } else {
            recipesSupplier = Collections::emptyList;
        }
        ResourceLocation id = new ResourceLocation(modid, name);
        CreateRecipeCategory.Info<T> info = new CreateRecipeCategory.Info<>(
                new mezz.jei.api.recipe.RecipeType<>(id, recipeClass),
                MetalLang.fromRL("recipe", id).component(),
                background, icon, recipesSupplier, catalysts);
        return factory.create(info);
    }
}
