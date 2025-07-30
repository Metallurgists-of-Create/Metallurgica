package dev.metallurgists.metallurgica.compat.rutile.flags.types.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.metallurgists.metallurgica.compat.rutile.flags.MetallurgicaFlagKeys;
import dev.metallurgists.metallurgica.compat.rutile.flags.objects.MaterialCogWheelBlock;
import dev.metallurgists.metallurgica.compat.rutile.flags.objects.MaterialCogWheelBlockItem;
import dev.metallurgists.metallurgica.foundation.config.server.subcat.MStress;
import dev.metallurgists.metallurgica.foundation.data.runtime.MetallurgicaDynamicResourcePack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.BlockFlag;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IBlockRegistry;
import dev.metallurgists.rutile.api.material.flag.types.IPartialHolder;
import dev.metallurgists.rutile.api.material.registry.block.IMaterialBlock;
import dev.metallurgists.rutile.util.helpers.ModelHelpers;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LargeCogWheelFlag extends BlockFlag implements IPartialHolder {
    @Getter
    private String cogWheelModelVariant = "steel";

    public LargeCogWheelFlag(String existingNamespace) {
        super("large_%s_cogwheel", existingNamespace);
        this.setTagPatterns(List.of("c:large_cogwheels", "c:large_cogwheels/%s", "minecraft:mineable/pickaxe"));
        this.setItemTagPatterns(List.of("c:large_cogwheels", "c:large_cogwheels/%s"));
    }

    public LargeCogWheelFlag() {
        this("");
    }

    public LargeCogWheelFlag variant(String variant) {
        this.cogWheelModelVariant = variant;
        return this;
    }

    @Override
    public BlockEntry<? extends IMaterialBlock> registerBlock(@NotNull Material material, IBlockRegistry flag, @NotNull AbstractRegistrate<?> registrate) {
        NonNullFunction<BlockBehaviour.Properties, MaterialCogWheelBlock> factory = (p) -> MaterialCogWheelBlock.large(material, flag, p);
        return registrate.block(getIdPattern().formatted(material.getName()), factory)
                .initialProperties(SharedProperties::stone)
                .properties((p) -> p.sound(SoundType.COPPER))
                .setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .addLayer(() -> RenderType::cutoutMipped)
                .transform(MStress.setNoImpact())
                .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                .item(MaterialCogWheelBlockItem::new)
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .build().register();
    }

    @Override
    public void registerBlockAssets(Material material) {
        JsonElement obj = getModel(material);
        if (obj == null) return;
        MetallurgicaDynamicResourcePack.addBlockModel(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), obj);
        MetallurgicaDynamicResourcePack.addBlockState(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), ModelHelpers.simpleAxisBlockstate("metallurgica:block/" + getIdPattern().formatted(material.getName())));
        MetallurgicaDynamicResourcePack.addItemModel(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), ModelHelpers.simpleParentedModel(material.getNamespace() + ":block/" + getIdPattern().formatted(material.getName())));
    }

    @Override
    public boolean shouldHaveComposition() {
        return false;
    }

    @Override
    public FlagKey<?> getKey() {
        return MetallurgicaFlagKeys.LARGE_COG_WHEEL;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(MetallurgicaFlagKeys.COG_WHEEL);
    }

    @Override
    public ResourceLocation getModelLocation(Material material) {
        return new ResourceLocation(material.getNamespace(), "large_cogwheel/" + material.getName());
    }

    @Override
    public JsonElement createModel(Material material) {
        boolean texturePresent = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/block/materials/" + material.getName() + "/large_cogwheel.png")).isPresent();
        String texture = texturePresent ? material.getNamespace() + ":block/materials/" + material.getName() + "/large_cogwheel" : "rutile:block/materials/null/large_cogwheel";
        JsonObject model = new JsonObject();
        model.addProperty("parent", "metallurgica:block/template/cogwheel/"+cogWheelModelVariant+"/large_shaftless");
        JsonObject textures = new JsonObject();
        textures.addProperty("texture", texture);
        model.add("textures", textures);
        return model;
    }

    private JsonElement getModel(Material material) {
        boolean texturePresent = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/block/materials/" + material.getName() + "/large_cogwheel.png")).isPresent();
        String texture = texturePresent ? material.getNamespace() + ":block/materials/" + material.getName() + "/large_cogwheel" : "rutile:block/materials/null/large_cogwheel";
        JsonObject model = new JsonObject();
        model.addProperty("parent", "metallurgica:block/template/cogwheel/"+cogWheelModelVariant+"/large");
        JsonObject textures = new JsonObject();
        textures.addProperty("texture", texture);
        model.add("textures", textures);
        return model;
    }
}
