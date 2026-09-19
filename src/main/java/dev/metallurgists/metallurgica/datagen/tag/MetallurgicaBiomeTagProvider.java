package dev.metallurgists.metallurgica.datagen.tag;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.registry.MetallurgicaTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
public class MetallurgicaBiomeTagProvider extends BiomeTagsProvider {
    public MetallurgicaBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, Metallurgica.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(MetallurgicaTags.Biomes.GOLD_SURFACE_DEPOSIT.tag)
                .addTag(BiomeTags.IS_BADLANDS);
    }
}
