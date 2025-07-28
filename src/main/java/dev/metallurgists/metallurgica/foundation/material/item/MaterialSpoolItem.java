package dev.metallurgists.metallurgica.foundation.material.item;

import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnection;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.SpoolItem;
import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.base.interfaces.IItemRegistry;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
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
