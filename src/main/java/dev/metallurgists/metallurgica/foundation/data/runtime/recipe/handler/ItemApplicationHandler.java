package dev.metallurgists.metallurgica.foundation.data.runtime.recipe.handler;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.block.CasingFlag;
import dev.metallurgists.metallurgica.infastructure.material.MaterialHelper;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.function.Consumer;

public class ItemApplicationHandler {

    private ItemApplicationHandler() {}

    public static void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        //processApplication(provider, material);
    }

    private static void processApplication(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        if (material.hasFlag(FlagKey.CASING)) {
            CasingFlag casingFlag = material.getFlag(FlagKey.CASING);
            FlagKey<? extends ItemFlag> used = casingFlag.isUseSheet() ? FlagKey.SHEET : FlagKey.INGOT;
            Item usedItem = MaterialHelper.getItem(material, used);
            for (TagKey<Item> appliesOn : casingFlag.getToApplyOn()) {
                String appliesOnPath = appliesOn.location().getPath().replace("/", "_");
                ProcessingRecipeBuilder<ItemApplicationRecipe> builder = new Builder<>(material.getNamespace(), (params) -> new ItemApplicationRecipe(AllRecipeTypes.ITEM_APPLICATION, params), casingFlag.getIdPattern().formatted(material.getName()), appliesOnPath, provider);
                builder.require(appliesOn);
                builder.require(usedItem);
                builder.output(MaterialHelper.getBlock(material, FlagKey.CASING).asItem());
                builder.build();
            }
        }
    }

    private static void logRecipeSkip(ResourceLocation rubbleId) {
        Metallurgica.LOGGER.info("Skipping item application recipe for {} as it is not in the metallurgica namespace and likely already has one", rubbleId);
    }


    private static class Builder<T extends ProcessingRecipe<?>> extends ProcessingRecipeBuilder<T> {
        Consumer<FinishedRecipe> consumer;
        public Builder(String modid, ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, String casing, String tagPath, Consumer<FinishedRecipe> consumer) {
            super(factory, Metallurgica.asResource("runtime_generated/" + modid + "/" + casing + "_from_" + tagPath));
            this.consumer = consumer;
        }

        @Override
        public T build() {
            T t = super.build();
            DataGenResult<T> result = new DataGenResult<>(t, Collections.emptyList());
            consumer.accept(result);
            return t;
        }
    }
}
