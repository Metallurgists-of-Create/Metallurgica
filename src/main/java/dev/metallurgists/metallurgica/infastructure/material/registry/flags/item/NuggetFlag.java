package dev.metallurgists.metallurgica.infastructure.material.registry.flags.item;

import dev.metallurgists.metallurgica.foundation.material.item.IMaterialItem;
import dev.metallurgists.metallurgica.foundation.material.item.MaterialItem;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.ISpecialLangSuffix;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.ItemFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.MaterialFlags;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IItemRegistry;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NuggetFlag extends ItemFlag implements ISpecialLangSuffix {
    @Getter
    private boolean requiresCompacting = false;
    @Getter
    private boolean shard;

    @Getter
    private int amountToCraft = 9; // Default to 9 nuggets per crafting recipe

    public NuggetFlag(String existingNamespace, boolean shard) {
        super(shard ? "%s_shard" :"%s_nugget", existingNamespace);
        this.shard = shard;
        this.setTagPatterns(List.of(shard ? "c:/shards" :"c:nuggets", shard ? "c:/shards/%s" : "c:nuggets/%s"));
    }

    public NuggetFlag(boolean $shard) {
        this("metallurgica", $shard);
    }

    public NuggetFlag(String existingNamespace) {
        this(existingNamespace, false);
    }

    public NuggetFlag() {
        this("metallurgica", false);
    }

    public NuggetFlag requiresCompacting() {
        this.requiresCompacting = true;
        return this;
    }

    public NuggetFlag amountToCraft(int amount) {
        if (amount < 2) {
            throw new IllegalArgumentException("Amount to craft must be at least 2");
        } else if (amount > 9) {
            throw new IllegalArgumentException("Amount to craft cannot exceed 9");
        }
        this.amountToCraft = amount;
        return this;
    }

    @Override
    public ItemEntry<? extends IMaterialItem> registerItem(@NotNull Material material, IItemRegistry flag, @NotNull MetallurgicaRegistrate registrate) {
        return registrate
                .item(flag.getIdPattern().formatted(material.getName()), (p) -> new MaterialItem(p, material, flag))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .register();
    }

    @Override
    public FlagKey<?> getKey() {
        return FlagKey.NUGGET;
    }

    @Override
    public void verifyFlag(MaterialFlags flags) {

    }

    @Override
    public String getLangSuffix(){return isShard() ? "shard" : "nugget";}

}
