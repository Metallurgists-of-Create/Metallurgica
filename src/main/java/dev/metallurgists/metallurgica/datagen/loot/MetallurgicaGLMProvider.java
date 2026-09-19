package dev.metallurgists.metallurgica.datagen.loot;

import com.simibubi.create.AllItems;
import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.loot.ReplaceItemLootModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MetallurgicaGLMProvider extends GlobalLootModifierProvider {
    public MetallurgicaGLMProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Metallurgica.MOD_ID);
    }

    @Override
    protected void start() {
        add("replace_ingots_modifier", new ReplaceItemLootModifier(
                new LootItemCondition[] {},
                List.of(
                        replace(Items.IRON_INGOT, AllItems.CRUSHED_IRON.get()),
                        replace(Items.GOLD_INGOT, AllItems.CRUSHED_GOLD.get()),
                        replace(Items.COPPER_INGOT, AllItems.CRUSHED_COPPER.get())
                )));
    }

    private static ReplaceItemLootModifier.Replacement replace(Item oldItem, Item newItem) {
        return ReplaceItemLootModifier.replace(oldItem, newItem);
    }
}
