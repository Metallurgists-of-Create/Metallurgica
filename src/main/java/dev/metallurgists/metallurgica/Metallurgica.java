package dev.metallurgists.metallurgica;

import dev.metallurgists.metallurgica.compat.cbc.BigCannonsCompat;
import dev.metallurgists.metallurgica.content.fluids.types.open_ended_pipe.OpenEndedPipeEffects;
import dev.metallurgists.metallurgica.events.CommonEvents;
import dev.metallurgists.metallurgica.experimental.ExperimentalEvents;
import dev.metallurgists.metallurgica.foundation.registrate.MetallurgicaRegistrate;
import dev.metallurgists.metallurgica.foundation.config.MetallurgicaConfigs;
import dev.metallurgists.metallurgica.foundation.data.MetallurgicaDatagen;
import dev.metallurgists.metallurgica.foundation.worldgen.MetallurgicaFeatures;
import dev.metallurgists.metallurgica.foundation.worldgen.MetallurgicaPlacementModifiers;
import dev.metallurgists.metallurgica.foundation.temperature.server.TemperatureHandler;
import dev.metallurgists.metallurgica.registry.*;
import dev.metallurgists.metallurgica.registry.material.*;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaElements;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaHeatingCoils;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaRegistries;
import dev.metallurgists.metallurgica.registry.misc.MetallurgicaSpecialRecipes;
import dev.metallurgists.metallurgica.world.MetallurgicaOreFeatureConfigEntries;
import dev.metallurgists.metallurgica.world.biome_modifier.SurfaceDepositsModifier;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;

import static com.simibubi.create.foundation.item.TooltipHelper.styleFromColor;

@Mod(Metallurgica.ID)
public class Metallurgica
{
    public static final String ID = "metallurgica";
    public static final String DISPLAY_NAME = "Metallurgica";
    public static final Logger LOGGER = LogUtils.getLogger();
    
    
    public static final MetallurgicaRegistrate registrate = MetallurgicaRegistrate.create(ID);
    
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Metallurgica.ID);
    //public static final RegistryObject<Codec<? extends OreModifier>> oreGen_CODEC = BIOME_MODIFIERS.register("generation_ores", () -> Codec.unit(OreModifier.INSTANCE));
    public static final RegistryObject<Codec<? extends SurfaceDepositsModifier>> surfaceDeposits_CODEC = BIOME_MODIFIERS.register("generation_surface_deposits", () -> Codec.unit(SurfaceDepositsModifier.INSTANCE));
    public static final FontHelper.Palette METALLURGICA_PALETTE = new FontHelper.Palette(styleFromColor(0x383d59), styleFromColor(0x717388));
    static {
        registrate.setTooltipModifierFactory((item) -> (new ItemDescription.Modifier(item, METALLURGICA_PALETTE)).andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }
    
    public Metallurgica() {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get()
                .getModEventBus();

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        registrate.registerEventListeners(modEventBus);
        MetallurgicaRegistries.BasicRegistries.register();
        MetallurgicaElements.register();
        MetallurgicaHeatingCoils.register();
        //BIOME_MODIFIERS.register(modEventBus);
        initMaterials(modEventBus);
        MetallurgicaLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        MCreativeTabs.register(modEventBus);
        MetallurgicaBlockEntities.register();
        //modEventBus.addGenericListener(Conductor.class, MetallurgicaConductors::register);
        MetallurgicaConductors.register();
        MetallurgicaBlocks.register();
        MetallurgicaItems.register();
        MetallurgicaSpecialRecipes.register(modEventBus);
        MetallurgicaFluids.register();
        MetallurgicaEffects.register(modEventBus);
        MetallurgicaRecipeTypes.register(modEventBus);
        MetallurgicaPackets.registerPackets();
        MetallurgicaOreFeatureConfigEntries.init();
        MetallurgicaFeatures.register(modEventBus);
        MetallurgicaPlacementModifiers.register(modEventBus);
        MetallurgicaEntityTypes.register();

        MetallurgicaConfigs.register(modLoadingContext);

        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.register(new CommonEvents());
        
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(Metallurgica::init);
        modEventBus.addListener(EventPriority.LOWEST, MetallurgicaDatagen::gatherData);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> MetallurgicaClient::new);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MetallurgicaClient.onCtorClient(modEventBus, forgeEventBus));
        modEventBus.addListener(MCreativeTabs::addCreative);
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    public static void init(final FMLCommonSetupEvent event) {
        LOGGER.info("Setting up Metallurgica's Open Ended Pipe Effects!");
        OpenEndedPipeEffects.init();
        BigCannonsCompat.register();
        MetallurgicaBlockSpoutingBehaviours.registerDefaults();
        MinecraftForge.EVENT_BUS.register(new ExperimentalEvents());
    };

    public static void initMaterials(IEventBus modEventBus) {
        MetalMaterials.register();
        NonMetalMaterials.register();
        AlloyMaterials.register();
        MineralMaterials.register();
        CompoundMaterials.register();
        MetMaterials.register();
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }
    
    @SubscribeEvent
    public void onServerStart(ServerAboutToStartEvent event)
    {
        LOGGER.info("Thanks for using Metallurgica! Expect a severe lack of ores in your world :3");
        TemperatureHandler.generateMap(event.getServer());
        //if (AllOreFeatureConfigEntries.ZINC_ORE != null)
        //    AllOreFeatureConfigEntries.ZINC_ORE.frequency.set(0.0);
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("Double checking our cool little ore frequency thingy :3");
        //if (AllOreFeatureConfigEntries.ZINC_ORE != null)
        //    AllOreFeatureConfigEntries.ZINC_ORE.frequency.set(0.0);
    }

    public static boolean isClientThread() {
        return isClientSide() && Minecraft.getInstance().isSameThread();
    }

    public static boolean isClientSide() {
        return FMLEnvironment.dist.isClient();
    }

    public static Path getGameDir() {
        return FMLPaths.GAMEDIR.get();
    }

    @Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(ID, path);
    }
    public static @NotNull MetallurgicaRegistrate registrate() {
        return registrate;
    }
}
