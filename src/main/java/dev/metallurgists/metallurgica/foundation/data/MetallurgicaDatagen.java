package dev.metallurgists.metallurgica.foundation.data;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.foundation.data.advancement.MetallurgicaAdvancements;
import dev.metallurgists.metallurgica.foundation.data.recipe.MProcessingRecipeGen;
import dev.metallurgists.metallurgica.foundation.data.recipe.create.MSequencedAssemblyGen;
import dev.metallurgists.metallurgica.foundation.data.recipe.metallurgica.MFluidReactionGen;
import dev.metallurgists.metallurgica.foundation.data.recipe.vanilla.MStandardRecipeGen;
import dev.metallurgists.metallurgica.foundation.ponder.MetallurgicaPonderPlugin;
import dev.metallurgists.metallurgica.foundation.units.MetallurgicaUnits;
import dev.metallurgists.metallurgica.registry.MetallurgicaBiomeTemperatures;
import dev.metallurgists.metallurgica.registry.MetallurgicaCompositions;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaRegistries;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import dev.metallurgists.rutile.api.composition.element.Element;
import dev.metallurgists.rutile.api.registry.RutileRegistries;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static dev.metallurgists.metallurgica.foundation.util.TextUtil.toEnglishName;

public class MetallurgicaDatagen {
    public static void gatherData(GatherDataEvent event) {
        addExtraRegistrateData();
        
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        
        boolean client = event.includeClient();
        boolean server = event.includeServer();
        MetallurgicaGeneratedEntriesProvider generatedEntriesProvider = new MetallurgicaGeneratedEntriesProvider(output, lookupProvider);
        lookupProvider = generatedEntriesProvider.getRegistryProvider();
        generator.addProvider(event.includeServer(), generatedEntriesProvider);

        generator.addProvider(server, new MetallurgicaAdvancements(output));
        if (server) {
            generator.addProvider(true, new MStandardRecipeGen(generator));
            generator.addProvider(true, new MFluidReactionGen(generator));
            MProcessingRecipeGen.registerAll(generator);
            MetallurgicaCompositions.register(generator);
            MetallurgicaBiomeTemperatures.register(generator);
            generator.addProvider(true, new  MSequencedAssemblyGen(generator));
        }
    }
    
    private static void addExtraRegistrateData() {
        MetallurgicaRegistrateTags.addGenerators();
        
        Metallurgica.registrate.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;
            provideDefaultLang("interface", langConsumer);
            provideDefaultLang("tooltips", langConsumer);
            provideDefaultLang("materials", langConsumer);
            RutileRegistries.ELEMENTS.entries().forEach((entry) -> {
                ResourceLocation key = entry.getKey();
                Element element = entry.getValue();
                if (key.getNamespace().equals("metallurgica")) {
                    provider.add(element.getOrCreateDescriptionId(), toEnglishName(key.getPath()));
                }
            });
            MetallurgicaAdvancements.provideLang(langConsumer);
            //MetallurgicaElements.provideElementLang(langConsumer);
            MetallurgicaUnits.provideUnitLang(langConsumer);
            providePonderLang(langConsumer);
        });
    }
    
    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = "assets/metallurgica/lang/default/" + fileName + ".json";
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            throw new IllegalStateException(String.format("Could not find default lang file: %s", path));
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            consumer.accept(key, value);
        }
    }

    private static void providePonderLang(BiConsumer<String, String> consumer) {
        // Register this since FMLClientSetupEvent does not run during datagen
        PonderIndex.addPlugin(new MetallurgicaPonderPlugin());

        PonderIndex.getLangAccess().provideLang(Metallurgica.ID, consumer);
    }
}
