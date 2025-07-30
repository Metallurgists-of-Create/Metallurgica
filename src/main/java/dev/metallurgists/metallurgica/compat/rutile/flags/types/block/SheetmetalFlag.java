package dev.metallurgists.metallurgica.compat.rutile.flags.types.block;


import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.metallurgists.metallurgica.compat.rutile.flags.MetallurgicaFlagKeys;
import dev.metallurgists.metallurgica.compat.rutile.flags.types.IHaveConnectedTextures;
import dev.metallurgists.metallurgica.compat.rutile.scrapping.IScrappable;
import dev.metallurgists.metallurgica.compat.rutile.scrapping.ScrappingData;
import dev.metallurgists.metallurgica.registry.MetallurgicaSpriteShifts;
import dev.metallurgists.rutile.api.dynamic_pack.asset.RutileDynamicResourcePack;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.base.MaterialFlags;
import dev.metallurgists.rutile.api.material.flag.BlockFlag;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.material.flag.types.IBlockRegistry;
import dev.metallurgists.rutile.api.material.registry.block.IMaterialBlock;
import dev.metallurgists.rutile.api.material.registry.block.MaterialBlock;
import dev.metallurgists.rutile.api.material.registry.block.MaterialBlockItem;
import dev.metallurgists.rutile.util.helpers.ModelHelpers;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class SheetmetalFlag extends BlockFlag implements IHaveConnectedTextures, IScrappable {
    @Getter
    private boolean requiresCompacting = false;

    public SheetmetalFlag(String existingNamespace) {
        super("%s_sheetmetal", existingNamespace);
        this.setTagPatterns(List.of("c:storage_blocks", "c:storage_blocks/%s_sheet", "minecraft:mineable/pickaxe"));
        this.setItemTagPatterns(List.of("c:storage_blocks", "c:storage_blocks/%s_sheet"));
    }

    public SheetmetalFlag() {
        this("");
    }

    public SheetmetalFlag requiresCompacting() {
        this.requiresCompacting = true;
        return this;
    }

    @Override
    public CTSpriteShiftEntry getSpriteShiftEntry(Material material) {
        return MetallurgicaSpriteShifts.materialRectangle(material, getKey());
    }

    @Override
    public ScrappingData getScrappingData(Material mainMaterial) {
        return ScrappingData.create().addOutput(mainMaterial, 9, 1, 0.25f);
    }

    @Override
    public BlockEntry<? extends IMaterialBlock> registerBlock(@NotNull Material material, IBlockRegistry flag, @NotNull AbstractRegistrate<?> registrate) {
        CTSpriteShiftEntry spriteShift = getSpriteShiftEntry(material);
        return registrate.block(getIdPattern().formatted(material.getName()), (p) -> new MaterialBlock(p, material, flag))
                .initialProperties(SharedProperties::copperMetal)
                .properties(p -> p.sound(SoundType.COPPER))
                .transform(pickaxeOnly())
                .setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .item(MaterialBlockItem::create)
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .build()
                .onRegister(connectedTextures(() -> new EncasedCTBehaviour(spriteShift)))
                .onRegister(casingConnectivity((block, cc) -> cc.make(block, spriteShift)))
                .register();
    }

    @Override
    public void registerBlockAssets(Material material) {
        boolean texturePresent = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(material.getNamespace() + ":textures/block/materials/" + material.getName() + "/sheetmetal.png")).isPresent();
        String texture = texturePresent ? material.getNamespace() + ":block/materials/" + material.getName() + "/sheetmetal" : "metallurgica:block/materials/null/sheetmetal";
        RutileDynamicResourcePack.addBlockModel(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), ModelHelpers.simpleCubeAll(texture));
        RutileDynamicResourcePack.addBlockState(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), ModelHelpers.singleVariantBlockstate(material.getNamespace() + ":block/" + getIdPattern().formatted(material.getName())));
        RutileDynamicResourcePack.addItemModel(new ResourceLocation(material.getNamespace(), getIdPattern().formatted(material.getName())), ModelHelpers.simpleParentedModel(material.getNamespace() + ":block/" + getIdPattern().formatted(material.getName())));
    }

    @Override
    public boolean shouldHaveComposition() {
        return false;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(MetallurgicaFlagKeys.SHEET);
    }

    @Override
    public FlagKey<?> getKey() {
        return MetallurgicaFlagKeys.SHEETMETAL;
    }
}
