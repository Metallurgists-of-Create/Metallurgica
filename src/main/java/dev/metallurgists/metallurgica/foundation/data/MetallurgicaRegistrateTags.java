package dev.metallurgists.metallurgica.foundation.data;

import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGPaletteStoneTypes;
import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.registry.MetallurgicaBlocks;
import dev.metallurgists.metallurgica.registry.MetallurgicaTags;
import dev.metallurgists.metallurgica.registry.MetallurgicaTags.AllBlockTags;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.decoration.palettes.AllPaletteBlocks;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;

public class MetallurgicaRegistrateTags {
    public MetallurgicaRegistrateTags() {
    }

    public static void addGenerators() {
        Metallurgica.registrate.addDataGenerator(ProviderType.BLOCK_TAGS, MetallurgicaRegistrateTags::genBlockTags);
        Metallurgica.registrate.addDataGenerator(ProviderType.ITEM_TAGS, MetallurgicaRegistrateTags::genItemTags);
        Metallurgica.registrate.addDataGenerator(ProviderType.FLUID_TAGS, MetallurgicaRegistrateTags::genFluidTags);
        Metallurgica.registrate.addDataGenerator(ProviderType.ENTITY_TAGS, MetallurgicaRegistrateTags::genEntityTags);
    }

    private static void genBlockTags(RegistrateTagsProvider<Block> provIn) {
        TagGen.CreateTagsProvider<Block> prov = new TagGen.CreateTagsProvider<>(provIn, Block::builtInRegistryHolder);
        prov.tag(MetallurgicaTags.AllBlockTags.BAUXITE_ORE_REPLACEABLE.tag)
                .add(TFMGPaletteStoneTypes.BAUXITE.getBaseBlock().get())
                ;
        prov.tag(MetallurgicaTags.AllBlockTags.REVERBARATORY_GLASS.tag)
                .add(
                        AllPaletteBlocks.FRAMED_GLASS.get(),
                        AllPaletteBlocks.TILED_GLASS.get(),
                        AllPaletteBlocks.HORIZONTAL_FRAMED_GLASS.get(),
                        AllPaletteBlocks.VERTICAL_FRAMED_GLASS.get()
                )
                .addTag(MetallurgicaTags.AllBlockTags.REVERBARATORY_WALL.tag);
        prov.tag(MetallurgicaTags.AllBlockTags.REVERBARATORY_WALL.tag)
                .add(
                        MetallurgicaBlocks.carbonBrick.get(),
                        TFMGBlocks.FIREPROOF_BRICKS.get()
                );
        prov.tag(MetallurgicaTags.AllBlockTags.REVERBARATORY_INPUT.tag)
                .add(
                        AllBlocks.CHUTE.get()
                );
        //prov.tag(MetallurgicaTags.AllBlockTags.DEPOSITS.tag)
        //        .add(
        //                MetallurgicaOre.FLUORITE.ORE.depositBlock().get()
        //        );
        prov.tag(MetallurgicaTags.AllBlockTags.AIR_BLOCKING.tag)
                .add(
                        MetallurgicaBlocks.logPile.get(),
                        MetallurgicaBlocks.ashedCharcoalPile.get(),
                        MetallurgicaBlocks.charredLogPile.get(),
                        MetallurgicaBlocks.charcoalPile.get()
                ).addTag(BlockTags.DIRT);
        
        prov.tag(AllBlockTags.CERAMIC_HEAT_SOURCES.tag)
                .add(
                        Blocks.MAGMA_BLOCK,
                        Blocks.TORCH,
                        Blocks.WALL_TORCH,
                        Blocks.SOUL_TORCH,
                        Blocks.SOUL_WALL_TORCH
                )
                .addTag(BlockTags.FIRE)
                .addTag(BlockTags.CAMPFIRES);
        prov.tag(AllBlockTags.LOW_HEAT_SOURCES.tag)
                .add(
                        Blocks.MAGMA_BLOCK,
                        Blocks.TORCH,
                        Blocks.WALL_TORCH,
                        Blocks.SOUL_TORCH,
                        Blocks.SOUL_WALL_TORCH
                );
        prov.tag(AllBlockTags.MEDIUM_HEAT_SOURCES.tag)
                .addTag(BlockTags.FIRE)
                .addTag(BlockTags.CAMPFIRES);
        for (MetallurgicaTags.AllBlockTags tag : MetallurgicaTags.AllBlockTags.values()) {
            if (tag.alwaysDatagen) {
                prov.getOrCreateRawBuilder(tag.tag);
            }
        }
    }

    private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
        TagGen.CreateTagsProvider<Item> prov = new TagGen.CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);
        
        for (MetallurgicaTags.AllItemTags tag : MetallurgicaTags.AllItemTags.values()) {
            if (tag.alwaysDatagen) {
                prov.getOrCreateRawBuilder(tag.tag);
            }
        }
        
        prov.tag(MetallurgicaTags.AllItemTags.IGNITES_LOG_PILE.tag).add(
                Items.FLINT_AND_STEEL,
                Items.FIRE_CHARGE
        );
        
        prov.tag(MetallurgicaTags.AllItemTags.NEEDS_CHEMICAL_FORMULA_TOOLTIP.tag).add(
                //Ingots
                Items.IRON_INGOT,
                Items.GOLD_INGOT,
                Items.COPPER_INGOT,
                TFMGItems.ALUMINUM_INGOT.get(),
                AllItems.ZINC_INGOT.get(),
                AllItems.BRASS_INGOT.get(),
                
                //Nuggets
                Items.IRON_NUGGET,
                Items.GOLD_NUGGET,
                AllItems.COPPER_NUGGET.get(),
                AllItems.ZINC_NUGGET.get(),
                AllItems.BRASS_NUGGET.get(),
                
                //Misc Items
                TFMGItems.SULFUR_DUST.get(),
                TFMGBlocks.SULFUR.get().asItem(),
                TFMGItems.NITRATE_DUST.get(),
                TFMGItems.LIMESAND.get()
        );
    }
    
    private static void genFluidTags(RegistrateTagsProvider<Fluid> provIn) {
        TagGen.CreateTagsProvider<Fluid> prov = new TagGen.CreateTagsProvider<>(provIn, Fluid::builtInRegistryHolder);
        prov.tag(MetallurgicaTags.modFluidTag("fluid_reactive/chlorine")).add(TFMGFluids.GASOLINE.get().getFlowing(), TFMGFluids.GASOLINE.get().getSource());
        
        prov.tag(MetallurgicaTags.AllFluidTags.REVERBARATORY_FUELS.tag)
                .add(
                        TFMGFluids.BUTANE.getSource(),
                        TFMGFluids.PROPANE.getSource(),
                        TFMGFluids.LPG.getSource(),
                        TFMGFluids.KEROSENE.getSource(),
                        TFMGFluids.NAPHTHA.getSource(),
                        TFMGFluids.ETHYLENE.getSource(),
                        TFMGFluids.PROPYLENE.getSource(),
                        TFMGFluids.DIESEL.getSource(),
                        TFMGFluids.LUBRICATION_OIL.getSource(),
                        TFMGFluids.HEAVY_OIL.getSource(),
                        TFMGFluids.CREOSOTE.getSource(),
                        TFMGFluids.GASOLINE.getSource()
                );
    }
    
    private static void genEntityTags(RegistrateTagsProvider<EntityType<?>> prov) {
    
    }
}
