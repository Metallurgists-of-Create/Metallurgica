package dev.metallurgists.metallurgica.foundation.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ReplaceItemLootModifier extends LootModifier {
    public static final MapCodec<ReplaceItemLootModifier> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            LOOT_CONDITIONS_CODEC
                                    .fieldOf("conditions")
                                    .forGetter(modifier -> modifier.conditions),
                            Replacement.CODEC
                                    .listOf()
                                    .fieldOf("replacements")
                                    .forGetter(modifier -> modifier.replacements)
                    ).apply(instance, ReplaceItemLootModifier::new)
            );

    private final List<Replacement> replacements;

    public ReplaceItemLootModifier(LootItemCondition[] conditions, List<Replacement> replacements) {
        super(conditions);
        this.replacements = replacements;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (int i = 0; i < generatedLoot.size(); i++) {
            ItemStack stack = generatedLoot.get(i);

            for (Replacement replacement : replacements) {
                if (stack.is(replacement.input().item())) {
                    generatedLoot.set(i, new ItemStack(replacement.output().item(), stack.getCount()));
                    break;
                }
            }
        }

        return generatedLoot;
    }

    public static ItemEntry entry(Item item) {
        return new ItemEntry(item);
    }

    public static Replacement replace(Item oldItem, Item newItem) {
        return new Replacement(entry(oldItem), entry(newItem));
    }

    public static Replacement replace(ItemEntry oldItem, ItemEntry newItem) {
        return new Replacement(oldItem, newItem);
    }

    public record ItemEntry(Item item) {
        public static final Codec<ItemEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(ItemEntry::item)
        ).apply(instance, ItemEntry::new));
    }

    public record Replacement(ItemEntry input, ItemEntry output) {
        public static final Codec<Replacement> CODEC =
                RecordCodecBuilder.create(instance -> instance.group(
                        ItemEntry.CODEC
                                .fieldOf("input")
                                .forGetter(Replacement::input),
                        ItemEntry.CODEC
                                .fieldOf("output")
                                .forGetter(Replacement::output)
                ).apply(instance, Replacement::new));
    }
}