package dev.metallurgists.metallurgica.registry;

import dev.metallurgists.metallurgica.Metallurgica;
import dev.metallurgists.metallurgica.content.fluids.faucet.FaucetActivationPacket;
import dev.metallurgists.metallurgica.content.metalworking.forging.hammer.RadialHammerMenuSubmitPacket;
import dev.metallurgists.metallurgica.foundation.temperature.TemperatureUpdatePacket;
import dev.metallurgists.metallurgica.foundation.data.custom.composition.fluid.FluidCompositionPacket;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER;

public enum MetallurgicaPackets {
    // Client to Server
    radialHammerMenuSubmit(RadialHammerMenuSubmitPacket.class, RadialHammerMenuSubmitPacket::new, PLAY_TO_SERVER),
    
    // Server to Client
    faucetActivation(FaucetActivationPacket.class, FaucetActivationPacket::new, PLAY_TO_CLIENT),
    fluidComposition(FluidCompositionPacket.class, FluidCompositionPacket::new, PLAY_TO_CLIENT),
    temperatureUpdate(TemperatureUpdatePacket.class, TemperatureUpdatePacket::new, PLAY_TO_CLIENT),
    ;
    
    public static final ResourceLocation CHANNEL_NAME = Metallurgica.asResource("main");
    public static final int NETWORK_VERSION = 3;
    public static final String NETWORK_VERSION_STR = String.valueOf(NETWORK_VERSION);
    private static SimpleChannel channel;
    
    private PacketType<?> packetType;
    
    <T extends SimplePacketBase> MetallurgicaPackets(Class<T> type, Function<FriendlyByteBuf, T> factory,
                                            NetworkDirection direction) {
        packetType = new PacketType<>(type, factory, direction);
    }
    
    public static void registerPackets() {
        channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
                .serverAcceptedVersions(NETWORK_VERSION_STR::equals)
                .clientAcceptedVersions(NETWORK_VERSION_STR::equals)
                .networkProtocolVersion(() -> NETWORK_VERSION_STR)
                .simpleChannel();
        
        for (MetallurgicaPackets packet : values())
            packet.packetType.register();
    }
    
    public static SimpleChannel getChannel() {
        return channel;
    }
    
    public static void sendToNear(Level world, BlockPos pos, int range, Object message) {
        getChannel().send(PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), range, world.dimension())), message);
    }
    public static void sendToClientsNear(Object msg, @Nullable LevelAccessor world, BlockPos position) {
        if (world instanceof ServerLevel server) {
            sendToNear(server, position, 64, msg);
        }
    }
    public static void sendToClientsTracking(Object msg, @Nullable LevelAccessor world, BlockPos position) {
        if (world instanceof ServerLevel server) {
            getChannel().send(PacketDistributor.TRACKING_CHUNK.with(() -> server.getChunkAt(position)), msg);
        }
    }
    
    public static void sendToPlayer(ServerPlayer player, Object msg) {
        getChannel().send(PacketDistributor.PLAYER.with(() -> player), msg);
    }
    
    public static void sendToAll(Object msg) {
        getChannel().send(PacketDistributor.ALL.noArg(), msg);
    }
    
    private static class PacketType<T extends SimplePacketBase> {
        private static int index = 0;
        
        private BiConsumer<T, FriendlyByteBuf> encoder;
        private Function<FriendlyByteBuf, T> decoder;
        private BiConsumer<T, Supplier<NetworkEvent.Context>> handler;
        private Class<T> type;
        private NetworkDirection direction;
        
        private PacketType(Class<T> type, Function<FriendlyByteBuf, T> factory, NetworkDirection direction) {
            encoder = T::write;
            decoder = factory;
            handler = (packet, contextSupplier) -> {
                NetworkEvent.Context context = contextSupplier.get();
                if (packet.handle(context)) {
                    context.setPacketHandled(true);
                }
            };
            this.type = type;
            this.direction = direction;
        }
        
        private void register() {
            getChannel().messageBuilder(type, index++, direction)
                    .encoder(encoder)
                    .decoder(decoder)
                    .consumerNetworkThread(handler)
                    .add();
        }
    }
}
