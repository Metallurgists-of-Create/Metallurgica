package dev.metallurgists.metallurgica.compat.rutile;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.rutile.flags.MetallurgicaFlagKeys;
import dev.metallurgists.metallurgica.compat.rutile.flags.objects.MaterialCogWheelBlockEntity;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.BlockFlag;
import dev.metallurgists.rutile.api.material.flag.FlagKey;
import dev.metallurgists.rutile.api.registry.RutileAPI;
import dev.metallurgists.rutile.api.registry.material.MaterialRegistry;
import dev.metallurgists.rutile.util.helpers.MaterialHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class RutileBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Metallurgica.ID);

    public static final RegistryObject<BlockEntityType<MaterialCogWheelBlockEntity>> materialCogwheel = BLOCK_ENTITY_TYPES.register("material_cogwheel",
            () -> BlockEntityType.Builder.of(MaterialCogWheelBlockEntity::new,
                    gatherValidBlocks(MetallurgicaFlagKeys.COG_WHEEL, MetallurgicaFlagKeys.LARGE_COG_WHEEL).toArray(new Block[0]))
                    .build(null));


    private static List<Block> gatherValidBlocks(FlagKey<? extends BlockFlag> flagKey) {
        List<Block> validBlocks = new ArrayList<>();
        for (MaterialRegistry registry : RutileAPI.materialManager.getRegistries()) {
            for (Material material : registry.getAllMaterials()) {
                if (material.hasFlag(flagKey)) {
                    validBlocks.add(MaterialHelpers.getBlock(material, flagKey));
                }
            }
        }
        return validBlocks;
    }

    @SafeVarargs
    private static List<Block> gatherValidBlocks(FlagKey<? extends BlockFlag>... flagKeys) {
        List<Block> validBlocks = new ArrayList<>();
        for (MaterialRegistry registry : RutileAPI.materialManager.getRegistries()) {
            for (Material material : registry.getAllMaterials()) {
                for (FlagKey<? extends BlockFlag> flagKey : flagKeys) {
                    if (material.hasFlag(flagKey)) {
                        validBlocks.add(MaterialHelpers.getBlock(material, flagKey));
                    }
                }
            }
        }
        return validBlocks;
    }

    public static void register(IEventBus bus) {
        BLOCK_ENTITY_TYPES.register(bus);
    }
}
