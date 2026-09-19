package dev.metallurgists.metallurgica.registry;

import com.mojang.serialization.MapCodec;
import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.loot.ReplaceItemLootModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class MetallurgicaLootModifiers {
    private MetallurgicaLootModifiers() {}

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Metallurgica.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ReplaceItemLootModifier>> REPLACE_ITEM = LOOT_MODIFIERS.register(
            "replace_item",
            () -> ReplaceItemLootModifier.CODEC
    );

    public static void init(IEventBus modEventBus) {
        LOOT_MODIFIERS.register(modEventBus);
    }
}
