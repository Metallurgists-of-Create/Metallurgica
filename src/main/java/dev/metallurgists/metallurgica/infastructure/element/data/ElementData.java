package dev.metallurgists.metallurgica.infastructure.element.data;

import dev.metallurgists.metallurgica.foundation.util.ClientUtil;
import dev.metallurgists.metallurgica.infastructure.element.ElementEntry;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaElements;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaRegistries;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record ElementData(@Getter ResourceLocation element, @Getter int amount) {
    public static final Codec<ElementData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("element").forGetter(ElementData::getElement),
            Codec.INT.optionalFieldOf("amount", 1).forGetter(ElementData::getAmount)
    ).apply(instance, ElementData::new));

    public static ElementData create(ElementEntry<?> element) {
        return new ElementData(element.getId(), 1);
    }

    public static ElementData create(ElementEntry<?> element, int amount) {
        return new ElementData(element.getId(), amount);
    }

    public ElementData withAmount(int amount) {
        return new ElementData(element, amount);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("amount", getAmount());
        json.addProperty("element", getElement().toString());
        return json;
    }

    public static List<SubComposition> createComposition(SubComposition.Builder... subCompositionBuilders) {
        List<SubComposition> subCompositions = new ArrayList<>();
        for (SubComposition.Builder builder : subCompositionBuilders) {
            subCompositions.add(builder.build());
        }
        return subCompositions;
    }

    public static List<SubComposition> createFromList(List<ElementData> elementDataList) {
        List<SubComposition> subCompositions = new ArrayList<>();
        for (ElementData elementData : elementDataList) {
            subCompositions.add(new SubComposition(List.of(elementData), 1));
        }
        return subCompositions;
    }

    public String getDisplay() {
        StringBuilder display = new StringBuilder(MetallurgicaRegistries.BasicRegistries.registeredElements.getOrDefault(element, MetallurgicaElements.NULL.get()).getSymbol());
        if (amount > 1)
            display.append(amount);
        return ClientUtil.toSmallDownNumbers(display.toString());
    }

    public void writeToPacket(FriendlyByteBuf buf) {
        buf.writeResourceLocation(element);
        buf.writeInt(amount);
    }

    public static ElementData fromNetwork(FriendlyByteBuf buf) {
        ResourceLocation element = buf.readResourceLocation();
        int amount = buf.readInt();
        return new ElementData(element, amount);
    }

    public static List<ElementData> listFromNetwork(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<ElementData> elements = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            elements.add(fromNetwork(buf));
        }
        return elements;
    }
}
