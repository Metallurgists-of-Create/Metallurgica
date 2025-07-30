package dev.metallurgists.metallurgica.foundation.ponder;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.rutile.flags.MetallurgicaFlagKeys;
import dev.metallurgists.metallurgica.compat.rutile.materials.MetallurgicaMaterials;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey;
import dev.metallurgists.metallurgica.infastructure.material.MaterialHelper;
import dev.metallurgists.metallurgica.registry.MetallurgicaBlocks;
import dev.metallurgists.metallurgica.registry.MetallurgicaItems;
import dev.metallurgists.metallurgica.registry.material.MetMaterials;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.metallurgists.rutile.util.helpers.MaterialHelpers;
import net.createmod.catnip.platform.CatnipServices;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class MetallurgicaPonderTags {

    public static final ResourceLocation
            MINERALS = loc("minerals"),
            METALS = loc("metals"),
            PRIMITIVE = loc("primitive"),
            MACHINERY = loc("machinery")
    ;

    private static ResourceLocation loc(String id) {
        return Metallurgica.asResource(id);
    }

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        PonderTagRegistrationHelper<ItemLike> itemHelper = helper.withKeyFunction(
                CatnipServices.REGISTRIES::getKeyOrThrow);

        helper.registerTag(MINERALS)
                .addToIndex()
                .item(Items.GRANITE, true, false)
                .title("Minerals")
                .description("Ores, Minerals and how to use them")
                .register();

        helper.registerTag(METALS)
                .addToIndex()
                .item(MetallurgicaItems.impureIronBloom.get(), true, false)
                .title("Metals")
                .description("Metallurgy and it's many complicated features")
                .register();

        helper.registerTag(PRIMITIVE)
                .addToIndex()
                .item(MetallurgicaItems.dirtyClayBall.get(), true, false)
                .title("Primitive Utilities")
                .description("Early-Game Utilities and Tools")
                .register();

        helper.registerTag(MACHINERY)
                .addToIndex()
                .item(MetallurgicaBlocks.drillActivator.get(), true, false)
                .title("Machinery")
                .description("Advanced Machines and Tools")
                .register();

    }
}
