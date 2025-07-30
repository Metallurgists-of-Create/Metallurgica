package dev.metallurgists.metallurgica.compat.rutile.handlers.material;

import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.rutile.scrapping.IScrappable;
import dev.metallurgists.metallurgica.compat.rutile.scrapping.ScrappingData;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.BlockFlag;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.ItemFlag;
import dev.metallurgists.rutile.registry.RutileFlagKeys;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.function.Consumer;

public class ScrappingHandler {
    private ScrappingHandler() {}

    public static void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        processCrushing(provider, material);
    }

    private static void processCrushing(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        for (FlagKey<?> flagKey : material.getFlags().getFlagKeys()) {
            if (material.getFlag(flagKey) instanceof IScrappable scrappable) {
                if (material.getFlag(flagKey) instanceof ItemFlag itemFlag) {
                    var inputId = itemFlag.getExistingId(material);
                    crush(provider, material, inputId, scrappable);
                }
                if (material.getFlag(flagKey) instanceof BlockFlag blockFlag) {
                    var inputId = blockFlag.getExistingId(material);
                    crush(provider, material, inputId, scrappable);
                }
            }
        }
    }

    private static void crush(@NotNull Consumer<FinishedRecipe> provider, Material material, ResourceLocation inputId, IScrappable scrappable) {
        ProcessingRecipeBuilder<CrushingRecipe> builder = new Builder<>(material.getNamespace(), CrushingRecipe::new, inputId.getPath(), provider);
        builder.require(BuiltInRegistries.ITEM.get(inputId));
        int outputs = 0;
        ScrappingData scrappingData = scrappable.getScrappingData(material);
        for (ScrappingData.ScrappingOutput output : scrappingData.getOutputs()) {
            Material scrapsInto = output.material();
            float chance = 1.0f - output.discardChance();
            int mainAmount = output.amount() - output.discardAmount();
            if (!scrapsInto.hasFlag(RutileFlagKeys.DUST)) continue;
            var dustId = scrapsInto.getFlag(RutileFlagKeys.DUST).getExistingId(scrapsInto);
            builder.output(BuiltInRegistries.ITEM.get(dustId), mainAmount);
            builder.output(chance, BuiltInRegistries.ITEM.get(dustId), output.discardAmount());
            outputs++;
        }
        for (ScrappingData.ExtraScrappingOutput extraOutput : scrappingData.getExtras()) {
            float chance = extraOutput.chance();
            int amount = extraOutput.amount();
            ItemLike output = extraOutput.item();
            builder.output(chance, output, amount);
            outputs++;
        }
        if (outputs == 0) return;
        builder.averageProcessingDuration();
        builder.build();
    }


    private static class Builder<T extends ProcessingRecipe<?>> extends ProcessingRecipeBuilder<T> {
        Consumer<FinishedRecipe> consumer;
        public Builder(String modid, ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, String from, Consumer<FinishedRecipe> consumer) {
            super(factory, Metallurgica.asResource("scrapping/" + modid + "/" + from));
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
