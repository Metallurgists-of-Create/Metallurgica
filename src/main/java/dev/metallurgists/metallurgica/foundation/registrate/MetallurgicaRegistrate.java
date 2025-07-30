package dev.metallurgists.metallurgica.foundation.registrate;

import com.tterrag.registrate.util.OneTimeEventReceiver;
import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.compat.rutile.flags.objects.MoltenMetalFluid;
import dev.metallurgists.metallurgica.compat.rutile.flags.objects.VirtualMaterialFluid;
import dev.metallurgists.metallurgica.content.fluids.types.Acid;
import dev.metallurgists.metallurgica.content.fluids.types.TransparentTintedFluidType;
import dev.metallurgists.metallurgica.foundation.item.AlloyItem;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableType;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableTypeBuilder;
import dev.metallurgists.metallurgica.foundation.item.MetallurgicaItem;
import dev.metallurgists.metallurgica.registry.MetallurgicaSpriteShifts;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.palettes.ConnectedPillarBlock;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.foundation.block.connected.RotatedPillarCTBehaviour;
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.VirtualFluidBuilder;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.*;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.metallurgists.rutile.api.material.base.Material;
import dev.metallurgists.rutile.api.material.flag.types.IFluidRegistry;
import dev.metallurgists.rutile.api.material.registry.fluid.MaterialFluidType;
import dev.metallurgists.rutile.mixin.registrate.AbstractRegistrateAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class MetallurgicaRegistrate extends CreateRegistrate {

    /**
     * Construct a new Registrate for the given mod ID.
     *
     * @param modid The mod ID for which objects will be registered
     */
    protected MetallurgicaRegistrate(String modid) {
        super(modid);
    }

    // UTIL
    
    public static String autoLang(String id) {
        StringBuilder builder = new StringBuilder();
        boolean b = true;
        for (char c: id.toCharArray()) {
            if(c == '_') {
                builder.append(' ');
                b = true;
            } else {
                builder.append(b ? String.valueOf(c).toUpperCase() : c);
                b = false;
            }
        }
        return builder.toString();
    }

    public static MetallurgicaRegistrate create(String modid) {
        return new MetallurgicaRegistrate(modid);
    }
    private final AtomicBoolean registered = new AtomicBoolean(false);

    @Override
    public MetallurgicaRegistrate registerEventListeners(IEventBus bus) {
        if (!registered.getAndSet(true)) {
            // recreate the super method so we can register the event listener with LOW priority.
            Consumer<RegisterEvent> onRegister = this::onRegister;
            Consumer<RegisterEvent> onRegisterLate = this::onRegisterLate;
            bus.addListener(EventPriority.LOW, onRegister);
            bus.addListener(EventPriority.LOWEST, onRegisterLate);

            // Fired multiple times when ever tabs need contents rebuilt (changing op tab perms for example)
            bus.addListener(this::onBuildCreativeModeTabContents);
            // Register events fire multiple times, so clean them up on common setup
            OneTimeEventReceiver.addModListener(this, FMLCommonSetupEvent.class, $ -> {
                OneTimeEventReceiver.unregister(this, onRegister, RegisterEvent.class);
                OneTimeEventReceiver.unregister(this, onRegisterLate, RegisterEvent.class);
            });
            if (((AbstractRegistrateAccessor) this).getDoDatagen().get()) {
                OneTimeEventReceiver.addModListener(this, GatherDataEvent.class, this::onData);
            }
        }
        return this;
    }

    //FLUIDS
    public FluidBuilder<VirtualFluid, CreateRegistrate> tintedVirtualDust(String name, int color) {
        ResourceLocation still = Metallurgica.asResource("fluid/dust_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/dust_flow");
        return tintedVirtualFluid(name, color, still, flow);
    }

    public FluidBuilder<VirtualFluid, CreateRegistrate> tintedVirtualFluid(String name, int color) {
        return tintedVirtualFluid(name, color, Metallurgica.asResource("fluid/thin_fluid_still"), Metallurgica.asResource("fluid/thin_fluid_flow"));
    }

    public FluidBuilder<VirtualFluid, CreateRegistrate> tintedVirtualFluid(String name, int color, String textureType) {
        return tintedVirtualFluid(name, color, Metallurgica.asResource("fluid/"+textureType+"_still"), Metallurgica.asResource("fluid/"+textureType+"_flow"));
    }

    public FluidBuilder<VirtualFluid, CreateRegistrate> tintedVirtualFluid(String name, int color, ResourceLocation still, ResourceLocation flow) {
        return virtualFluid(name, still, flow, TransparentTintedFluidType.create(color), VirtualFluid::createSource, VirtualFluid::createFlowing);
    }

    public FluidBuilder<VirtualMaterialFluid, CreateRegistrate> materialVirtualFluid(String name, ResourceLocation still, ResourceLocation flow, Material material, IFluidRegistry flag) {
        return entry(name, c -> new VirtualFluidBuilder<>(self(), self(), name, c, still, flow, MaterialFluidType.create(material, flag), (p) -> VirtualMaterialFluid.createSource(p, material, flag), (p) -> VirtualMaterialFluid.createFlowing(p, material, flag)));
    }

    public FluidBuilder<VirtualMaterialFluid, CreateRegistrate> materialVirtualFluid(String name, ResourceLocation still, ResourceLocation flow, Material material, IFluidRegistry flag, boolean tint) {
        return entry(name, c -> new VirtualFluidBuilder<>(self(), self(), name, c, still, flow, MaterialFluidType.create(material, flag, tint), (p) -> VirtualMaterialFluid.createSource(p, material, flag), (p) -> VirtualMaterialFluid.createFlowing(p, material, flag)));
    }

    public FluidBuilder<MoltenMetalFluid, CreateRegistrate> moltenMetal(String name, Material material, IFluidRegistry flag, double moltenTemperature) {
        ResourceLocation still = Metallurgica.asResource("fluid/molten_metal_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/molten_metal_flow");
        return entry(name, c -> new VirtualFluidBuilder<>(self(), self(), name, c, still, flow,
                MaterialFluidType.create(material, flag, false),
                (p) -> MoltenMetalFluid.createSource(p, material, flag).meltingPoint(moltenTemperature),
                (p) -> MoltenMetalFluid.createFlowing(p, material, flag).meltingPoint(moltenTemperature)));
    }

    public FluidBuilder<ForgeFlowingFluid.Flowing, CreateRegistrate> tintedFluid(String name, int color) {
        ResourceLocation still = Metallurgica.asResource("fluid/thin_fluid_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/thin_fluid_flow");
        return fluid(name, still, flow, TransparentTintedFluidType.create(color), ForgeFlowingFluid.Flowing::new);
    }

    public FluidBuilder<Acid, CreateRegistrate> acid(String name, int color, float acidity) {
        ResourceLocation still = Metallurgica.asResource("fluid/thin_fluid_still");
        ResourceLocation flow = Metallurgica.asResource("fluid/thin_fluid_flow");
        return acid(name, color, still, flow, acidity);
    }

    public FluidEntry<Acid> acid(String name, int color, float acidity, String lang) {
        return Metallurgica.registrate.acid(name, color, acidity).lang(lang).register();
    }

    public FluidBuilder<Acid, CreateRegistrate> acid(String name, int color, ResourceLocation still, ResourceLocation flow, float acidity) {
        if (acidity > 14 || acidity < 0) {
            throw new IllegalArgumentException("Acidity must be between 0 and 14 for " + name);
        }
        return virtualFluid(name, still, flow, TransparentTintedFluidType.create(color), (p) -> Acid.createSource(p).acidity(acidity), (p) -> Acid.createFlowing(p).acidity(acidity));
    }

    //ITEM
    public <T extends Item> ItemEntry<T> item(String name, NonNullFunction<Item.Properties, T> factory, NonNullUnaryOperator<Item.Properties> properties, String... tags) {
        ItemBuilder<T, ?> builder = this.item(name, factory).properties(properties);
        for(String tag : tags) {
            builder.tag(AllTags.forgeItemTag(tag));
        }
        return builder.register();
    }

    public <T extends CableType> CableTypeBuilder<T, MetallurgicaRegistrate> cableType(NonNullFunction<CableType.Properties, T> factory) {
        return cableType((MetallurgicaRegistrate) self(), factory);
    }

    public <T extends CableType> CableTypeBuilder<T, MetallurgicaRegistrate> cableType(String name, NonNullFunction<CableType.Properties, T> factory) {
        return cableType((MetallurgicaRegistrate) self(), name, factory);
    }

    public <T extends CableType, P> CableTypeBuilder<T, P> cableType(P parent, NonNullFunction<CableType.Properties, T> factory) {
        return cableType(parent, currentName(), factory);
    }

    public <T extends CableType, P> CableTypeBuilder<T, P> cableType(P parent, String name, NonNullFunction<CableType.Properties, T> factory) {
        return entry(name, callback -> CableTypeBuilder.create(this, parent, name, callback, factory));
    }

    public enum Dimension {
        OVERWORLD(BiomeTags.IS_OVERWORLD),
        NETHER(BiomeTags.IS_NETHER),
        END(BiomeTags.IS_END);

        public final TagKey<Biome> biomeTag;

        Dimension(TagKey<Biome> biomeTag) {
            this.biomeTag = biomeTag;
        }

        public TagKey<Biome> biomeTag() {
            return biomeTag;
        }
    }
    

    public ItemEntry<Item> simpleItem(String name, String... tags) {
        return item(name, Item::new, p->p, tags);
    }

    public ItemEntry<MetallurgicaItem> metallurgicaItem(String name, String... tags) {
        return item(name, MetallurgicaItem::new, p->p, tags);
    }
    
    public ItemEntry<MetallurgicaItem> cluster(String name) {
        return this.item(name, MetallurgicaItem::new)
                .tag(AllTags.forgeItemTag("gem_clusters/" + name))
                .tag(AllTags.forgeItemTag("gem_clusters"))
                .lang(autoLang(name))
                .register();
    }

    public ItemEntry<MetallurgicaItem> raw(String name) {
        return this.item(name, MetallurgicaItem::new)
                .tag(AllTags.forgeItemTag("raw_materials/" + name))
                .tag(AllTags.forgeItemTag("raw_materials"))
                .lang(autoLang(name))
                .register();
    }

    public ItemEntry<Item> rubble(String name) {
        return this.item(name, Item::new)
                .tag(AllTags.forgeItemTag("material_rubble/" + name))
                .tag(AllTags.forgeItemTag("material_rubble"))
                .lang(autoLang(name))
                .register();
    }
    public ItemEntry<MetallurgicaItem> powder(String name) {
        return this.item(name, MetallurgicaItem::new)
                .tag(AllTags.forgeItemTag("powders/" + name))
                .tag(AllTags.forgeItemTag("powders"))
                .lang(autoLang(name))
                .register();
    }
    public ItemEntry<AlloyItem> alloyItem(String name, String... tags) {
        return item(name, AlloyItem::new, p->p, tags);
    }
    public ItemEntry<AlloyItem> alloyNugget(String name) {
        return this.item(name, AlloyItem::new)
                .tag(AllTags.forgeItemTag("nuggets/" + name))
                .tag(AllTags.forgeItemTag("nuggets"))
                .tag(AllTags.forgeItemTag("alloy_nuggets/" + name))
                .tag(AllTags.forgeItemTag("alloy_nuggets"))
                .lang(autoLang(name))
                .register();
    }
    public ItemEntry<AlloyItem> alloyDust(String name) {
        return this.item(name, AlloyItem::new)
                .tag(AllTags.forgeItemTag("dusts/" + name))
                .tag(AllTags.forgeItemTag("dusts"))
                .tag(AllTags.forgeItemTag("alloy_dusts/" + name))
                .tag(AllTags.forgeItemTag("alloy_dusts"))
                .lang(autoLang(name))
                .register();
    }
    public ItemEntry<AlloyItem> alloySheet(String name) {
        return this.item(name, AlloyItem::new)
                .tag(AllTags.forgeItemTag("plates/" + name))
                .tag(AllTags.forgeItemTag("plates"))
                .tag(AllTags.forgeItemTag("alloy_sheets/" + name))
                .tag(AllTags.forgeItemTag("alloy_sheets"))
                .lang(autoLang(name))
                .register();
    }

    //BLOCK ENTITY
    @Override
    public <T extends BlockEntity> @NotNull CreateBlockEntityBuilder<T, CreateRegistrate> blockEntity(String name,
                                                                                                      BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return blockEntity(self(), name, factory);
    }

    @Override
    public <T extends BlockEntity, P> @NotNull CreateBlockEntityBuilder<T, P> blockEntity(P parent, String name,
                                                                                          BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return (CreateBlockEntityBuilder<T, P>) entry(name,
                (callback) -> CreateBlockEntityBuilder.create(this, parent, name, callback, factory));
    }

    public <T extends BlockEntity> BlockEntityBuilder<T, CreateRegistrate> simpleBlockEntity(String name, BlockEntityBuilder.BlockEntityFactory<T> factory, NonNullSupplier<? extends Block>[] blocks) {
        BlockEntityBuilder<T, CreateRegistrate> builder = blockEntity(self(), name, factory);
        for (NonNullSupplier<? extends Block> block : blocks) {
            builder.validBlock(block);
        }
        return builder;
    }

    //BLOCKS
    //public BlockEntry<MineralDepositBlock> depositBlock(String name, ItemEntry<MetallurgicaItem> mineral) {
    //    return this.block(name, MineralDepositBlock::new)
    //            .transform(MBuilderTransformers.mineralDeposit())
    //            .loot((lt, bl) -> lt.add(bl, lt.createSingleItemTable(Items.COBBLESTONE)
    //                    .withPool(lt.applyExplosionCondition(mineral.get(), LootPool.lootPool()
    //                            .setRolls(UniformGenerator.between(2.0f, 5.0f))
    //                            .add(LootItem.lootTableItem(mineral.get()).apply(LimitCount.limitCount(IntRange.range(0, 1))))))))
    //            .lang(autoLang(name))
    //            .register();
    //}

    //public BlockEntry<MineralDepositBlock> depositBlock(String name, ItemEntry<MetallurgicaItem> mineral, boolean sideTop) {
    //    if (!sideTop)
    //        return depositBlock(name, mineral);
    //    return this.block(name, MineralDepositBlock::new)
    //            .transform(MBuilderTransformers.mineralDepositSideTop())
    //            .loot((lt, bl) -> lt.add(bl, lt.createSingleItemTable(Items.COBBLESTONE)
    //                    .withPool(lt.applyExplosionCondition(mineral.get(), LootPool.lootPool()
    //                            .setRolls(UniformGenerator.between(2.0f, 5.0f))
    //                            .add(LootItem.lootTableItem(mineral.get()).apply(LimitCount.limitCount(IntRange.range(0, 1))))))))
    //            .lang(autoLang(name))
    //            .register();
    //}

    //public BlockEntry<Block> mineralBlock(String name, TagKey<Block> tag, ItemEntry<MetallurgicaItem> mineral) {
    //    return this.block(name + "_rich_stone", Block::new)
    //            .transform(MBuilderTransformers.mineralStone(name))
    //            .loot((lt, bl) -> lt.add(bl, RegistrateBlockLootTables.createSilkTouchDispatchTable(bl, lt.applyExplosionDecay(bl, LootItem.lootTableItem(mineral.get()).apply(LimitCount.limitCount(IntRange.range(0, 1))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))))))
    //            .tag(tag)
    //            .lang(autoLang(name + "_rich_stone"))
    //            .register();
    //}

    public <T extends Block> BlockEntry<T> simpleMachineBlock(
            String name,
            @Nullable String lang,
            NonNullFunction<BlockBehaviour.Properties, T> builder,
            SoundType sound,
            NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> blockstate) {
        BlockBuilder<T, CreateRegistrate> b = this.block(name, builder)
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(sound))
                .transform(pickaxeOnly())
                .blockstate(blockstate)
//                .addLayer(() -> RenderType::cutoutMipped)
                .simpleItem();
        b = lang != null ? b.lang(lang) : b;
        return b.register();
    }

    public <T extends ConnectedPillarBlock> BlockEntry<T> directionalMetalBlock(
            String name,
            String lang,
            NonNullFunction<BlockBehaviour.Properties, T> builder,
            SoundType sound,
            NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> blockstate
    ) {
        BlockBuilder<T, CreateRegistrate> b = this.block(name, builder)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(sound))
                .blockstate(blockstate)
                .onRegister(connectedTextures(() -> new RotatedPillarCTBehaviour(MetallurgicaSpriteShifts.directionalMetalBlock, MetallurgicaSpriteShifts.directionalMetalBlock)))
                .simpleItem();
        b = b.lang(lang);
        return b.register();
    }
}
