package dev.metallurgists.metallurgica.content.temperature.hot_plate;

import dev.metallurgists.metallurgica.content.temperature.hot_plate.heating_coil.HeatingCoilType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public record HeatingCoilData(ItemStack itemIn, HeatingCoilType type) {

    public void write(CompoundTag tag) {
        CompoundTag heatingCoil = new CompoundTag();
        if (type != null) {
            type.writeToNBT(heatingCoil.getCompound("Type"));
            itemIn.save(heatingCoil.getCompound("Item"));
        }
        tag.put("HeatingCoil", heatingCoil);
    }

    public static HeatingCoilData read(CompoundTag tag) {
        CompoundTag heatingCoil = tag.getCompound("HeatingCoil");
        ItemStack itemStack = ItemStack.of(heatingCoil.getCompound("Item"));
        HeatingCoilType type = HeatingCoilType.readFromNBT(heatingCoil.getCompound("Type"));
        return new HeatingCoilData(itemStack, type);
    }
}
