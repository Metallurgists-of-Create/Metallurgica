package dev.metallurgists.metallurgica.compat.rutile.flags.objects;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.decoration.encasing.EncasableBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.AbstractSimpleShaftBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import dev.metallurgists.metallurgica.foundation.util.ClientUtil;
import dev.metallurgists.metallurgica.registry.material.init.MetMaterialBlockEntities;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.types.IBlockRegistry;
import dev.metallurgists.rutile.api.material.registry.block.IMaterialBlock;
import dev.metallurgists.rutile.client.MaterialBlockRenderer;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MaterialCogWheelBlock extends AbstractSimpleShaftBlock implements ICogWheel, EncasableBlock, IMaterialBlock {
    public boolean isLarge;
    public final Material material;
    public final IBlockRegistry blockFlag;

    protected MaterialCogWheelBlock(Material material, IBlockRegistry blockFlag, boolean large, Properties properties, boolean registerModel) {
        super(properties);
        this.material = material;
        this.blockFlag = blockFlag;
        this.isLarge = large;
        if (registerModel && ClientUtil.isClientSide()) {
            MaterialBlockRenderer.create(this, material, blockFlag.getKey());
        }
    }

    public static MaterialCogWheelBlock small(Material material, IBlockRegistry blockFlag, Properties properties) {
        return new MaterialCogWheelBlock(material, blockFlag, false, properties, true);
    }

    public static MaterialCogWheelBlock large(Material material, IBlockRegistry blockFlag, Properties properties) {
        return new MaterialCogWheelBlock(material, blockFlag, true, properties, true);
    }

    public boolean isLargeCog() {
        return this.isLarge;
    }

    public boolean isSmallCog() {
        return !this.isLarge;
    }

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return (this.isLarge ? AllShapes.LARGE_GEAR : AllShapes.SMALL_GEAR).get(state.getValue(AXIS));
    }

    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return isValidCogwheelPosition(ICogWheel.isLargeCog(state), worldIn, pos, (Direction.Axis)state.getValue(AXIS));
    }

    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (placer instanceof Player player) {
            this.triggerShiftingGearsAdvancement(worldIn, pos, state, player);
        }

    }

    protected void triggerShiftingGearsAdvancement(Level world, BlockPos pos, BlockState state, Player player) {
        if (!world.isClientSide && player != null) {
            Direction.Axis axis = state.getValue(AXIS);

            for(Direction.Axis perpendicular1 : Iterate.axes) {
                if (perpendicular1 != axis) {
                    Direction d1 = Direction.get(Direction.AxisDirection.POSITIVE, perpendicular1);

                    for(Direction.Axis perpendicular2 : Iterate.axes) {
                        if (perpendicular1 != perpendicular2 && axis != perpendicular2) {
                            Direction d2 = Direction.get(Direction.AxisDirection.POSITIVE, perpendicular2);

                            for(int offset1 : Iterate.positiveAndNegative) {
                                for(int offset2 : Iterate.positiveAndNegative) {
                                    BlockPos connectedPos = pos.relative(d1, offset1).relative(d2, offset2);
                                    BlockState blockState = world.getBlockState(connectedPos);
                                    if (blockState.getBlock() instanceof dev.metallurgists.metallurgica.foundation.material.block.MaterialCogWheelBlock && blockState.getValue(AXIS) == axis && ICogWheel.isLargeCog(blockState) != this.isLarge) {
                                        AllAdvancements.COGS.awardTo(player);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (!player.isShiftKeyDown() && player.mayBuild()) {
            ItemStack heldItem = player.getItemInHand(hand);
            InteractionResult result = this.tryEncase(state, world, pos, heldItem, player, hand, ray);
            return result.consumesAction() ? result : InteractionResult.PASS;
        } else {
            return InteractionResult.PASS;
        }
    }

    public static boolean isValidCogwheelPosition(boolean large, LevelReader worldIn, BlockPos pos, Direction.Axis cogAxis) {
        for(Direction facing : Iterate.directions) {
            if (facing.getAxis() != cogAxis) {
                BlockPos offsetPos = pos.relative(facing);
                BlockState blockState = worldIn.getBlockState(offsetPos);
                if ((!blockState.hasProperty(AXIS) || facing.getAxis() != blockState.getValue(AXIS)) && (ICogWheel.isLargeCog(blockState) || large && ICogWheel.isSmallCog(blockState))) {
                    return false;
                }
            }
        }

        return true;
    }

    protected Direction.Axis getAxisForPlacement(BlockPlaceContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            return context.getClickedFace().getAxis();
        } else {
            Level world = context.getLevel();
            BlockState stateBelow = world.getBlockState(context.getClickedPos().below());
            if (AllBlocks.ROTATION_SPEED_CONTROLLER.has(stateBelow) && this.isLargeCog()) {
                return stateBelow.getValue(SpeedControllerBlock.HORIZONTAL_AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            } else {
                BlockPos placedOnPos = context.getClickedPos().relative(context.getClickedFace().getOpposite());
                BlockState placedAgainst = world.getBlockState(placedOnPos);
                Block block = placedAgainst.getBlock();
                if (ICogWheel.isSmallCog(placedAgainst)) {
                    return ((IRotate)block).getRotationAxis(placedAgainst);
                } else {
                    Direction.Axis preferredAxis = getPreferredAxis(context);
                    return preferredAxis != null ? preferredAxis : context.getClickedFace().getAxis();
                }
            }
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean shouldWaterlog = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return (BlockState)((BlockState)this.defaultBlockState().setValue(AXIS, this.getAxisForPlacement(context))).setValue(BlockStateProperties.WATERLOGGED, shouldWaterlog);
    }

    public float getParticleTargetRadius() {
        return this.isLargeCog() ? 1.125F : 0.65F;
    }

    public float getParticleInitialRadius() {
        return this.isLargeCog() ? 1.0F : 0.75F;
    }

    public boolean isDedicatedCogWheel() {
        return true;
    }

    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return MetMaterialBlockEntities.materialCogwheel.get();
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }

    @Override
    public IBlockRegistry getFlag() {
        return this.blockFlag;
    }

    @Override
    public String getDescriptionId() {
        return blockFlag.getUnlocalizedName(material);
    }

    @Override
    public MutableComponent getName() {
        return blockFlag.getLocalizedName(material);
    }
}