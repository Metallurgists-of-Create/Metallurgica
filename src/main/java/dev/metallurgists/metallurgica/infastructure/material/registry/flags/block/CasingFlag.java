package dev.metallurgists.metallurgica.infastructure.material.registry.flags.block;

import dev.metallurgists.metallurgica.foundation.data.runtime.RuntimeProcessingRecipeBuilder;
import dev.metallurgists.metallurgica.foundation.material.block.IMaterialBlock;
import dev.metallurgists.metallurgica.foundation.material.block.MaterialBlock;
import dev.metallurgists.metallurgica.foundation.material.block.MaterialBlockItem;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.MaterialHelper;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.BlockFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.IRecipeHandler;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.MaterialFlags;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IBlockRegistry;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IHaveConnectedTextures;
import dev.metallurgists.metallurgica.registry.MetallurgicaSpriteShifts;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import lombok.Getter;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

import static dev.metallurgists.metallurgica.infastructure.material.MaterialHelper.getNameForRecipe;
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
        this("metallurgica");
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
    public BlockEntry<? extends IMaterialBlock> registerBlock(@NotNull Material material, IBlockRegistry flag, @NotNull MetallurgicaRegistrate registrate) {
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
    public boolean shouldHaveComposition() {
        return false;
    }

    @Override
    public FlagKey<?> getKey() {
        return FlagKey.CASING;
    }

    @Override
    public CTSpriteShiftEntry getSpriteShiftEntry(Material material) {
        return null;//MetallurgicaSpriteShifts.materialOmni(material, getKey());
    }

    @Override
    public void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        if (material.noRegister(getKey())) return;
        FlagKey<? extends ItemFlag> used = isUseSheet() ? FlagKey.SHEET : FlagKey.INGOT;
        Item usedItem = MaterialHelper.getItem(material, used);
        for (TagKey<Item> appliesOn : getToApplyOn()) {
            String appliesOnPath = appliesOn.location().toString().replace("/", "_").replace(":", "_");

            String recipePath = material.asResourceString(getNameForRecipe(material, getKey()) + "_from_" + appliesOnPath);
            ProcessingRecipeBuilder<ItemApplicationRecipe> builder = new RuntimeProcessingRecipeBuilder<>((params) -> new ItemApplicationRecipe(AllRecipeTypes.ITEM_APPLICATION, params), provider, recipePath);
            builder.require(appliesOn);
            builder.require(usedItem);
            builder.output(MaterialHelper.getBlock(material, FlagKey.CASING).asItem());
            builder.build();
        }
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        if (useSheet) {
            flags.ensureSet(FlagKey.SHEET, true);
        } else {
            flags.ensureSet(FlagKey.INGOT, true);
        }
    }
}
