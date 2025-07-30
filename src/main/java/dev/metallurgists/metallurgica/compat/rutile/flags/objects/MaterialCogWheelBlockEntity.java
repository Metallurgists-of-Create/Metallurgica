package dev.metallurgists.metallurgica.compat.rutile.flags.objects;

import com.simibubi.create.content.kinetics.simpleRelays.SimpleKineticBlockEntity;
import dev.metallurgists.metallurgica.compat.rutile.RutileBlockEntities;
import dev.metallurgists.metallurgica.registry.material.init.MetMaterialBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MaterialCogWheelBlockEntity extends SimpleKineticBlockEntity {
    public MaterialCogWheelBlockEntity(BlockPos pos, BlockState state) {
        super(RutileBlockEntities.materialCogwheel.get(), pos, state);
    }
}
