package dev.metallurgists.metallurgica.compat.rutile.flags.types.item;

import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.rutile.flags.MetallurgicaFlagKeys;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.block.SheetmetalFlag;
import dev.metallurgists.metallurgica.compat.rutile.handlers.CreateRecipeHelpers;
import dev.metallurgists.metallurgica.compat.rutile.handlers.RuntimeBuilder;
import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.ItemFlag;
import dev.metallurgists.rutile.api.material.flag.types.IItemRegistry;
import dev.metallurgists.rutile.api.material.flag.types.IRecipeHandler;
import dev.metallurgists.rutile.api.material.registry.item.IMaterialItem;
import dev.metallurgists.rutile.api.material.registry.item.MaterialItem;
import dev.metallurgists.rutile.registry.RutileFlagKeys;
import dev.metallurgists.rutile.util.helpers.MaterialHelpers;
import dev.metallurgists.rutile.util.helpers.ModelHelpers;
import dev.metallurgists.rutile.util.helpers.RecipeHelpers;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

import static dev.metallurgists.metallurgica.infastructure.material.MaterialHelper.getNameForRecipe;

public class SheetFlag extends ItemFlag implements IRecipeHandler {

    @Getter
    private int pressTimes = 1;

    @Getter
    public boolean needsTransitional = false;

    public SheetFlag(String existingNamespace) {
        super("%s_sheet", existingNamespace);
        this.setTagPatterns(List.of("c:plates", "c:plates/%s"));
    }

    public SheetFlag() {
        this("");
    }



    public SheetFlag pressTimes(int pressTimes) {
        this.pressTimes = pressTimes;
        this.needsTransitional = pressTimes > 1;
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
        boolean texturePresent = resourceManager.getResource(new ResourceLocation(namespace + ":textures/item/materials/" + material.getName() + "/sheet.png")).isPresent();
        String texture = texturePresent ? material.getNamespace() + ":item/materials/" + material.getName() + "/sheet" : "rutile:item/materials/null/sheet";
        RutileDynamicResourcePack.addItemModel(new ResourceLocation(material.getNamespace(), this.getIdPattern().formatted(material.getName())), ModelHelpers.simpleGeneratedModel("minecraft:item/generated", texture));
    }

    @Override
    public FlagKey<?> getKey() {
        return MetallurgicaFlagKeys.SHEET;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        if (needsTransitional) {
            flags.ensureSet(MetallurgicaFlagKeys.SEMI_PRESSED_SHEET, true);
        }
        flags.ensureSet(RutileFlagKeys.INGOT, true);
    }

    @Override
    public void run(@NotNull Consumer<FinishedRecipe> consumer, @NotNull Material material) {
        boolean runSheetmetal = !MaterialHelpers.hasExternalId(material, MetallurgicaFlagKeys.SHEETMETAL);
        if (material.hasFlag(MetallurgicaFlagKeys.SHEETMETAL) && runSheetmetal) {
            SheetmetalFlag sheetmetalFlag = material.getFlag(MetallurgicaFlagKeys.SHEETMETAL);
            Item sheet = MaterialHelpers.getItem(material, MetallurgicaFlagKeys.SHEET);
            Block sheetmetal = MaterialHelpers.getBlock(material, MetallurgicaFlagKeys.SHEETMETAL);
            if (sheetmetalFlag.isRequiresCompacting()) {
                String compactPath = material.asResourceString(MaterialHelpers.getNameForRecipe(material, MetallurgicaFlagKeys.SHEETMETAL) + "_from_sheets");
                String decompactPath = material.asResourceString(MaterialHelpers.getNameForRecipe(material, getKey()) + "_from_sheetmetal");
                CreateRecipeHelpers.compact(consumer, sheet, sheetmetal, 9, material, compactPath);
                CreateRecipeHelpers.decompact(consumer, sheetmetal, sheet, 9, material, decompactPath);
            } else {
                RecipeHelpers.craftCompact(consumer, sheet, sheetmetal, 9, material, "%s_sheetmetal_from_sheets");
                RecipeHelpers.craftDecompact(consumer, sheetmetal, sheet, 9, material, "%s_sheets_from_sheetmetal");
            }
        }
        // Only check this now so sheetmetal can run
        if (MaterialHelpers.hasExternalId(material, MetallurgicaFlagKeys.SHEET)) return;

        Item ingot = MaterialHelpers.getItem(material, RutileFlagKeys.INGOT);
        Item sheet = MaterialHelpers.getItem(material, MetallurgicaFlagKeys.SHEET);
        if (needsTransitional) {
            SheetFlag sheetFlag = material.getFlag(MetallurgicaFlagKeys.SHEET);
            Item transitional = MaterialHelpers.getItem(material, MetallurgicaFlagKeys.SEMI_PRESSED_SHEET);
            SequencedAssemblyRecipeBuilder builder = new SequencedAssemblyRecipeBuilder(Metallurgica.asResource("sequenced_assembly/runtime_generated/" + material.getNamespace() + "/" + material.getName() + "_sheet"));
            builder.transitionTo(transitional);
            builder.require(ingot);
            for (int i = 0; i < sheetFlag.getPressTimes(); i++) {
                builder.addStep(PressingRecipe::new, rb -> rb);
            }
            builder.loops(1);
            builder.addOutput(sheet, 1);
            builder.build(consumer);
        } else {
            String recipePath = material.asResourceString(MaterialHelpers.getNameForRecipe(material, getKey()) + "_from_ingot");
            ProcessingRecipeBuilder<PressingRecipe> builder = new RuntimeBuilder<>(PressingRecipe::new, consumer, recipePath);
            builder.require(ingot).output(sheet).build();
        }
    }
}
