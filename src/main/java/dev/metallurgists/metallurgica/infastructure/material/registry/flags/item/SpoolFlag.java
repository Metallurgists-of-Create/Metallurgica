package dev.metallurgists.metallurgica.infastructure.material.registry.flags.item;

import com.drmangotea.tfmg.config.TFMGResistivity;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableType;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableTypeEntry;
import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnection;
import com.drmangotea.tfmg.registry.TFMGCableTypes;
import com.drmangotea.tfmg.registry.TFMGItems;
import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.material.item.IMaterialItem;
import dev.metallurgists.metallurgica.foundation.material.item.MaterialSpoolItem;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IItemRegistry;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IPartialHolder;
import dev.metallurgists.metallurgica.infastructure.material.scrapping.IScrappable;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.MaterialHelper;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.IRecipeHandler;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.MaterialFlags;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.infastructure.material.scrapping.ScrappingData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import lombok.Getter;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.theme.Color;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

import static dev.metallurgists.metallurgica.foundation.data.runtime.assets.MetallurgicaModels.isDeleteMePresent;
import static dev.metallurgists.metallurgica.infastructure.material.MaterialHelper.getNameForRecipe;

public class SpoolFlag extends ItemFlag implements IRecipeHandler, IScrappable, IPartialHolder {

    @Getter
    private int color;

    @Getter
    private double resistivity;

    public SpoolFlag(double resistivity, int color) {
        this("metallurgica");
        this.resistivity = resistivity;
        this.color = color;
        this.setTagPatterns(List.of("metallurgica:spools", "metallurgica:spools/%s"));
    }

    public SpoolFlag(String existingNamespace) {
        super("%s_spool", existingNamespace);
    }

    @Override
    public FlagKey<?> getKey() {
        return FlagKey.SPOOL;
    }

    @Override
    public ItemEntry<? extends IMaterialItem> registerItem(@NotNull Material material, IItemRegistry flag, @NotNull MetallurgicaRegistrate registrate) {
        Color colour = new Color(color);
        ResourceLocation cableTypeKey = new ResourceLocation(registrate.getModid(), material.getName());
        ItemEntry<? extends IMaterialItem> spool = registrate.item(getIdPattern().formatted(material.getName()), (p) -> new MaterialSpoolItem(p, colour.getRGB(), cableTypeKey, material, flag))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .register();
        CableTypeEntry<CableType> cableType = registrate.cableType(material.getName(), CableType::new)
                .properties(p -> p.color(colour.getRGB()).spool(spool))
                .transform(TFMGResistivity.setResistivity(getResistivity()))
                .register();
        return spool;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(FlagKey.WIRE, true);
    }

    @Override
    public void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        var wire = MaterialHelper.getItem(material, FlagKey.WIRE);
        var spool = MaterialHelper.getItem(material, getKey());
        SpoolFlag spoolFlag = (SpoolFlag) material.getFlag(getKey());
        if (!spoolFlag.getExistingId(material).getNamespace().equals(material.getNamespace())) return;
        ShapedRecipeBuilder builder = new ShapedRecipeBuilder(RecipeCategory.MISC, spool, 1);
        builder.pattern("WWW").pattern("WSW").pattern("WWW")
                .define('W', wire).define('S', TFMGItems.EMPTY_SPOOL);
        builder.unlockedBy("has_wire", InventoryChangeTrigger.TriggerInstance.hasItems(wire));
        String recipePath = material.asResourceString(getNameForRecipe(material, getKey()) + "_from_" + getNameForRecipe(material, FlagKey.WIRE));
        builder.save(provider,  Metallurgica.asResource("runtime_generated/" + recipePath));
    }

    @Override
    public ScrappingData getScrappingData(Material mainMaterial) {
        return ScrappingData.create()
                .addOutput(mainMaterial, 3, 1, 0.5f)
                .addExtraOutput(Items.STICK, 0.25f)
                .addExtraOutput(mainMaterial, FlagKey.WIRE, 1, 0.15f);
    }

    @Override
    public ResourceLocation getModelLocation(Material material) {
        return new ResourceLocation(material.getNamespace(), "winding_machine/%s_spool".formatted(material.getName()));
    }

    @Override
    public JsonElement createModel(Material material) {
        boolean texturePresent = isDeleteMePresent() && Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/item/materials/" + material.getName() + "/spool_partial.png")).isPresent();
        String texture = texturePresent ? "metallurgica:item/materials/" + material.getName() + "/spool_partial" : "metallurgica:item/materials/null/spool_partial";
        JsonObject model = new JsonObject();
        model.addProperty("parent", "metallurgica:block/template/spool/spool");
        JsonObject textures = new JsonObject();
        textures.addProperty("texture", texture);
        model.add("textures", textures);
        return model;
    }
}
