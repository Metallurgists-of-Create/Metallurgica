package dev.metallurgists.metallurgica.compat.rutile.flags.types;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import dev.metallurgists.rutile.api.material.base.Material;

public interface IHaveConnectedTextures {
    CTSpriteShiftEntry getSpriteShiftEntry(Material material);
}
