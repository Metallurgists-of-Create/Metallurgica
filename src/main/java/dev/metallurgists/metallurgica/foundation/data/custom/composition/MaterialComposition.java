package dev.metallurgists.metallurgica.foundation.data.custom.composition;

import dev.metallurgists.metallurgica.infastructure.material.Material;
import dev.metallurgists.metallurgica.infastructure.material.MaterialHelper;
import dev.metallurgists.metallurgica.infastructure.element.data.SubComposition;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record MaterialComposition(Material material, List<SubComposition> compositions) {

    public static final Codec<MaterialComposition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MaterialHelper.byNameCodec().fieldOf("material").forGetter(MaterialComposition::material),
            Codec.list(SubComposition.CODEC).fieldOf("compositions").forGetter(MaterialComposition::compositions)
    ).apply(instance, MaterialComposition::new));

}
