package dev.metallurgists.metallurgica.compat.rutile.flags.types.item;

import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.metallurgica.compat.rutile.flags.MetallurgicaFlagKeys;
import dev.metallurgists.metallurgica.compat.rutile.handlers.RuntimeBuilder;
import dev.metallurgists.metallurgica.foundation.data.runtime.RuntimeProcessingRecipeBuilder;
import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.ItemFlag;
import dev.metallurgists.rutile.api.material.flag.types.IItemRegistry;
import dev.metallurgists.rutile.api.material.flag.types.IRecipeHandler;
import dev.metallurgists.rutile.api.material.registry.item.IMaterialItem;
import dev.metallurgists.rutile.api.material.registry.item.MaterialItem;
import dev.metallurgists.rutile.util.helpers.MaterialHelpers;
import dev.metallurgists.rutile.util.helpers.ModelHelpers;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;


public class RubbleFlag extends ItemFlag implements IRecipeHandler {
    @Getter
    private boolean crushing = false;
    @Getter
    private float bonusChance = 0;

    public RubbleFlag(String existingNamespace) {
        super("%s_rubble", existingNamespace);
        this.setTagPatterns(List.of("c:rubble", "c:rubble/%s"));
    }

    public RubbleFlag() {
        this("");
    }

    public RubbleFlag crushing() {
        this.crushing = true;
        return this;
    }

    public RubbleFlag bonusChance(float chance) {
        this.bonusChance = chance;
        return this;
    }

    @Override
    public ItemEntry<? extends IMaterialItem> registerItem(@NotNull Material material, IItemRegistry flag, @NotNull AbstractRegistrate<?> registrate) {
        return registrate
                .item(flag.getIdPattern().formatted(material.getName()), (p) -> new MaterialItem(p, material, flag))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .register();
    }

    @Override
    public void registerItemAssets(Material material) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        String namespace = material.getNamespace();
        boolean texturePresent = resourceManager.getResource(new ResourceLocation(namespace + ":textures/item/materials/" + material.getName() + "/rubble.png")).isPresent();
        String texture = texturePresent ? material.getNamespace() + ":item/materials/" + material.getName() + "/rubble" : "rutile:item/materials/null/rubble";
        RutileDynamicResourcePack.addItemModel(new ResourceLocation(material.getNamespace(), this.getIdPattern().formatted(material.getName())), ModelHelpers.simpleGeneratedModel("minecraft:item/generated", texture));
    }

    @Override
    public FlagKey<?> getKey() {
        return MetallurgicaFlagKeys.RUBBLE;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(MetallurgicaFlagKeys.MINERAL, true);
    }

    @Override
    public void run(@NotNull Consumer<FinishedRecipe> consumer, @NotNull Material material) {
        var mineral = MaterialHelpers.getItem(material, MetallurgicaFlagKeys.MINERAL);
        var rubble = MaterialHelpers.getItem(material, getKey());
        if (isCrushing()) {
            String recipePath = material.asResourceString(MaterialHelpers.getNameForRecipe(material, getKey()) + "_from_" + MaterialHelpers.getNameForRecipe(material, MetallurgicaFlagKeys.MINERAL));
            ProcessingRecipeBuilder<CrushingRecipe> builder = new RuntimeBuilder<>(CrushingRecipe::new, consumer, recipePath);
            builder.require(mineral);
            builder.output(rubble);
            if (getBonusChance() > 0) {
                builder.output(getBonusChance(), rubble);
            }
            builder.averageProcessingDuration();
            builder.build();
        }
    }
}
