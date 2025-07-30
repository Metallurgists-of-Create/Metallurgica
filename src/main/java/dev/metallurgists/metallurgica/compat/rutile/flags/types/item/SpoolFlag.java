package dev.metallurgists.metallurgica.compat.rutile.flags.types.item;

import com.drmangotea.tfmg.config.TFMGResistivity;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableType;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableTypeBuilder;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableTypeEntry;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.rutile.flags.MetallurgicaFlagKeys;
import dev.metallurgists.metallurgica.compat.rutile.flags.objects.MaterialSpoolItem;
import dev.metallurgists.metallurgica.compat.rutile.scrapping.IScrappable;
import dev.metallurgists.metallurgica.compat.rutile.scrapping.ScrappingData;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
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
import net.createmod.catnip.theme.Color;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class SpoolFlag extends ItemFlag implements IRecipeHandler, IScrappable {

    @Getter
    private int color;

    @Getter
    private double resistivity;

    public SpoolFlag(String existingNamespace) {
        super("%s_spool", existingNamespace);
    }

    public SpoolFlag(double resistivity, int color) {
        this("");
        this.resistivity = resistivity;
        this.color = color;
        this.setTagPatterns(List.of("metallurgica:spools", "metallurgica:spools/%s"));
    }

    @Override
    public ItemEntry<? extends IMaterialItem> registerItem(@NotNull Material material, IItemRegistry flag, @NotNull AbstractRegistrate<?> registrate) {
        Color colour = new Color(color);
        ResourceLocation cableTypeKey = new ResourceLocation(registrate.getModid(), material.getName());
        ItemEntry<? extends IMaterialItem> spool = registrate.item(getIdPattern().formatted(material.getName()), (p) -> new MaterialSpoolItem(p, colour.getRGB(), cableTypeKey, material, flag))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .register();
        CableTypeEntry<CableType> cableType = cableType(material.getName(), CableType::new, registrate)
                .properties(p -> p.color(colour.getRGB()).spool(spool))
                .transform(TFMGResistivity.setResistivity(getResistivity()))
                .register();

        return spool;
    }

    @Override
    public void registerItemAssets(Material material) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        String namespace = material.getNamespace();
        boolean texturePresent = resourceManager.getResource(new ResourceLocation(namespace + ":textures/item/materials/" + material.getName() + "/spool.png")).isPresent();
        String texture = texturePresent ? material.getNamespace() + ":item/materials/" + material.getName() + "/spool" : "rutile:item/materials/null/spool";
        RutileDynamicResourcePack.addItemModel(new ResourceLocation(material.getNamespace(), this.getIdPattern().formatted(material.getName())), ModelHelpers.simpleGeneratedModel("minecraft:item/generated", texture));
    }

    @Override
    public FlagKey<?> getKey() {
        return MetallurgicaFlagKeys.SPOOL;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(MetallurgicaFlagKeys.WIRE, true);
    }

    @Override
    public void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        var wire = MaterialHelpers.getItem(material, MetallurgicaFlagKeys.WIRE);
        var spool = MaterialHelpers.getItem(material, getKey());
        SpoolFlag spoolFlag = (SpoolFlag) material.getFlag(getKey());
        if (!spoolFlag.getExistingId(material).getNamespace().equals(material.getNamespace())) return;
        ShapedRecipeBuilder builder = new ShapedRecipeBuilder(RecipeCategory.MISC, spool, 1);
        builder.pattern("WWW").pattern("WSW").pattern("WWW")
                .define('W', wire).define('S', TFMGItems.EMPTY_SPOOL);
        builder.unlockedBy("has_wire", InventoryChangeTrigger.TriggerInstance.hasItems(wire));
        String recipePath = material.asResourceString(MaterialHelpers.getNameForRecipe(material, getKey()) + "_from_" + MaterialHelpers.getNameForRecipe(material, MetallurgicaFlagKeys.WIRE));
        builder.save(provider,  Metallurgica.asResource("runtime_generated/" + recipePath));
    }

    @Override
    public ScrappingData getScrappingData(Material mainMaterial) {
        return ScrappingData.create()
                .addOutput(mainMaterial, 3, 1, 0.5f)
                .addExtraOutput(Items.STICK, 0.25f)
                .addExtraOutput(mainMaterial, MetallurgicaFlagKeys.WIRE, 1, 0.15f);
    }

    public <T extends CableType> CableTypeBuilder<T, AbstractRegistrate<?>> cableType(String name, NonNullFunction<CableType.Properties, T> factory, AbstractRegistrate<?> registrate) {
        return cableType(registrate, name, factory, registrate);
    }

    public <T extends CableType, P> CableTypeBuilder<T, P> cableType(P parent, String name, NonNullFunction<CableType.Properties, T> factory, AbstractRegistrate<?> registrate) {
        return registrate.entry(name, callback -> CableTypeBuilder.create(registrate, parent, name, callback, factory));
    }
}
