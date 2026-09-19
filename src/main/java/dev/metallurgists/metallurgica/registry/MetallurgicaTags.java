package dev.metallurgists.metallurgica.registry;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.lang.MetallurgicaLang;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public final class MetallurgicaTags {
    private MetallurgicaTags() {}

    public enum NameSpace {
        MOD(Metallurgica.MOD_ID),
        COMMON("c");

        public final String id;
        NameSpace(String id) {
            this.id = id;
        }
    }

    public enum Blocks {
        C_ORES(NameSpace.COMMON, "ores"),
        ;

        public final TagKey<Block> tag;

        Blocks() {
            this(NameSpace.MOD);
        }

        Blocks(NameSpace namespace) {
            this(namespace, null);
        }

        Blocks(NameSpace namespace, String path) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path == null ? MetallurgicaLang.asId(name()) : path);
            this.tag = BlockTags.create(id);
        }

        Blocks(String namespace, String path) {
            this.tag = BlockTags.create(ResourceLocation.fromNamespaceAndPath(namespace, path));
        }
    }

    public enum Biomes {
        GOLD_SURFACE_DEPOSIT(NameSpace.MOD, "has_gold_surface_desosit")
        ;

        public final TagKey<Biome> tag;

        Biomes() {
            this(NameSpace.MOD);
        }

        Biomes(NameSpace namespace) {
            this(namespace, null);
        }

        Biomes(NameSpace namespace, String path) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace.id, path);
            this.tag = TagKey.create(Registries.BIOME, id);
        }

        Biomes(String namespace, String path) {
            this.tag = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(namespace, path));
        }
    }
}
