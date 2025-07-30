package dev.metallurgists.metallurgica.compat.rutile.flags.types.block;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.metallurgica.compat.rutile.flags.MetallurgicaFlagKeys;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.IHaveConnectedTextures;
import dev.metallurgists.metallurgica.compat.rutile.handlers.RuntimeBuilder;
import dev.metallurgists.metallurgica.foundation.data.runtime.RuntimeProcessingRecipeBuilder;
import dev.metallurgists.metallurgica.registry.MetallurgicaSpriteShifts;
import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.BlockFlag;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IBlockRegistry;
import dev.metallurgists.rutile.api.material.flag.types.IItemRegistry;
import dev.metallurgists.rutile.api.material.flag.types.IRecipeHandler;
import dev.metallurgists.rutile.api.material.registry.block.IMaterialBlock;
import dev.metallurgists.rutile.api.material.registry.block.MaterialBlock;
import dev.metallurgists.rutile.api.material.registry.block.MaterialBlockItem;
import dev.metallurgists.rutile.registry.RutileFlagKeys;
import dev.metallurgists.rutile.util.helpers.MaterialHelpers;
import dev.metallurgists.rutile.util.helpers.ModelHelpers;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class CasingFlag extends BlockFlag implements IHaveConnectedTextures, IRecipeHandler {
    @Getter
    private List<TagKey<Item>> toApplyOn = List.of(AllTags.AllItemTags.STRIPPED_LOGS.tag, AllTags.AllItemTags.STRIPPED_WOOD.tag);
    @Getter
    private boolean useSheet = false;

    public CasingFlag(String existingNamespace) {
        super("%s_casing", existingNamespace);
        this.setTagPatterns(List.of("metallurgica:casings", "metallurgica:casings/%s", "minecraft:mineable/pickaxe"));
        this.setItemTagPatterns(List.of("metallurgica:casings", "metallurgica:casings/%s"));
    }

    public CasingFlag() {
        this("");
    }

    @SafeVarargs
    public final CasingFlag appliesOn(TagKey<Item>... tags) {
        this.toApplyOn = List.of(tags);
        return this;
    }

    public CasingFlag useSheet() {
        this.useSheet = true;
        return this;
    }

    @Override
    public BlockEntry<? extends IMaterialBlock> registerBlock(@NotNull Material material, IBlockRegistry flag, @NotNull AbstractRegistrate<?> registrate) {
        CTSpriteShiftEntry spriteShift = getSpriteShiftEntry(material);
        return registrate.block(getIdPattern().formatted(material.getName()), (p) -> new MaterialBlock(p, material, flag))
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.sound(SoundType.METAL))
                .transform(pickaxeOnly())
                .setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .item(MaterialBlockItem::create)
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .build()
                .onRegister(connectedTextures(() -> new EncasedCTBehaviour(spriteShift)))
                .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, spriteShift)))
                .register();
    }

    @Override
    public void registerBlockAssets(Material material) {
        boolean texturePresent = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/block/materials/" + material.getName() + "/casing.png")).isPresent();
        String texture = texturePresent ? material.getNamespace() + ":block/materials/" + material.getName() + "/casing" : "metallurgica:block/materials/null/casing";
        RutileDynamicResourcePack.addBlockModel(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), ModelHelpers.simpleCubeAll(texture));
        RutileDynamicResourcePack.addBlockState(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), ModelHelpers.singleVariantBlockstate(material.getNamespace() + ":block/" + getIdPattern().formatted(material.getName())));
        RutileDynamicResourcePack.addItemModel(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), ModelHelpers.simpleParentedModel(material.getNamespace() + ":block/" + getIdPattern().formatted(material.getName())));
    }

    @Override
    public boolean shouldHaveComposition() {
        return false;
    }

    @Override
    public FlagKey<?> getKey() {
        return MetallurgicaFlagKeys.CASING;
    }

    @Override
    public CTSpriteShiftEntry getSpriteShiftEntry(Material material) {
        return MetallurgicaSpriteShifts.materialOmni(material, getKey());
    }

    @Override
    public void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        if (material.noRegister(getKey())) return;
        FlagKey<? extends IItemRegistry> used = isUseSheet() ? MetallurgicaFlagKeys.SHEET : RutileFlagKeys.INGOT;
        Item usedItem = MaterialHelpers.getItem(material, used);
        for (TagKey<Item> appliesOn : getToApplyOn()) {
            String appliesOnPath = appliesOn.location().toString().replace("/", "_").replace(":", "_");

            String recipePath = material.asResourceString(MaterialHelpers.getNameForRecipe(material, getKey()) + "_from_" + appliesOnPath);
            ProcessingRecipeBuilder<ItemApplicationRecipe> builder = new RuntimeBuilder<>((params) -> new ItemApplicationRecipe(AllRecipeTypes.ITEM_APPLICATION, params), provider, recipePath);
            builder.require(appliesOn);
            builder.require(usedItem);
            builder.output(MaterialHelpers.getBlock(material, MetallurgicaFlagKeys.CASING).asItem());
            builder.build();
        }
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        if (useSheet) {
            flags.ensureSet(MetallurgicaFlagKeys.SHEET, true);
        } else {
            flags.ensureSet(RutileFlagKeys.INGOT, true);
        }
    }
}
