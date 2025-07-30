package dev.metallurgists.metallurgica.compat.rutile.flags.objects;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import dev.metallurgists.rutile.api.material.registry.block.MaterialBlockItem;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Predicate;

public class MaterialCogWheelBlockItem extends MaterialBlockItem {
    boolean large;
    private final int placementHelperId;
    private final int integratedCogHelperId;

    public MaterialCogWheelBlockItem(MaterialCogWheelBlock block, Properties properties) {
        super(block, properties);
        this.large = block.isLarge;
        this.placementHelperId = PlacementHelpers.register(this.large ? new MaterialCogWheelBlockItem.LargeCogHelper() : new MaterialCogWheelBlockItem.SmallCogHelper());
        this.integratedCogHelperId = PlacementHelpers.register(this.large ? new MaterialCogWheelBlockItem.IntegratedLargeCogHelper() : new MaterialCogWheelBlockItem.IntegratedSmallCogHelper());
    }

    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        IPlacementHelper helper = PlacementHelpers.get(this.placementHelperId);
        Player player = context.getPlayer();
        BlockHitResult ray = new BlockHitResult(context.getClickLocation(), context.getClickedFace(), pos, true);
        if (helper.matchesState(state) && player != null && !player.isShiftKeyDown()) {
            return helper.getOffset(player, world, state, pos, ray).placeInWorld(world, this, player, context.getHand(), ray);
        } else {
            if (this.integratedCogHelperId != -1) {
                helper = PlacementHelpers.get(this.integratedCogHelperId);
                if (helper.matchesState(state) && player != null && !player.isShiftKeyDown()) {
                    return helper.getOffset(player, world, state, pos, ray).placeInWorld(world, this, player, context.getHand(), ray);
                }
            }

            return super.onItemUseFirst(stack, context);
        }
    }

    @MethodsReturnNonnullByDefault
    private static class SmallCogHelper extends DiagonalCogHelper {
        public Predicate<ItemStack> getItemPredicate() {
            return p -> ICogWheel.isSmallCogItem(p) && ICogWheel.isDedicatedCogItem(p);
        }

        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            if (this.hitOnShaft(state, ray)) {
                return PlacementOffset.fail();
            } else if (!ICogWheel.isLargeCog(state)) {
                Direction.Axis axis = ((IRotate)state.getBlock()).getRotationAxis(state);

                for(Direction dir : IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(), axis)) {
                    BlockPos newPos = pos.relative(dir);
                    if (MaterialCogWheelBlock.isValidCogwheelPosition(false, world, newPos, axis) && world.getBlockState(newPos).canBeReplaced()) {
                        return PlacementOffset.success(newPos, (s) -> s.setValue(RotatedPillarKineticBlock.AXIS, axis));
                    }
                }

                return PlacementOffset.fail();
            } else {
                return super.getOffset(player, world, state, pos, ray);
            }
        }
    }

    @MethodsReturnNonnullByDefault
    private static class LargeCogHelper extends DiagonalCogHelper {
        public Predicate<ItemStack> getItemPredicate() {
            return p -> ICogWheel.isLargeCogItem(p) && ICogWheel.isDedicatedCogItem(p);
        }

        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            if (this.hitOnShaft(state, ray)) {
                return PlacementOffset.fail();
            } else if (ICogWheel.isLargeCog(state)) {
                Direction.Axis axis = ((IRotate)state.getBlock()).getRotationAxis(state);
                Direction side = IPlacementHelper.orderedByDistanceOnlyAxis(pos, ray.getLocation(), axis).get(0);

                for(Direction dir : IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(), axis)) {
                    BlockPos newPos = pos.relative(dir).relative(side);
                    if (MaterialCogWheelBlock.isValidCogwheelPosition(true, world, newPos, dir.getAxis()) && world.getBlockState(newPos).canBeReplaced()) {
                        return PlacementOffset.success(newPos, (s) -> s.setValue(RotatedPillarKineticBlock.AXIS, dir.getAxis()));
                    }
                }

                return PlacementOffset.fail();
            } else {
                return super.getOffset(player, world, state, pos, ray);
            }
        }
    }

    @MethodsReturnNonnullByDefault
    public abstract static class DiagonalCogHelper implements IPlacementHelper {
        public Predicate<BlockState> getStatePredicate() {
            return (s) -> ICogWheel.isSmallCog(s) || ICogWheel.isLargeCog(s);
        }

        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            Direction.Axis axis = ((IRotate)state.getBlock()).getRotationAxis(state);
            Direction closest = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(), axis).get(0);

            for(Direction dir : IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(), axis, (d) -> d.getAxis() != closest.getAxis())) {
                BlockPos newPos = pos.relative(dir).relative(closest);
                if (world.getBlockState(newPos).canBeReplaced() && MaterialCogWheelBlock.isValidCogwheelPosition(ICogWheel.isLargeCog(state), world, newPos, axis)) {
                    return PlacementOffset.success(newPos, (s) -> s.setValue(RotatedPillarKineticBlock.AXIS, axis));
                }
            }

            return PlacementOffset.fail();
        }

        protected boolean hitOnShaft(BlockState state, BlockHitResult ray) {
            return AllShapes.SIX_VOXEL_POLE.get(((IRotate)state.getBlock()).getRotationAxis(state)).bounds().inflate(0.001).contains(ray.getLocation().subtract(ray.getLocation().align(Iterate.axisSet)));
        }
    }

    @MethodsReturnNonnullByDefault
    public static class IntegratedLargeCogHelper implements IPlacementHelper {
        public Predicate<ItemStack> getItemPredicate() {
            return p -> ICogWheel.isLargeCogItem(p) && ICogWheel.isDedicatedCogItem(p);
        }

        public Predicate<BlockState> getStatePredicate() {
            return (s) -> !ICogWheel.isDedicatedCogWheel(s.getBlock()) && ICogWheel.isSmallCog(s);
        }

        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            Direction face = ray.getDirection();
            Direction.Axis newAxis;
            if (state.hasProperty(HorizontalKineticBlock.HORIZONTAL_FACING)) {
                newAxis = state.getValue(HorizontalKineticBlock.HORIZONTAL_FACING).getAxis();
            } else if (state.hasProperty(DirectionalKineticBlock.FACING)) {
                newAxis = state.getValue(DirectionalKineticBlock.FACING).getAxis();
            } else if (state.hasProperty(RotatedPillarKineticBlock.AXIS)) {
                newAxis = state.getValue(RotatedPillarKineticBlock.AXIS);
            } else {
                newAxis = Direction.Axis.Y;
            }

            if (face.getAxis() == newAxis) {
                return PlacementOffset.fail();
            } else {
                for(Direction d : IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(), face.getAxis(), newAxis)) {
                    BlockPos newPos = pos.relative(face).relative(d);
                    if (world.getBlockState(newPos).canBeReplaced()) {
                        if (!MaterialCogWheelBlock.isValidCogwheelPosition(false, world, newPos, newAxis)) {
                            return PlacementOffset.fail();
                        }

                        return PlacementOffset.success(newPos, (s) -> s.setValue(MaterialCogWheelBlock.AXIS, newAxis));
                    }
                }

                return PlacementOffset.fail();
            }
        }
    }

    @MethodsReturnNonnullByDefault
    public static class IntegratedSmallCogHelper implements IPlacementHelper {
        public Predicate<ItemStack> getItemPredicate() {
            return p -> ICogWheel.isSmallCogItem(p) && ICogWheel.isDedicatedCogItem(p);
        }

        public Predicate<BlockState> getStatePredicate() {
            return (s) -> !ICogWheel.isDedicatedCogWheel(s.getBlock()) && ICogWheel.isSmallCog(s);
        }

        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            Direction face = ray.getDirection();
            Direction.Axis newAxis;
            if (state.hasProperty(HorizontalKineticBlock.HORIZONTAL_FACING)) {
                newAxis = state.getValue(HorizontalKineticBlock.HORIZONTAL_FACING).getAxis();
            } else if (state.hasProperty(DirectionalKineticBlock.FACING)) {
                newAxis = state.getValue(DirectionalKineticBlock.FACING).getAxis();
            } else if (state.hasProperty(RotatedPillarKineticBlock.AXIS)) {
                newAxis = state.getValue(RotatedPillarKineticBlock.AXIS);
            } else {
                newAxis = Direction.Axis.Y;
            }

            if (face.getAxis() == newAxis) {
                return PlacementOffset.fail();
            } else {
                for(Direction d : IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(), newAxis)) {
                    BlockPos newPos = pos.relative(d);
                    if (world.getBlockState(newPos).canBeReplaced()) {
                        if (!MaterialCogWheelBlock.isValidCogwheelPosition(false, world, newPos, newAxis)) {
                            return PlacementOffset.fail();
                        }

                        return PlacementOffset.success().at(newPos).withTransform((s) -> s.setValue(MaterialCogWheelBlock.AXIS, newAxis));
                    }
                }

                return PlacementOffset.fail();
            }
        }
    }

    @Override
    public String getDescriptionId() {
        return getBlock().getDescriptionId();
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return getDescriptionId();
    }

    @Override
    public Component getDescription() {
        return getBlock().getName();
    }

    @Override
    public Component getName(ItemStack stack) {
        return getDescription();
    }
}
