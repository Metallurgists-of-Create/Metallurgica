package dev.metallurgists.metallurgica.registry;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.rutile.materials.MetalMaterials;
import dev.metallurgists.metallurgica.compat.rutile.materials.MetallurgicaMaterials;
import dev.metallurgists.metallurgica.foundation.material.item.MaterialSpoolItem;
import dev.metallurgists.metallurgica.infastructure.material.MaterialHelper;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.registry.material.MetMaterials;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.metallurgists.rutile.registry.RutileFlagKeys;
import dev.metallurgists.rutile.util.helpers.MaterialHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class MCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS;
    public static final RegistryObject<CreativeModeTab> MAIN;
    public static final RegistryObject<CreativeModeTab> MATERIALS;

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == MAIN.get()) {
            event.acceptAll(customAdditions(MAIN.get()));

            for(RegistryEntry<Item> item : Metallurgica.registrate.getAll(Registries.ITEM)) {
                if (CreateRegistrate.isInCreativeTab(item, MAIN) && !blacklist(MAIN.get()).contains(item) && !(item.get() instanceof SequencedAssemblyItem)) {
                    event.accept(item);
                }
            }
        }
        if (event.getTab() == MATERIALS.get()) {
            for(RegistryEntry<Item> item : Metallurgica.registrate.getAll(Registries.ITEM)) {
                if (CreateRegistrate.isInCreativeTab(item, MATERIALS) && !blacklist(MATERIALS.get()).contains(item) && !(item.get() instanceof SequencedAssemblyItem)) {
                    if (item.get() instanceof MaterialSpoolItem) {
                        ItemStack spool = item.get().getDefaultInstance();
                        spool.getOrCreateTag().putInt("Amount", 1000);
                        event.accept(spool);
                    } else {
                        event.accept(item);
                    }
                }
            }
        }
    }

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }

    public static List<RegistryEntry<? extends Item>> blacklist(CreativeModeTab tab) {
        List<RegistryEntry<? extends Item>> list = new ArrayList();
        if (tab == MAIN.get()) {

        } else if (tab == MATERIALS.get()) {

        }
        return list;
    }

    public static List<ItemStack> customAdditions(CreativeModeTab tab) {
        List<ItemStack> list = new ArrayList();
        if (tab == MAIN.get()) {

        } else if (tab == MATERIALS.get()) {

        }
        return list;
    }

    static {
        CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "tfmg");
        MAIN = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder().withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getId()).title(Component.translatable("itemGroup.metallurgica.base")).icon(MetallurgicaItems.alluvialCassiterite::asStack).build());
        MATERIALS = CREATIVE_MODE_TABS.register("materials", () -> CreativeModeTab.builder().withTabsBefore(MAIN.getId()).title(Component.translatable("itemGroup.metallurgica.materials")).icon(() -> MaterialHelpers.getItem(MetalMaterials.Uranium, RutileFlagKeys.INGOT).getDefaultInstance()).build());
    }
}
