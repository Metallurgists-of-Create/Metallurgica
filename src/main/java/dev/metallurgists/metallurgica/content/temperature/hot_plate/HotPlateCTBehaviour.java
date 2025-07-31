package dev.metallurgists.metallurgica.content.temperature.hot_plate;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.HorizontalCTBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class HotPlateCTBehaviour extends HorizontalCTBehaviour {

    private CTSpriteShiftEntry bottomShift;

    public HotPlateCTBehaviour(CTSpriteShiftEntry layerShift, CTSpriteShiftEntry topShift, CTSpriteShiftEntry bottomShift) {
        super(layerShift, topShift);
        this.bottomShift = bottomShift;
    }

    @Override
    public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
        if (sprite != null && direction.getAxis() == Direction.Axis.Y && bottomShift.getOriginal() == sprite)
            return bottomShift;
        return super.getShift(state, direction, sprite);
    }

    public boolean buildContextForOccludedDirections() {
        return true;
    }

    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos,
                              Direction face) {
        return state.getBlock() == other.getBlock() && ConnectivityHandler.isConnected(reader, pos, otherPos);
    }
}
