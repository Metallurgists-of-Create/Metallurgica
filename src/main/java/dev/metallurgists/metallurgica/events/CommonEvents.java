package dev.metallurgists.metallurgica.events;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.experimental.exposure_effects.ExposureEffect;
import dev.metallurgists.metallurgica.experimental.exposure_effects.ExposureMinerals;
import dev.metallurgists.metallurgica.experimental.exposure_effects.ExposureUtil;
import dev.metallurgists.metallurgica.foundation.command.MetallurgicaCommands;
import dev.metallurgists.metallurgica.foundation.data.runtime.MetallurgicaDynamicDataPack;
import dev.metallurgists.metallurgica.foundation.data.runtime.MetallurgicaDynamicResourcePack;
import dev.metallurgists.metallurgica.foundation.data.runtime.MetallurgicaPackSource;
import dev.metallurgists.metallurgica.foundation.data.runtime.composition.RuntimeCompositions;
import dev.metallurgists.metallurgica.foundation.data.runtime.recipe.MetallurgicaRecipes;
import dev.metallurgists.metallurgica.foundation.temperature.server.TemperatureHandler;
import dev.metallurgists.metallurgica.foundation.util.recipe.helper.PhysicalRecipeHelper;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CommonEvents {

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        Level level = (Level) event.getLevel();
        if (level.isClientSide()) return; // skip client side

        LevelChunk chunk = (LevelChunk) event.getChunk();

        if(chunk.getFullStatus() == FullChunkStatus.BLOCK_TICKING) {
            TemperatureHandler handler = TemperatureHandler.getHandler((ServerLevel) level);
            handler.addLoadedChunk(chunk.getPos());
        }
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event) {
        Level level = (Level) event.getLevel();
        if (level.isClientSide()) return; // skip client side

        ChunkAccess chunk = event.getChunk();

        TemperatureHandler handler = TemperatureHandler.getHandler((ServerLevel) level);
        if(handler.getLoadedChunkPositions().contains(chunk.getPos())) {
            handler.removeLoadedChunk(chunk.getPos());
        }
    }

    @SubscribeEvent
    public void levelTickEvent(TickEvent.LevelTickEvent event) {
        if(event.level.isClientSide()) {
            return;
        }
        if(event.phase != TickEvent.Phase.START) {
            return;
        }
        if(event.level.getGameTime() % 5 != 0) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel) event.level;
        serverLevel.getEntities().get(EntityTypeTest.forClass(ItemEntity.class), PhysicalRecipeHelper::matchFluidReactionRecipe);
    }

    @SubscribeEvent
    public void serverTickEvent(net.minecraftforge.event.TickEvent.ServerTickEvent event) {
//        GasMovementHandler.handlers.forEach(
//                (pair, handler) -> {
//                    SimpleWeightedGraph<BlockPos, DefaultWeightedEdge> graph = handler.getGraph();
//                    List<DefaultWeightedEdge> removingEdges = new ArrayList<>();
//                    graph.edgeSet().forEach(edge -> {
//                        graph.setEdgeWeight(edge, graph.getEdgeWeight(edge) - 1d/(20 * 60));
//                        double weight = graph.getEdgeWeight(edge);
//                        if (weight <= 0)
//                            removingEdges.add(edge);
//                    });
//
//                    removingEdges.forEach(graph::removeEdge);
//
//                    List<BlockPos> removingVertexes = new ArrayList<>();
//
//                    graph.vertexSet().forEach(vertex -> {
//                        if (graph.edgesOf(vertex).isEmpty())
//                            removingVertexes.add(vertex);
//                    });
//
//                    removingVertexes.forEach(graph::removeVertex);
//                }
//        );
        if(event.phase == TickEvent.Phase.END) {
            //                    serverLevel.getEntities().getAll().forEach(FluidEntityInteractionHandler::handleInteraction);
//            event.getServer().getAllLevels().forEach(level -> TemperatureHandler.getHandler(level).tick());
            for(ServerLevel level : event.getServer().getAllLevels()) {
                TemperatureHandler handler = TemperatureHandler.getHandler(level);
                for(ChunkPos pos : handler.getLoadedChunkPositions()) {
                    if(level.getChunk(pos.x, pos.z).getFullStatus() != FullChunkStatus.BLOCK_TICKING) {
                        handler.removeLoadedChunk(pos);
                    }
                }
//                handler.tick();
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            // Do something every client tick
        }
    }

    @SubscribeEvent
    public void playerTickEvent(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        for (ExposureMinerals mineral : ExposureMinerals.values()) {
            boolean hasExposure = ExposureUtil.searchForExposer(player, mineral);
            MobEffectInstance effect = null;
            if (hasExposure && ExposureUtil.isEnabled(mineral)) {
                int timer = ExposureUtil.getExposureTimer(player, mineral);
                ExposureUtil.incrementExposureTimer(player, mineral);
                if (timer >= ExposureUtil.getStageOneMin(mineral) && timer < ExposureUtil.getStageTwoMin(mineral)) {
                    effect = ExposureUtil.getEffect(mineral, 1);
                } else if (timer >= ExposureUtil.getStageTwoMin(mineral) && timer < ExposureUtil.getStageThreeMin(mineral)) {
                    effect = ExposureUtil.getEffect(mineral, 2);
                } else if (timer >= ExposureUtil.getStageThreeMin(mineral) && timer < ExposureUtil.getStageFourMin(mineral)) {
                    effect = ExposureUtil.getEffect(mineral, 3);
                } else if (timer >= ExposureUtil.getStageFourMin(mineral)) {
                    effect = ExposureUtil.getEffect(mineral, 4);
                }
                if (effect != null) {
                    player.addEffect(effect);
                }
            }
        }
        int checkedEffects = 0;
        for (MobEffectInstance effect : player.getActiveEffects()) {
            MobEffect mobEffect = effect.getEffect();
            if (!(mobEffect instanceof ExposureEffect)) {
                checkedEffects++;
                continue;
            }
            if (checkedEffects >= player.getActiveEffects().size()) {
                player.getPersistentData().putBoolean("metallurgica:exposureEffect_showBlur", false);
                player.getPersistentData().putBoolean("metallurgica:exposureEffect_fatigue", false);
            }
        }
        //if (player instanceof ServerPlayer serverPlayer)
        //    MetallurgicaPackets.sendToPlayer(serverPlayer, /*new BlurShaderPacket(player.getPersistentData().getBoolean("metallurgica:exposureEffect_showBlur"))*/true);
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        MetallurgicaCommands.register(event.getDispatcher(), event.getBuildContext());
    }

    @SubscribeEvent
    public void onServerStart(ServerAboutToStartEvent event) {
        Metallurgica.LOGGER.info("Thanks for using Metallurgica! Expect a severe lack of ores in your world :3");
        TemperatureHandler.generateMap(event.getServer());
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        Metallurgica.LOGGER.info("Double checking our cool little ore frequency thingy :3");
    }

    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {

        @SubscribeEvent
        public static void addPackFinders(AddPackFindersEvent event) {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                // Clear old data
                MetallurgicaDynamicResourcePack.clearClient();

                event.addRepositorySource(new MetallurgicaPackSource("metallurgica:dynamic_assets",
                        event.getPackType(),
                        Pack.Position.BOTTOM,
                        MetallurgicaDynamicResourcePack::new));
            } else if (event.getPackType() == PackType.SERVER_DATA) {
                MetallurgicaDynamicDataPack.clearServer();

                long startTime = System.currentTimeMillis();
                MetallurgicaRecipes.recipeRemoval();
                MetallurgicaRecipes.recipeAddition(MetallurgicaDynamicDataPack::addRecipe);
                RuntimeCompositions.compositionAddition(MetallurgicaDynamicDataPack::addComposition);
                Metallurgica.LOGGER.info("Metallurgica Data loading took {}ms", System.currentTimeMillis() - startTime);
                event.addRepositorySource(new MetallurgicaPackSource("metallurgica:dynamic_data", event.getPackType(), Pack.Position.BOTTOM, MetallurgicaDynamicDataPack::new));
            }
        }
    }
}
