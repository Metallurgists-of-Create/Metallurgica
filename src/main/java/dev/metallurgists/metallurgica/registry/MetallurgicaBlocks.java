package dev.metallurgists.metallurgica.registry;

import com.simibubi.create.content.fluids.tank.FluidTankModel;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.content.blocks.SaltBlock;
import dev.metallurgists.metallurgica.content.fluids.channel.channel_depot.ChannelDepotBlock;
import dev.metallurgists.metallurgica.content.fluids.channel.channel_depot.ChannelDepotGenerator;
import dev.metallurgists.metallurgica.content.fluids.fluid_shower.FluidShowerBlock;
import dev.metallurgists.metallurgica.content.machines.vat.floatation_cell.FlotationCellBlock;
import dev.metallurgists.metallurgica.content.metalworking.casting.ingot.IngotCastingMoldBlock;
import dev.metallurgists.metallurgica.content.metalworking.casting.ingot.IngotCastingMoldGenerator;
import dev.metallurgists.metallurgica.content.fluids.faucet.FaucetBlock;
import dev.metallurgists.metallurgica.content.fluids.faucet.FaucetGenerator;
import dev.metallurgists.metallurgica.content.machines.reverbaratory.ReverbaratoryBlock;
import dev.metallurgists.metallurgica.content.machines.shaking_table.ShakingTableBlock;
import dev.metallurgists.metallurgica.content.machines.shaking_table.ShakingTableGenerator;
import dev.metallurgists.metallurgica.content.mineral.drill.drill_activator.DrillActivatorBlock;
import dev.metallurgists.metallurgica.content.mineral.drill.drill_tower.DrillTowerBlock;
import dev.metallurgists.metallurgica.content.mineral.drill.drill_tower.DrillTowerDeployerBlock;
import dev.metallurgists.metallurgica.content.primitive.bellows.intake.BellowsIntakeBlock;
import dev.metallurgists.metallurgica.content.primitive.bellows.intake.BellowsIntakeGenerator;
import dev.metallurgists.metallurgica.content.primitive.brick_kiln.base.BrickKilnBaseBlock;
import dev.metallurgists.metallurgica.content.primitive.brick_kiln.base.BrickKilnBaseGenerator;
import dev.metallurgists.metallurgica.content.primitive.brick_kiln.chamber.BrickKilnChamberBlock;
import dev.metallurgists.metallurgica.content.primitive.brick_kiln.chamber.BrickKilnChamberGenerator;
import dev.metallurgists.metallurgica.content.primitive.ceramic.UnfiredCeramicBlock;
import dev.metallurgists.metallurgica.content.primitive.ceramic.ceramic_mixing_pot.CeramicMixingPotBlock;
import dev.metallurgists.metallurgica.content.primitive.ceramic.ceramic_pot.CeramicPotBlock;
import dev.metallurgists.metallurgica.content.primitive.log_pile.AshedLogPileBlock;
import dev.metallurgists.metallurgica.content.primitive.log_pile.IgnitableLogPileBlock;
import dev.metallurgists.metallurgica.content.primitive.log_pile.LogPileBlock;
import dev.metallurgists.metallurgica.content.primitive.log_pile.LogPileGenerator;
import dev.metallurgists.metallurgica.content.primitive.log_pile.charred_pile.CharredLogPileBlock;
import dev.metallurgists.metallurgica.content.temperature.DebugTempBlock;
import dev.metallurgists.metallurgica.content.temperature.hot_plate.HotPlateBlock;
import dev.metallurgists.metallurgica.content.temperature.hot_plate.HotPlateModel;
import dev.metallurgists.metallurgica.foundation.MBlockStateGen;
import dev.metallurgists.metallurgica.foundation.MBuilderTransformers;
import dev.metallurgists.metallurgica.foundation.config.server.subcat.MStress;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.foundation.multiblock.FluidOutputBlock;
import dev.metallurgists.metallurgica.registry.material.init.MetMaterialBlocks;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.palettes.ConnectedGlassBlock;
import com.simibubi.create.foundation.block.connected.HorizontalCTBehaviour;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;

import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.*;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

@SuppressWarnings("removal")
public class MetallurgicaBlocks {
    private static final MetallurgicaRegistrate registrate = (MetallurgicaRegistrate) Metallurgica.registrate().setCreativeTab(MCreativeTabs.MAIN);

    public static final BlockEntry<FluidShowerBlock> fluidShower = registrate.block("fluid_shower", FluidShowerBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.sound(SoundType.COPPER))
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/fluid_shower"))))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/fluid_shower")))
            .build()
            .register();

    public static final BlockEntry<FlotationCellBlock> flotationCell = registrate.block("flotation_cell", FlotationCellBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.sound(SoundType.COPPER))
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/floatation_cell"))))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/floatation_cell")))
            .build()
            .register();

    public static final BlockEntry<BellowsIntakeBlock> bellowsIntake = registrate.block("bellows_intake", BellowsIntakeBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.strength(0.5F).sound(SoundType.STONE).noOcclusion())
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> new BellowsIntakeGenerator("bronze").generate(ctx, prov))
            .item()
            .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
            .build()
            .register();

    public static final BlockEntry<BrickKilnChamberBlock> brickKilnChamber = registrate.block("brick_kiln_chamber", BrickKilnChamberBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.strength(0.5F).sound(SoundType.STONE).noOcclusion())
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate(new BrickKilnChamberGenerator()::generateState)
            .item()
            .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
            .build()
            .register();

    public static final BlockEntry<BrickKilnBaseBlock> brickKilnBase = registrate.block("brick_kiln_base", BrickKilnBaseBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.strength(0.5F).sound(SoundType.STONE).noOcclusion())
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate(new BrickKilnBaseGenerator()::generateState)
            .item()
            .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
            .build()
            .register();

    public static final BlockEntry<Block> refactoryBricks = registrate.block("refactory_bricks", Block::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.sound(SoundType.MUD_BRICKS).requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(MBlockStateGen.simpleCubeAllVariantBlock("block/decoration/bricks/refactory_bricks", 4))
            .item()
            .model((c, p) -> p.cubeAll(c.getName(), p.modLoc("block/decoration/bricks/refactory_bricks_0")))
            .build()
            .register();

    public static final BlockEntry<DebugTempBlock> debugTemp = registrate.block("debug_temp", DebugTempBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.strength(0.5F).sound(SoundType.STONE))
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/debug_temp"))))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/debug_temp")))
            .build()
            .register();

    public static final BlockEntry<IngotCastingMoldBlock> ingotCastingMold = registrate.block("ingot_casting_mold", IngotCastingMoldBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.strength(0.5F).sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> new IngotCastingMoldGenerator().generate(ctx, prov))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/ingot_casting_mold/item")))
            .build()
            .register();

    public static final BlockEntry<UnfiredCeramicBlock> unfiredCeramicPot = registrate.block("unfired_ceramic_pot", (p) -> new UnfiredCeramicBlock(p, MetallurgicaShapes.ceramicPot))
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().withExistingParent(ctx.getName(), prov.modLoc("block/ceramic_pot"))
                    .texture("particle", prov.modLoc("block/unfired_ceramic_pot_side"))
                    .texture("side", prov.modLoc("block/unfired_ceramic_pot_side"))
                    .texture("top", prov.modLoc("block/unfired_ceramic_pot_top"))
                    .texture("bottom", prov.modLoc("block/unfired_ceramic_pot_bottom"))
                    .texture("inner", prov.modLoc("block/unfired_ceramic_pot_inner"))
            ))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/ceramic_pot"))
                    .texture("particle", p.modLoc("block/unfired_ceramic_pot_side"))
                    .texture("side", p.modLoc("block/unfired_ceramic_pot_side"))
                    .texture("top", p.modLoc("block/unfired_ceramic_pot_top"))
                    .texture("bottom", p.modLoc("block/unfired_ceramic_pot_bottom"))
                    .texture("inner", p.modLoc("block/unfired_ceramic_pot_inner"))
            )
            .build()
            .register();

    public static final BlockEntry<UnfiredCeramicBlock> unfiredCrucible = registrate.block("unfired_crucible", (p) -> new UnfiredCeramicBlock(p, MetallurgicaShapes.crucible))
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().withExistingParent(ctx.getName(), prov.modLoc("block/crucible"))
                    .texture("particle", prov.modLoc("block/unfired_ceramic_pot_side"))
                    .texture("side", prov.modLoc("block/unfired_ceramic_pot_side"))
                    .texture("bottom", prov.modLoc("block/unfired_crucible_bottom"))
                    .texture("inner", prov.modLoc("block/unfired_ceramic_pot_inner"))
            ))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/crucible"))
                    .texture("particle", p.modLoc("block/unfired_ceramic_pot_side"))
                    .texture("side", p.modLoc("block/unfired_ceramic_pot_side"))
                    .texture("bottom", p.modLoc("block/unfired_crucible_bottom"))
                    .texture("inner", p.modLoc("block/unfired_ceramic_pot_inner"))
            )
            .build()
            .register();

    public static final BlockEntry<CeramicPotBlock> crucible = registrate.block("crucible", CeramicPotBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.sound(SoundType.DEEPSLATE))
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/crucible"))))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/crucible")))
            .build()
            .register();

    public static final BlockEntry<CeramicPotBlock> ceramicPot = registrate.block("ceramic_pot", CeramicPotBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/ceramic_pot"))))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/ceramic_pot")))
            .build()
            .register();
    
    public static final BlockEntry<CeramicMixingPotBlock> ceramicMixingPot = registrate.block("ceramic_mixing_pot", CeramicMixingPotBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/ceramic_pot"))))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/ceramic_pot")))
            .build()
            .register();
    
    public static final BlockEntry<Block> dirtyClay = registrate.block("dirty_clay", Block::new)
            .initialProperties(() -> Blocks.CLAY)
            .properties(p -> p.strength(0.6F).sound(SoundType.GRAVEL))
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            lt.applyExplosionDecay(b, LootItem.lootTableItem(MetallurgicaItems.dirtyClayBall.get()).apply(LimitCount.limitCount(IntRange.exact(4))))
                                            .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))))
            .simpleItem()
            .register();
    
    public static final BlockEntry<IgnitableLogPileBlock> logPile = registrate.block("log_pile", IgnitableLogPileBlock::new)
            .initialProperties(SharedProperties::wooden)
            .properties(p -> p.strength(2.0F).sound(SoundType.WOOD))
            .loot((lt, b) -> lt.add(b, LogPileBlock.buildLootTable(b)))
            .transform(axeOnly())
            .blockstate((ctx, prov) -> new LogPileGenerator().generateCustom(ctx, prov))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/log_pile/1"))
                    .texture("log", p.modLoc("block/log_pile"))
                    .texture("side", p.modLoc("block/log_pile_side")))
            .build()
            .register();
    
    public static final BlockEntry<CharredLogPileBlock> charredLogPile = registrate.block("charred_log_pile", CharredLogPileBlock::new)
            .initialProperties(SharedProperties::wooden)
            .properties(p -> p.strength(0.75F).sound(SoundType.WOOD))
            .loot((lt, b) -> lt.add(b, RegistrateBlockLootTables.noDrop()))
            .transform(axeOrPickaxe())
            .blockstate((ctx, prov) -> new LogPileGenerator().generateCustom(ctx, prov))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/log_pile/1"))
                    .texture("log", p.modLoc("block/charred_log_pile"))
                    .texture("side", p.modLoc("block/charred_log_pile_side")))
            .build()
            .register();
    
    public static final BlockEntry<AshedLogPileBlock> ashedCharcoalPile = registrate.block("ashed_charcoal_pile", AshedLogPileBlock::new)
            .initialProperties(SharedProperties::wooden)
            .properties(p -> p.strength(1.5F).sound(SoundType.WOOD))
            .loot((lt, b) -> lt.add(b, LogPileBlock.buildLootTable(b)))
            .transform(axeOrPickaxe())
            .blockstate((ctx, prov) -> new LogPileGenerator().generateCustom(ctx, prov))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/log_pile/1"))
                    .texture("log", p.modLoc("block/ashed_charcoal_pile"))
                    .texture("side", p.modLoc("block/ashed_charcoal_pile_side")))
            .build()
            .register();
    
    public static final BlockEntry<LogPileBlock> charcoalPile = registrate.block("charcoal_pile", LogPileBlock::new)
            .initialProperties(SharedProperties::wooden)
            .properties(p -> p.strength(1.5F).sound(SoundType.WOOD))
            .loot((lt, b) -> lt.add(b, LogPileBlock.charcoalLootTable(b)))
            .transform(axeOrPickaxe())
            .blockstate((ctx, prov) -> new LogPileGenerator().generateCustom(ctx, prov))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/log_pile/1"))
                    .texture("log", p.modLoc("block/charcoal_pile"))
                    .texture("side", p.modLoc("block/charcoal_pile_side")))
            .build()
            .register();
    
    public static final BlockEntry<ConnectedGlassBlock> blastProofGlass = registrate.block("blast_proof_glass", ConnectedGlassBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties((p) -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.GLASS).strength(1.5f, 8.0f))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::translucent)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.get()))
            .onRegister(connectedTextures(() -> new EncasedCTBehaviour(MetallurgicaSpriteShifts.blastProofGlass)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, MetallurgicaSpriteShifts.blastProofGlass)))
            .item()
            .build()
            .register();
    
    //public static final BlockEntry<RotaryKilnSegmentBlock> rotaryKilnSegment  = registrate.block("rotary_kiln_segment", RotaryKilnSegmentBlock::new)
    //        .initialProperties(SharedProperties::copperMetal)
    //        .properties(BlockBehaviour.Properties::noOcclusion)
    //        .addLayer(() -> RenderType::cutoutMipped)
    //        .transform(TagGen.pickaxeOnly())
    //        .blockstate(new RotaryKilnSegmentGenerator()::generate)
    //        .item().transform(customItemModel())
    //        .register();
    //
    //public static final BlockEntry<HeaterSegmentStructuralBlock> heaterSegmentStructural = registrate.block("heater_segment_structural", HeaterSegmentStructuralBlock::new)
    //        .initialProperties(SharedProperties::copperMetal)
    //        .blockstate((c, p) -> p.getVariantBuilder(c.get()).forAllStatesExcept(BlockStateGen.mapToAir(p), HeaterSegmentStructuralBlock.FACING))
    //        .properties(p -> p.noOcclusion().color(MaterialColor.COLOR_LIGHT_GRAY))
    //        .transform(pickaxeOnly())
    //        .lang("Rotary Kiln Heater Segment")
    //        .register();
    //
    //public static final BlockEntry<HeaterSegmentBlock> heaterSegment = registrate.block("heater_segment", HeaterSegmentBlock::new)
    //        .initialProperties(SharedProperties::copperMetal)
    //        .blockstate(new HeaterSegmentGenerator()::generate)
    //        .properties(p -> p.mapColor(MapColor.COLOR_LIGHT_GRAY))
    //        .transform(pickaxeOnly())
    //        .lang("Rotary Kiln Heater Segment")
    //        .simpleItem()
    //        .register();
    //public static final BlockEntry<TestCableConnectorBlock> testCableConnector = registrate.block("cable_connector", TestCableConnectorBlock::new)
    //        .initialProperties(() -> Blocks.IRON_BLOCK)
    //        .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
    //        .properties(BlockBehaviour.Properties::noOcclusion)
    //        .transform(TagGen.pickaxeOnly())
    //        .blockstate((ctx, prov) -> new TestCableConnectorGenerator().generate(ctx, prov))
    //        .item()
    //        .transform(ModelGen.customItemModel())
    //        .lang("Cable Connector")
    //        .register();
    //MACHINES
    public static final BlockEntry<ShakingTableBlock> shakingTable = registrate.block("shaking_table", ShakingTableBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(TagGen.pickaxeOnly())
            .transform(MStress.setImpact(12.0))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate(new ShakingTableGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();
    
    public static final BlockEntry<ReverbaratoryBlock> reverbaratory = registrate.block("reverbaratory", ReverbaratoryBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(TagGen.pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(false))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .simpleItem()
            .register();

    public static final BlockEntry<FluidOutputBlock> fluidOutput = registrate.block("fluid_output", FluidOutputBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(TagGen.pickaxeOnly())
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/reverbaratory/input_output"))))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .simpleItem()
            .register();
    
    public static final BlockEntry<DrillActivatorBlock> drillActivator = registrate.block("drill_activator", DrillActivatorBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(TagGen.pickaxeOnly())
            .transform(MStress.setImpact(6.0))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/drill_activator/block"))))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<DrillTowerDeployerBlock> drillTowerDeployer = registrate.block("drill_tower_deployer", DrillTowerDeployerBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(TagGen.pickaxeOnly())
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/drill_tower_deployer/block"))))
            .simpleItem()
            .register();

    public static final BlockEntry<DrillTowerBlock> drillTower = registrate.block("drill_tower", DrillTowerBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(TagGen.pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/drill_tower/block"))))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<Block> drillExpansion = registrate.block("drill_expansion", Block::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_YELLOW))
            .properties(p -> p.strength(3))
            .transform(pickaxeOnly())
            .onRegister(connectedTextures(() -> new HorizontalCTBehaviour(MetallurgicaSpriteShifts.drillExpansion)))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, MetallurgicaSpriteShifts.drillExpansion)))
            .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                    .cubeColumn(c.getName(), p.modLoc("block/" + c.getName()), p.modLoc("block/" + c.getName() + "_end"))))
            .simpleItem()
            .lang("Drill Expansion")
            .register();
//METAL BLOCKS
    //CAST
    //public static final BlockEntry<Block>
    //        bronzeBlock =              registrate.simpleMachineBlock("bronze_block", "Bronze Block", Block::new, SoundType.METAL, (c, p) -> p.simpleBlock(c.get()));
    //public static final BlockEntry<Block>
    //        arsenicalBronzeBlock =              registrate.simpleMachineBlock("arsenical_bronze_block", "Arsenical Bronze Block", Block::new, SoundType.METAL, (c, p) -> p.simpleBlock(c.get()));
    public static final BlockEntry<Block>
            impureBronzeBlock =              registrate.simpleMachineBlock("impure_bronze_block", "Impure Bronze Block", Block::new, SoundType.METAL, (c, p) -> p.simpleBlock(c.get()));
    //SMITHED
    //public static final BlockEntry<ConnectedPillarBlock>
    //        wroughtIronBlock =              registrate.directionalMetalBlock("wrought_iron_block", "Wrought Iron Block", ConnectedPillarBlock::new, SoundType.METAL, (c, p) -> p.axisBlock(c.get()));


    //SAND
    public static final BlockEntry<SandBlock> quartzSand = registrate.block("quartz_sand", p -> new SandBlock(0x8D8388, p))
            .properties(p -> p.sound(SoundType.SAND))
            .loot(RegistrateBlockLootTables::dropSelf)
            .simpleItem()
            .register();

    public static final BlockEntry<SandBlock> blackSand = registrate.block("black_sand", p -> new SandBlock(0x8D8388, p))
            .properties(p -> p.sound(SoundType.SAND))
            .loot(RegistrateBlockLootTables::dropSelf)
            .simpleItem()
            .register();

    public static final BlockEntry<SandBlock> siliconSand = registrate.block("silicon_sand", p -> new SandBlock(0x8D8388, p))
            .properties(p -> p.sound(SoundType.SAND))
            .loot(RegistrateBlockLootTables::dropSelf)
            .simpleItem()
            .register();
                //Are we sure we're still adding this guy?
    public static final BlockEntry<SandBlock> magnetiteTracedSand = registrate.block("magnetite_traced_sand", p -> new SandBlock(0x8D8388, p))
            .transform(MBuilderTransformers.mineralStone("magnetite_traced_sand"))
            .loot((lt, bl) -> lt.add(bl,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(bl,
                            lt.applyExplosionDecay(bl, LootItem.lootTableItem(Items.SAND)))))
            .register();

    //SIMPLE MACHINES
    public static final BlockEntry<ChannelDepotBlock>
            channelDepot =             registrate.simpleMachineBlock("channel_depot", null, ChannelDepotBlock::new, SoundType.LODESTONE, new ChannelDepotGenerator()::generate);
    
    //public static final BlockEntry<ChannelBlock>
    //        channel =                  registrate.simpleMachineBlock("channel", null, ChannelBlock::new, SoundType.LODESTONE, new ChannelGenerator()::generate);
    //TODO
    //.properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
    // .properties(BlockBehaviour.Properties::noOcclusion)
    
    public static final BlockEntry<FaucetBlock>
            faucet =                   registrate.simpleMachineBlock("faucet", null, FaucetBlock::new, SoundType.LODESTONE, new FaucetGenerator()::generate);
    
    //public static final BlockEntry<TuyereBlock>
    //        tuyere =                   registrate.simpleMachineBlock("tuyere", "Tuyere", TuyereBlock::new, SoundType.COPPER, (c, p) -> p.simpleBlock(c.get()));
//
    //public static final BlockEntry<HearthBlock>
    //        hearth =                   registrate.simpleMachineBlock("hearth", "Hearth", HearthBlock::new, SoundType.COPPER, (c, p) -> p.horizontalBlock(c.get(), p.models().getExistingFile(p.modLoc("block/hearth"))));

    public static final BlockEntry<Block>
            carbonBrick =              registrate.simpleMachineBlock("carbon_brick", "Carbon Brick", Block::new, SoundType.DEEPSLATE_BRICKS, (c, p) -> p.simpleBlock(c.get()));

    //public static final BlockEntry<CastingTable>
    //        castingTable =             registrate.simpleMachineBlock("casting_table", null, CastingTable::new, SoundType.DEEPSLATE_BRICKS, (c, p) -> p.simpleBlock(c.getEntry(), p.models().getExistingFile(p.modLoc("block/casting_table"))));

    public static final BlockEntry<Block> layeredCoal = registrate.block("layered_coal", Block::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.sound(MetallurgicaSoundTypes.COAL).requiresCorrectToolForDrops())
            .transform(pickaxeOnly())
            .blockstate(MBlockStateGen.variantMineralBottomTop("layered_coal", 4))
            .loot((lt, b) -> lt.add(b,
                    RegistrateBlockLootTables.createSilkTouchDispatchTable(b,
                            lt.applyExplosionDecay(b, LootItem.lootTableItem(Items.COAL).apply(LimitCount.limitCount(IntRange.range(4, 9))))
                                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))))
            .item()
            .model((c, p) -> p.cubeColumn(c.getName(), p.modLoc("block/palettes/minerals/layered_coal/side_0"), p.modLoc("block/palettes/minerals/layered_coal/top_0")))
            .build()
            .register();

    public static final BlockEntry<SaltBlock> saltBlock = registrate.block("salt_block", SaltBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.sound(SoundType.SAND))
            .tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/nozzle/small"))))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/nozzle/small")))
            .build()
            .register();

    public static final BlockEntry<HotPlateBlock> hotPlate = registrate.block("hot_plate", HotPlateBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.strength(0.5F).sound(SoundType.NETHERITE_BLOCK))
            .onRegister(CreateRegistrate.blockModel(() -> HotPlateModel::standard))
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/hot_plate/block"))))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/hot_plate/item")))
            .build()
            .register();

    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }

    public static void register() {
        MetallurgicaRegistrate materialRegistrate = (MetallurgicaRegistrate) Metallurgica.registrate().setCreativeTab(MCreativeTabs.MATERIALS);
        MetMaterialBlocks.generateMaterialBlocks(materialRegistrate);
        MetMaterialBlocks.MATERIAL_BLOCKS = MetMaterialBlocks.MATERIAL_BLOCKS_BUILDER.build();

        MetMaterialBlocks.MATERIAL_BLOCKS_BUILDER = null;
    }
}
