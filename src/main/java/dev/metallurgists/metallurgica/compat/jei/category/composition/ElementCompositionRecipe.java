package dev.metallurgists.metallurgica.compat.jei.category.composition;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.registry.MetallurgicaRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import dev.metallurgists.rutile.api.composition.SubComposition;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.util.helpers.MaterialHelpers;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ElementCompositionRecipe extends ProcessingRecipe<RecipeWrapper> {
    @Nullable
    public Item item;
    @Nullable
    public Material material;
    @Getter
    public List<SubComposition> subCompositions;

    static int counter = 0;

    public ElementCompositionRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(MetallurgicaRecipeTypes.item_composition, params);
    }

    public static ElementCompositionRecipe create(List<SubComposition> subCompositions, @Nullable Item item, @Nullable Material material) {
        ResourceLocation recipeId = Metallurgica.asResource("element_composition_" + counter++);
        if (item != null && material != null) {
            Metallurgica.LOGGER.error("Item Composition cannot have both item and material set at the same time.");
            return null;
        }
        if (item != null) {
            return new ProcessingRecipeBuilder<>((params) -> new ElementCompositionRecipe(params).set(item,subCompositions), recipeId)
                    .build();
        } else if (material != null) {
            return new ProcessingRecipeBuilder<>((params) -> new ElementCompositionRecipe(params).set(material,subCompositions), recipeId)
                    .build();
        }
        return null;
    }

    public ElementCompositionRecipe set(Item item, List<SubComposition> subCompositions) {
        this.item = item;
        this.subCompositions = subCompositions;
        return this;
    }

    public ElementCompositionRecipe set(Material material, List<SubComposition> subCompositions) {
        this.material = material;
        this.subCompositions = subCompositions;
        return this;
    }

    @Nullable
    public Item getItem() {
        return item;
    }

    @Nullable
    public Material getMaterial() {
        return material;
    }

    public List<ItemStack> getAllMaterialItems() {
        if (material == null) {
            Metallurgica.LOGGER.error("Material is null, cannot get all items for null material.");
            return new ArrayList<>();
        }
        return MaterialHelpers.getAllMaterialItemsForTooltips(material).stream().map(Item::getDefaultInstance).toList();
    }


    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }

    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        return false;
    }
}
