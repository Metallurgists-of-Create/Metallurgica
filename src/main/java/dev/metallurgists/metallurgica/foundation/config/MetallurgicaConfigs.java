package dev.metallurgists.metallurgica.foundation.config;

import dev.metallurgists.metallurgica.foundation.config.client.MClient;
import dev.metallurgists.metallurgica.foundation.config.common.MCommon;
import dev.metallurgists.metallurgica.foundation.config.server.MServer;
import dev.metallurgists.metallurgica.foundation.config.server.subcat.MStress;
import com.simibubi.create.api.stress.BlockStressValues;
import net.createmod.catnip.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class MetallurgicaConfigs {
    
    private static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);
    
    private static MClient client;
    private static MCommon common;
    private static MServer server;
    public static MClient client() {
        return client;
    }
    
    public static MCommon common() {
        return common;
    }
    
    public static MServer server() {
        return server;
    }
    
    public static ConfigBase byType(ModConfig.Type type) {
        return CONFIGS.get(type);
    }
    
    private static <T extends ConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
        Pair<T, ForgeConfigSpec> specPair = (new ForgeConfigSpec.Builder()).configure((builder) -> {
            T config = factory.get();
            config.registerAll(builder);
            return config;
        });
        T config = specPair.getLeft();
        config.specification = specPair.getRight();
        CONFIGS.put(side, config);
        return config;
    }
    
    public static void register(ModLoadingContext context) {
        server = register(MServer::new, ModConfig.Type.SERVER);
        common = register(MCommon::new, ModConfig.Type.COMMON);
        client = register(MClient::new, ModConfig.Type.CLIENT);
        
        for (Map.Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet()) {
            context.registerConfig(pair.getKey(), pair.getValue().specification);
        }

        MStress stress = server().kinetics.stressValues;
        BlockStressValues.IMPACTS.registerProvider(stress::getImpact);
        BlockStressValues.CAPACITIES.registerProvider(stress::getCapacity);
    }
    
    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        
        for (ConfigBase config : CONFIGS.values()) {
            if (config.specification == event.getConfig().getSpec()) {
                config.onLoad();
            }
        }
        
    }
    
    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading event) {
        
        for (ConfigBase config : CONFIGS.values()) {
            if (config.specification == event.getConfig().getSpec()) {
                config.onReload();
            }
        }
        
    }
}
