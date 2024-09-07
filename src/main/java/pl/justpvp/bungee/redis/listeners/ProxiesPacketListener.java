package pl.justpvp.bungee.redis.listeners;

import org.redisson.api.RTopic;
import org.redisson.api.listener.MessageListener;
import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;
import pl.justpvp.bungee.packets.handler.PacketHandlerImpl;
import pl.justpvp.bungee.packets.manager.PacketManager;
import pl.justpvp.bungee.redis.enums.ChannelType;
import pl.justpvp.bungee.redis.listeners.api.RedisListener;
import pl.justpvp.bungee.util.GsonUtil;

public class ProxiesPacketListener<T extends String> extends RedisListener<T> {


    final PacketHandler packetHandler = new PacketHandlerImpl();

    public ProxiesPacketListener(ChannelType type, RTopic rTopic) {
        super(type, rTopic);
        getTopic().addListener(String.class, new MessageListener<String>() {
            @Override
            public void onMessage(CharSequence charSequence, String packet) {
                final String[] split = packet.split("@");
                Class<? extends RedisPacket> clzPacket = PacketManager.getPacketClass(Integer.parseInt(split[0]));
                if (clzPacket == null) return;
                try {
                    final RedisPacket p = GsonUtil.fromJson(split[1], clzPacket);
                    p.handlePacket(packetHandler);
                }catch (Exception e)
                {
                    throw new RuntimeException("Wystapil blad podczas czytania pakietu o ID: " + split[0] + " exception: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void sendMessage(T message) {
        getTopic().publish(message);
    }
}
