package dev.metallurgists.metallurgica.compat.rutile.flags.types.item;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.metallurgica.compat.rutile.flags.MetallurgicaFlagKeys;
import dev.metallurgists.metallurgica.compat.rutile.flags.objects.SequencedAssemblyMaterialItem;
import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.ItemFlag;
import dev.metallurgists.rutile.api.material.flag.types.IItemRegistry;
import dev.metallurgists.rutile.api.material.registry.item.IMaterialItem;
import dev.metallurgists.rutile.util.helpers.ModelHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

public class SemiPressedSheetFlag extends ItemFlag {

    public SemiPressedSheetFlag(String existingNamespace) {
        super("semi_pressed_%s_sheet", existingNamespace);
    }

    public SemiPressedSheetFlag() {
        this("");
    }

    @Override
    public ItemEntry<? extends IMaterialItem> registerItem(@NotNull Material material, IItemRegistry flag, @NotNull AbstractRegistrate<?> registrate) {
        return registrate
                .item(getIdPattern().formatted(material.getName()), (p) -> new SequencedAssemblyMaterialItem(p, material, flag))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .register();
    }

    @Override
    public void registerItemAssets(Material material) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        String namespace = material.getNamespace();
        boolean texturePresent = resourceManager.getResource(new ResourceLocation(namespace + ":textures/item/materials/" + material.getName() + "/semi_pressed_sheet.png")).isPresent();
        String texture = texturePresent ? material.getNamespace() + ":item/materials/" + material.getName() + "/semi_pressed_sheet" : "rutile:item/materials/null/semi_pressed_sheet";
        RutileDynamicResourcePack.addItemModel(new ResourceLocation(material.getNamespace(), this.getIdPattern().formatted(material.getName())), ModelHelpers.simpleGeneratedModel("minecraft:item/generated", texture));
    }

    @Override
    public FlagKey<?> getKey() {
        return MetallurgicaFlagKeys.SEMI_PRESSED_SHEET;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(MetallurgicaFlagKeys.SHEET, true);
    }
}
