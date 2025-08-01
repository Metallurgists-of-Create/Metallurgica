package dev.metallurgists.metallurgica.content.temperature.hot_plate;

import com.simibubi.create.AllShapes;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.logistics.depot.DepotBlock;
import com.simibubi.create.foundation.block.IBE;
import dev.metallurgists.metallurgica.registry.MetallurgicaBlockEntities;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HotPlateBlock extends Block implements IWrenchable, IBE<HotPlateBlockEntity>  {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Shape> SHAPE = EnumProperty.create("shape", Shape.class);

    public HotPlateBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH)
                .setValue(SHAPE, Shape.MIDDLE));
    }

    public static boolean isHotPlate(BlockState state) {
        return state.getBlock() instanceof HotPlateBlock;
    }

    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved) {
        if (oldState.getBlock() == state.getBlock())
            return;
        if (moved)
            return;
        withBlockEntityDo(world, pos, HotPlateBlockEntity::updateConnectivity);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
            BlockEntity be = world.getBlockEntity(pos);
            if (!(be instanceof HotPlateBlockEntity tankBE))
                return;
            tankBE.destroy();
            world.removeBlockEntity(pos);
            ConnectivityHandler.splitMulti(tankBE);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof HotPlateBlockEntity hotPlate) {
            if (hotPlate.getControllerBE() != null) {
                return hotPlate.getControllerBE().setOrRemoveCoil(player.getItemInHand(hand), player);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return AllShapes.CASING_13PX.get(Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SHAPE);
    }

    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return 0;
    }

    public enum Shape implements StringRepresentable {
        MIDDLE, EDGE, CORNER;

        @Override
        public String getSerializedName() {
            return Lang.asId(name());
        }
    }

    @Override
    public Class<HotPlateBlockEntity> getBlockEntityClass() {
        return HotPlateBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends HotPlateBlockEntity> getBlockEntityType() {
        return MetallurgicaBlockEntities.hotPlate.get();
    }
}
