package com.freezedown.metallurgica.registry;

import com.freezedown.metallurgica.Metallurgica;
import com.freezedown.metallurgica.content.fluids.channel.channel_depot.ChannelDepotBlock;
import com.freezedown.metallurgica.content.fluids.channel.channel_depot.ChannelDepotGenerator;
import com.freezedown.metallurgica.content.fluids.fluid_shower.FluidShowerBlock;
import com.freezedown.metallurgica.content.machines.vat.floatation_cell.FloatationCellBlock;
import com.freezedown.metallurgica.content.metalworking.casting.ingot.IngotCastingMoldBlock;
import com.freezedown.metallurgica.content.metalworking.casting.ingot.IngotCastingMoldGenerator;
import com.freezedown.metallurgica.content.fluids.faucet.FaucetBlock;
import com.freezedown.metallurgica.content.fluids.faucet.FaucetGenerator;
import com.freezedown.metallurgica.content.machines.reaction_basin.ReactionBasinBlock;
import com.freezedown.metallurgica.content.machines.reaction_basin.ReactionBasinGenerator;
import com.freezedown.metallurgica.content.machines.reverbaratory.ReverbaratoryBlock;
import com.freezedown.metallurgica.content.machines.shaking_table.ShakingTableBlock;
import com.freezedown.metallurgica.content.machines.shaking_table.ShakingTableGenerator;
import com.freezedown.metallurgica.content.mineral.drill.drill_activator.DrillActivatorBlock;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerBlock;
import com.freezedown.metallurgica.content.mineral.drill.drill_tower.DrillTowerDeployerBlock;
import com.freezedown.metallurgica.content.primitive.ceramic.UnfiredCeramicBlock;
import com.freezedown.metallurgica.content.primitive.ceramic.ceramic_mixing_pot.CeramicMixingPotBlock;
import com.freezedown.metallurgica.content.primitive.ceramic.ceramic_pot.CeramicPotBlock;
import com.freezedown.metallurgica.content.primitive.log_pile.AshedLogPileBlock;
import com.freezedown.metallurgica.content.primitive.log_pile.IgnitableLogPileBlock;
import com.freezedown.metallurgica.content.primitive.log_pile.LogPileBlock;
import com.freezedown.metallurgica.content.primitive.log_pile.LogPileGenerator;
import com.freezedown.metallurgica.content.primitive.log_pile.charred_pile.CharredLogPileBlock;
import com.freezedown.metallurgica.content.primitive.mortar.MortarAndPestleBlock;
import com.freezedown.metallurgica.content.temperature.DebugTempBlock;
import com.freezedown.metallurgica.foundation.MBuilderTransformers;
import com.freezedown.metallurgica.foundation.config.server.subcat.MStress;
import com.freezedown.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import com.freezedown.metallurgica.foundation.multiblock.FluidOutputBlock;
import com.freezedown.metallurgica.registry.material.init.MetMaterialBlocks;
import com.simibubi.create.AllDisplaySources;
import com.simibubi.create.AllMountedStorageTypes;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.palettes.ConnectedGlassBlock;
import com.simibubi.create.content.logistics.depot.DepotBlock;
import com.simibubi.create.content.logistics.depot.MountedDepotInteractionBehaviour;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.block.connected.HorizontalCTBehaviour;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.infrastructure.config.CStress;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;

import static com.simibubi.create.api.behaviour.display.DisplaySource.displaySource;
import static com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour.interactionBehaviour;
import static com.simibubi.create.api.contraption.storage.item.MountedItemStorageType.mountedItemStorage;
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

    public static final BlockEntry<FloatationCellBlock> floatationCell = registrate.block("floatation_cell", FloatationCellBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.sound(SoundType.COPPER))
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/floatation_cell"))))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/floatation_cell")))
            .build()
            .register();

    public static final BlockEntry<ReactionBasinBlock> reactionBasin = registrate.block("reaction_basin", ReactionBasinBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.strength(0.5F).sound(SoundType.STONE))
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> new ReactionBasinGenerator().generate(ctx, prov))
            .item()
            .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/reaction_basin/block_single")))
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


    public static final BlockEntry<MortarAndPestleBlock> mortarAndPestle = registrate.block("mortar_and_pestle", MortarAndPestleBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.DIRT))
            .transform(pickaxeOnly())
            .simpleItem()
            .lang("Mortar And Pestle")
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


    public static void register() {
        MetallurgicaRegistrate materialRegistrate = (MetallurgicaRegistrate) Metallurgica.registrate().setCreativeTab(MCreativeTabs.MATERIALS);
        MetMaterialBlocks.generateMaterialBlocks(materialRegistrate);
        MetMaterialBlocks.MATERIAL_BLOCKS = MetMaterialBlocks.MATERIAL_BLOCKS_BUILDER.build();

        MetMaterialBlocks.MATERIAL_BLOCKS_BUILDER = null;
    }
}
