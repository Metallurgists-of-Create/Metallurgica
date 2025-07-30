package dev.metallurgists.metallurgica.infastructure.material.registry.flags.block;

import dev.metallurgists.metallurgica.foundation.material.block.IMaterialBlock;
import dev.metallurgists.metallurgica.foundation.material.block.MaterialBlock;
import dev.metallurgists.metallurgica.foundation.material.block.MaterialBlockItem;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IBlockRegistry;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IHaveConnectedTextures;
import dev.metallurgists.metallurgica.infastructure.material.scrapping.IScrappable;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.BlockFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.MaterialFlags;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.infastructure.material.scrapping.ScrappingData;
import dev.metallurgists.metallurgica.registry.MetallurgicaSpriteShifts;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import lombok.Getter;
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
        this("metallurgica");
    }

    public SheetmetalFlag requiresCompacting() {
        this.requiresCompacting = true;
        return this;
    }

    @Override
    public BlockEntry<? extends IMaterialBlock> registerBlock(@NotNull Material material, IBlockRegistry flag, @NotNull MetallurgicaRegistrate registrate) {
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
    public boolean shouldHaveComposition() {
        return false;
    }

    @Override
    public FlagKey<?> getKey() {
        return FlagKey.SHEETMETAL;
    }

    @Override
    public CTSpriteShiftEntry getSpriteShiftEntry(Material material) {
        return null;//MetallurgicaSpriteShifts.materialRectangle(material, getKey());
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {
        flags.ensureSet(FlagKey.SHEET);
    }

    @Override
    public ScrappingData getScrappingData(Material mainMaterial) {
        return ScrappingData.create().addOutput(mainMaterial, 9, 1, 0.25f);
    }
}
