package dev.metallurgists.metallurgica.foundation.registrate;

import com.simibubi.create.api.registry.SimpleRegistry;
import com.simibubi.create.api.registry.registrate.SimpleBuilder;
import com.simibubi.create.impl.registry.TagProviderImpl;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Literally just SimpleBuilder but with the option to associate items.
 * @see SimpleBuilder
 */
public class SimplerBuilder<R, T extends R, P> extends AbstractBuilder<R, T, P, SimplerBuilder<R, T, P>> {
    private final Supplier<T> value;

    private SimpleRegistryAccess<Block, R> byBlock;
    private SimpleRegistryAccess<BlockEntityType<?>, R> byBlockEntity;
    private SimpleRegistryAccess<EntityType<?>, R> byEntity;
    private SimpleRegistryAccess<Fluid, R> byFluid;
    private SimpleRegistryAccess<Item, R> byItem;
    
    public SimplerBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceKey<Registry<R>> registryKey, Supplier<T> value) {
        super(owner, parent, name, callback, registryKey);
        this.value = value;
    }

    @Override
    protected T createEntry() {
        return this.value.get();
    }

    // for setup

    @SuppressWarnings("deprecation")
    public SimplerBuilder<R, T, P> byBlock(SimpleRegistry<Block, R> registry) {
        this.byBlock = SimpleRegistryAccess.of(registry, Block::builtInRegistryHolder);
        return this;
    }

    @SuppressWarnings("deprecation")
    public SimplerBuilder<R, T, P> byBlock(SimpleRegistry.Multi<Block, R> registry) {
        this.byBlock = SimpleRegistryAccess.of(registry, Block::builtInRegistryHolder);
        return this;
    }

    public SimplerBuilder<R, T, P> byBlockEntity(SimpleRegistry<BlockEntityType<?>, R> registry) {
        this.byBlockEntity = SimpleRegistryAccess.of(registry, TagProviderImpl::getBeHolder);
        return this;
    }

    public SimplerBuilder<R, T, P> byBlockEntity(SimpleRegistry.Multi<BlockEntityType<?>, R> registry) {
        this.byBlockEntity = SimpleRegistryAccess.of(registry, TagProviderImpl::getBeHolder);
        return this;
    }

    @SuppressWarnings("deprecation")
    public SimplerBuilder<R, T, P> byEntity(SimpleRegistry<EntityType<?>, R> registry) {
        this.byEntity = SimpleRegistryAccess.of(registry, EntityType::builtInRegistryHolder);
        return this;
    }

    @SuppressWarnings("deprecation")
    public SimplerBuilder<R, T, P> byEntity(SimpleRegistry.Multi<EntityType<?>, R> registry) {
        this.byEntity = SimpleRegistryAccess.of(registry, EntityType::builtInRegistryHolder);
        return this;
    }

    @SuppressWarnings("deprecation")
    public SimplerBuilder<R, T, P> byFluid(SimpleRegistry<Fluid, R> registry) {
        this.byFluid = SimpleRegistryAccess.of(registry, Fluid::builtInRegistryHolder);
        return this;
    }

    @SuppressWarnings("deprecation")
    public SimplerBuilder<R, T, P> byFluid(SimpleRegistry.Multi<Fluid, R> registry) {
        this.byFluid = SimpleRegistryAccess.of(registry, Fluid::builtInRegistryHolder);
        return this;
    }

    @SuppressWarnings("deprecation")
    public SimplerBuilder<R, T, P> byItem(SimpleRegistry<Item, R> registry) {
        this.byItem = SimpleRegistryAccess.of(registry, Item::builtInRegistryHolder);
        return this;
    }

    @SuppressWarnings("deprecation")
    public SimplerBuilder<R, T, P> byItem(SimpleRegistry.Multi<Item, R> registry) {
        this.byItem = SimpleRegistryAccess.of(registry, Item::builtInRegistryHolder);
        return this;
    }

    // association methods

    public SimplerBuilder<R, T, P> associate(Block block) {
        assertPresent(this.byBlock, "Block");
        this.onRegister(value -> this.byBlock.adder.accept(block, value));
        return this;
    }

    public SimplerBuilder<R, T, P> associateBlockTag(TagKey<Block> tag) {
        assertPresent(this.byBlock, "Block");
        this.onRegister(value -> this.byBlock.tagAdder.accept(tag, value));
        return this;
    }

    public SimplerBuilder<R, T, P> associate(BlockEntityType<?> type) {
        assertPresent(this.byBlockEntity, "BlockEntityType");
        this.onRegister(value -> this.byBlockEntity.adder.accept(type, value));
        return this;
    }

    public SimplerBuilder<R, T, P> associateBeTag(TagKey<BlockEntityType<?>> tag) {
        assertPresent(this.byBlockEntity, "BlockEntityType");
        this.onRegister(value -> this.byBlockEntity.tagAdder.accept(tag, value));
        return this;
    }

    public SimplerBuilder<R, T, P> associate(EntityType<?> type) {
        assertPresent(this.byEntity, "EntityType");
        this.onRegister(value -> this.byEntity.adder.accept(type, value));
        return this;
    }

    public SimplerBuilder<R, T, P> associateEntityTag(TagKey<EntityType<?>> tag) {
        assertPresent(this.byEntity, "EntityType");
        this.onRegister(value -> this.byEntity.tagAdder.accept(tag, value));
        return this;
    }

    public SimplerBuilder<R, T, P> associate(Fluid fluid) {
        assertPresent(this.byFluid, "Fluid");
        this.onRegister(value -> this.byFluid.adder.accept(fluid, value));
        return this;
    }

    public SimplerBuilder<R, T, P> associateFluidTag(TagKey<Fluid> tag) {
        assertPresent(this.byFluid, "Fluid");
        this.onRegister(value -> this.byFluid.tagAdder.accept(tag, value));
        return this;
    }

    public SimplerBuilder<R, T, P> associate(Item item) {
        assertPresent(this.byItem, "Item");
        this.onRegister(value -> this.byItem.adder.accept(item, value));
        return this;
    }

    public SimplerBuilder<R, T, P> associateItemTag(TagKey<Item> tag) {
        assertPresent(this.byItem, "Item");
        this.onRegister(value -> this.byItem.tagAdder.accept(tag, value));
        return this;
    }

    private static void assertPresent(@Nullable Object object, String type) {
        if (object == null) {
            throw new IllegalStateException("This type does not support " + type + " associations");
        }
    }

    protected record SimpleRegistryAccess<K, V>(BiConsumer<K, V> adder, BiConsumer<TagKey<K>, V> tagAdder) {
        public static <K, V> SimpleRegistryAccess<K, V> of(SimpleRegistry<K, V> registry, Function<K, Holder<K>> holderGetter) {
            return new SimpleRegistryAccess<>(
                    registry::register,
                    (tag, value) -> registry.registerProvider(SimpleRegistry.Provider.forTag(tag, holderGetter, value))
            );
        }

        public static <K, V> SimpleRegistryAccess<K, V> of(SimpleRegistry.Multi<K, V> registry, Function<K, Holder<K>> holderGetter) {
            return new SimpleRegistryAccess<>(
                    registry::add,
                    (tag, value) -> registry.addProvider(SimpleRegistry.Provider.forTag(tag, holderGetter, value))
            );
        }
    }
}
