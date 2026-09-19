package dev.metallurgists.metallurgica.foundation.registrate;

import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableType;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableTypeBuilder;
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.metallurgists.metallurgica.Metallurgica;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class MetallurgicaRegistrate extends CreateRegistrate {
    public static final List<String> COMP_MOD_BLACKLIST = new ArrayList<>();

    protected MetallurgicaRegistrate(String modId) {
        super(modId);
        COMP_MOD_BLACKLIST.add(Metallurgica.MOD_ID);
    }

    public static MetallurgicaRegistrate create(String modId) {
        return new MetallurgicaRegistrate(modId).setTooltipModifierFactory(item ->
                new ItemDescription.Modifier(item, Metallurgica.PALETTE)
                        .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    public MetallurgicaRegistrate setTooltipModifierFactory(@Nullable Function<Item, TooltipModifier> factory) {
        currentTooltipModifierFactory = factory;
        return this;
    }

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

    public <T extends Item> ItemEntry<T> item(String name, NonNullFunction<Item.Properties, T> factory, NonNullUnaryOperator<Item.Properties> properties, String... tags) {
        ItemBuilder<T, ?> builder = this.item(name, factory).properties(properties);
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

    public ItemEntry<Item> simpleItem(String name, String... tags) {
        return item(name, Item::new, p->p, tags);
    }

    public ItemEntry<Item> rubble(String name) {
        return this.item(name, Item::new)
                .tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","material_rubble/" + name)))
                .tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","material_rubble")))
                .lang(autoLang(name))
                .register();
    }

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
                .simpleItem();
        b = lang != null ? b.lang(lang) : b;
        return b.register();
    }
}