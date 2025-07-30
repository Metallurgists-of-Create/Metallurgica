package dev.metallurgists.metallurgica.compat.rutile.flags.objects;

import com.drmangotea.tfmg.content.machinery.misc.winding_machine.SpoolItem;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.types.IItemRegistry;
import dev.metallurgists.rutile.api.material.registry.item.IMaterialItem;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class MaterialSpoolItem extends SpoolItem implements IMaterialItem {
    public final Material material;
    public final IItemRegistry itemFlag;

    public MaterialSpoolItem(Properties properties, int barColor, ResourceLocation type, Material material, IItemRegistry itemFlag) {
        super(properties, barColor, type);
        this.material = material;
        this.itemFlag = itemFlag;
    }

    @Override
    public String getDescriptionId() {
        return itemFlag.getUnlocalizedName(material);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return getDescriptionId();
    }

    @Override
    public MutableComponent getDescription() {
        return itemFlag.getLocalizedName(material);
    }

    @Override
    public MutableComponent getName(ItemStack stack) {
        return getDescription();
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public IItemRegistry getFlag() {
        return this.itemFlag;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }
}
