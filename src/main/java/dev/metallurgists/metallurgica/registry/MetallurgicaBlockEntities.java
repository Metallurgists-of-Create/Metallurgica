package dev.metallurgists.metallurgica.registry;

import dev.metallurgists.metallurgica.compat.rutile.RutileBlockEntities;
import dev.metallurgists.metallurgica.content.fluids.channel.channel_depot.ChannelDepotBlockEntity;
import dev.metallurgists.metallurgica.content.fluids.channel.channel_depot.ChannelDepotRenderer;
import dev.metallurgists.metallurgica.content.fluids.fluid_shower.FluidShowerBlockEntity;
import dev.metallurgists.metallurgica.content.machines.vat.floatation_cell.FlotationCellBlockEntity;
import dev.metallurgists.metallurgica.content.machines.vat.floatation_cell.FlotationCellRenderer;
import dev.metallurgists.metallurgica.content.metalworking.casting.ingot.IngotCastingMoldBlockEntity;
import dev.metallurgists.metallurgica.content.metalworking.casting.ingot.IngotCastingMoldRenderer;
import dev.metallurgists.metallurgica.content.fluids.faucet.FaucetBlockEntity;
import dev.metallurgists.metallurgica.content.machines.reverbaratory.ReverbaratoryBlockEntity;
import dev.metallurgists.metallurgica.content.machines.shaking_table.ShakingTableBlockEntity;
import dev.metallurgists.metallurgica.content.machines.shaking_table.ShakingTableVisual;
import dev.metallurgists.metallurgica.content.machines.shaking_table.ShakingTableRenderer;
import dev.metallurgists.metallurgica.content.mineral.drill.drill_activator.DrillActivatorBlockEntity;
import dev.metallurgists.metallurgica.content.mineral.drill.drill_activator.DrillActivatorRenderer;
import dev.metallurgists.metallurgica.content.mineral.drill.drill_tower.DrillTowerDeployerBlockEntity;
import dev.metallurgists.metallurgica.content.primitive.bellows.intake.BellowsIntakeBlockEntity;
import dev.metallurgists.metallurgica.content.primitive.bellows.intake.BellowsIntakeRenderer;
import dev.metallurgists.metallurgica.content.primitive.ceramic.UnfiredCeramicBlockEntity;
import dev.metallurgists.metallurgica.content.primitive.ceramic.UnfiredCeramicBlockRenderer;
import dev.metallurgists.metallurgica.content.primitive.ceramic.ceramic_mixing_pot.CeramicMixingPotBlockEntity;
import dev.metallurgists.metallurgica.content.primitive.ceramic.ceramic_mixing_pot.CeramicMixingPotVisual;
import dev.metallurgists.metallurgica.content.primitive.ceramic.ceramic_mixing_pot.CeramicMixingPotRenderer;
import dev.metallurgists.metallurgica.content.primitive.ceramic.ceramic_pot.CeramicPotBlockEntity;
import dev.metallurgists.metallurgica.content.primitive.ceramic.ceramic_pot.CeramicPotRenderer;
import dev.metallurgists.metallurgica.content.primitive.log_pile.charred_pile.CharredLogPileBlockEntity;
import dev.metallurgists.metallurgica.content.temperature.DebugTempBlockEntity;
import dev.metallurgists.metallurgica.foundation.multiblock.FluidOutputBlockEntity;
import dev.metallurgists.metallurgica.registry.material.init.MetMaterialBlockEntities;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static dev.metallurgists.metallurgica.Metallurgica.registrate;

public class MetallurgicaBlockEntities {
    public static final BlockEntityEntry<DebugTempBlockEntity> debugTemp = registrate
            .blockEntity("debug_temp", DebugTempBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.debugTemp)
            .register();
    public static final BlockEntityEntry<IngotCastingMoldBlockEntity> ingotCastingMold = registrate
            .blockEntity("ingot_casting_mold", IngotCastingMoldBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.ingotCastingMold)
            .renderer(() -> IngotCastingMoldRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidShowerBlockEntity> fluidShower = registrate
            .blockEntity("fluid_shower", FluidShowerBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.fluidShower)
            .register();

    public static final BlockEntityEntry<FlotationCellBlockEntity> floatationCell = registrate
            .blockEntity("flotation_cell", FlotationCellBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.flotationCell)
            .renderer(() -> FlotationCellRenderer::new)
            .register();

    public static final BlockEntityEntry<BellowsIntakeBlockEntity> bellowsIntake = registrate
            .blockEntity("bellows_intake", BellowsIntakeBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.bellowsIntake)
            .renderer(() -> BellowsIntakeRenderer::new)
            .register();
     
    public static final BlockEntityEntry<UnfiredCeramicBlockEntity> unfiredCeramic = registrate
            .blockEntity("unfired_ceramic", UnfiredCeramicBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.unfiredCeramicPot, MetallurgicaBlocks.unfiredCrucible)
            .renderer(() -> UnfiredCeramicBlockRenderer::new)
            .register();

    
    public static final BlockEntityEntry<CeramicPotBlockEntity> ceramicPot = registrate
            .blockEntity("ceramic_pot", CeramicPotBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.ceramicPot)
            .renderer(() -> CeramicPotRenderer::new)
            .register();

    public static final BlockEntityEntry<CeramicPotBlockEntity> crucible = registrate
            .blockEntity("crucible", CeramicPotBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.crucible)
            .renderer(() -> CeramicPotRenderer::new)
            .register();

    public static final BlockEntityEntry<CeramicMixingPotBlockEntity> ceramicMixingPot = registrate
            .blockEntity("ceramic_mixing_pot", CeramicMixingPotBlockEntity::new)
            .visual(() -> CeramicMixingPotVisual::new)
            .validBlocks(MetallurgicaBlocks.ceramicMixingPot)
            .renderer(() -> CeramicMixingPotRenderer::new)
            .register();
    
    public static final BlockEntityEntry<DrillActivatorBlockEntity> drillActivator =
            registrate.blockEntity("drill_activator", DrillActivatorBlockEntity::new)
                    .renderer(() -> DrillActivatorRenderer::new)
                    .validBlocks(MetallurgicaBlocks.drillActivator)
                    .register();

    public static final BlockEntityEntry<DrillTowerDeployerBlockEntity> drillTowerDeployer =
            registrate.blockEntity("drill_tower_deployer", DrillTowerDeployerBlockEntity::new)
                    .validBlocks(MetallurgicaBlocks.drillTowerDeployer)
                    .register();

    //public static final BlockEntityEntry<MineralDepositBlockEntity> mineralDeposit = registrate.simpleBlockEntity("mineral_deposit", MineralDepositBlockEntity::new).register();

    public static final BlockEntityEntry<ChannelDepotBlockEntity> channelDepot =
            registrate.blockEntity("channel_depot", ChannelDepotBlockEntity::new)
                    .renderer(() -> ChannelDepotRenderer::new)
                    .validBlocks(MetallurgicaBlocks.channelDepot)
                    .register();


    public static final BlockEntityEntry<FaucetBlockEntity> faucet =
            registrate.blockEntity("faucet", FaucetBlockEntity::new)
                    .validBlocks(MetallurgicaBlocks.faucet)
                    .register();

    public static final BlockEntityEntry<ReverbaratoryBlockEntity> reverbaratory = registrate
            .blockEntity("reverbaratory", ReverbaratoryBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.reverbaratory)
            .register();

    public static final BlockEntityEntry<FluidOutputBlockEntity> fluidOutput = registrate
            .blockEntity("fluid_output", FluidOutputBlockEntity::new)
            .validBlocks(MetallurgicaBlocks.fluidOutput)
            .register();
    
    public static final BlockEntityEntry<ShakingTableBlockEntity> shakingTable =
            registrate.blockEntity("shaking_table", ShakingTableBlockEntity::new)
                    .visual(() -> ShakingTableVisual::new)
                    .renderer(() -> ShakingTableRenderer::new)
                    .validBlocks(MetallurgicaBlocks.shakingTable)
                    .register();
    
    public static final BlockEntityEntry<CharredLogPileBlockEntity> charredLogPile =
            registrate.blockEntity("charred_log_pile", CharredLogPileBlockEntity::new)
                    .validBlocks(MetallurgicaBlocks.charredLogPile).register();

    public static void register() {
        RutileBlockEntities.register(registrate().getModEventBus());
    }
}
