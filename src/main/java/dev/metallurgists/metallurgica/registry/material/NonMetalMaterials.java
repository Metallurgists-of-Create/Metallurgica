package dev.metallurgists.metallurgica.registry.material;

import dev.metallurgists.metallurgica.infastructure.material.registry.flags.block.SheetmetalFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.block.StorageBlockFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.fluid.LiquidFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.fluid.MoltenFlag;
import dev.metallurgists.metallurgica.infastructure.material.registry.flags.item.*;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaElements;

import static dev.metallurgists.metallurgica.registry.material.MetMaterials.*;
import static dev.metallurgists.metallurgica.infastructure.material.registry.flags.FlagKey.*;


public class NonMetalMaterials {
    public static void register() {
        // Null :3
        NULL = createMaterial("null", (b) -> b
                .element(MetallurgicaElements.NULL)
                .meltingPoint(9999.0)
                .addFlags(
                        new NuggetFlag().requiresCompacting(),
                        new IngotFlag().requiresCompacting(),
                        new StorageBlockFlag().requiresDecompacting(),
                        new SheetmetalFlag().requiresCompacting(),
                        new SheetFlag().pressTimes(5),
                        new MoltenFlag(9999.0),
                        new DustFlag(),
                        new SpoolFlag(1, 0xb32aca),
                        new MineralFlag(true),
                        new RubbleFlag().crushing().bonusChance(0.001f)
                ));

        // --- Actual Non-Metal Materials --- //
        SILICON = createMaterial("silicon", (b) -> b
                .element(MetallurgicaElements.SILICON)
                .noRegister(INGOT, LIQUID)
                .meltingPoint(1414.0)
                .addFlags(
                        new NuggetFlag(),
                        new IngotFlag("tfmg"),
                        new StorageBlockFlag().useColumnModel(),
                        new SheetFlag(),
                        new MoltenFlag(1414.0),
                        new DustFlag(),
                        new LiquidFlag("tfmg")
                ));

        GRAPHITE = createMaterial("graphite", (b) -> b
                .element(MetallurgicaElements.CARBON)
                .meltingPoint(3652.0)
                .addFlags(
                        new MineralFlag(),
                        new MoltenFlag(3652.0),
                        new DustFlag()
        ));

        COAL_COKE = createMaterial("coal_coke", (b) -> b
                .element(MetallurgicaElements.CARBON)
                .noRegister(GEM,DUST,STORAGE_BLOCK)
                .existingIds(GEM, "tfmg:coal_coke")
                .addFlags(
                        new StorageBlockFlag("tfmg"),
                        new GemFlag("tfmg"),
                        new DustFlag("tfmg", false)
        ));

        COAL = createMaterial("coal", (b) -> b
                .element(MetallurgicaElements.CARBON)
                .noRegister(GEM,STORAGE_BLOCK)
                .existingIds(GEM, "minecraft:coal")
                .addFlags(
                        new GemFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new DustFlag()
        ));

        DIAMOND = createMaterial("diamond", (b) -> b
                .element(MetallurgicaElements.CARBON)
                .noRegister(GEM, STORAGE_BLOCK)
                .existingIds(GEM, "minecraft:diamond")
                .addFlags(
                        new GemFlag("minecraft"),
                        new StorageBlockFlag("minecraft"),
                        new NuggetFlag(true),
                        new DustFlag()
        ));

        CHARCOAL = createMaterial("charcoal", (b) -> b
                .element(MetallurgicaElements.CARBON)
                .noRegister(GEM)
                .existingIds(GEM, "minecraft:charcoal")
                .addFlags(
                        new GemFlag("minecraft"),
                        new DustFlag()
        ));

        SULFUR = createMaterial("sulfur", (b) -> b
                .element(MetallurgicaElements.SULFUR)
                .noRegister(DUST)
                .addFlags(
                        new DustFlag("tfmg", false)
                ));

        QUARTZ = createMaterial("quartz", (b) -> b
                .composition(MetallurgicaElements.SILICON, 1, MetallurgicaElements.OXYGEN, 2)
                .noRegister(GEM)
                .existingIds(GEM, "minecraft:quartz")
                .addFlags(
                        new GemFlag("minecraft"),
                        new DustFlag()
                ));
    }
}
