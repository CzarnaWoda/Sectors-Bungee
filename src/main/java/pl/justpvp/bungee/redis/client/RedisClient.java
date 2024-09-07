package pl.justpvp.bungee.redis.client;


import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.manager.PacketManager;
import pl.justpvp.bungee.redis.RedisManager;
import pl.justpvp.bungee.redis.enums.ChannelType;
import pl.justpvp.bungee.util.GsonUtil;

public class RedisClient {


    public static void sendGlobalPacket(final RedisPacket packet)
    {
        final int packetID = PacketManager.getPacketID(packet.getClass());
        RedisManager.getRedisListener(ChannelType.GLOBAL_PACKETS).sendMessage(packetID + "@" + GsonUtil.toJson(packet));
    }


    public static void sendSectorsPacket(final RedisPacket packet)
    {
        final int packetID = PacketManager.getPacketID(packet.getClass());
        RedisManager.getRedisListener(ChannelType.PACKET_TO_SPIGOTS).sendMessage(packetID + "@" + GsonUtil.toJson(packet));
    }

    public static void sendProxiesPacket(final RedisPacket packet)
    {
        final int packetID = PacketManager.getPacketID(packet.getClass());
        RedisManager.getRedisListener(ChannelType.PACKET_TO_PROXIES).sendMessage(packetID + "@" + GsonUtil.toJson(packet));
    }

    public static void sendPacket(final RedisPacket packet, final String serverName)
    {
        final int packetID = PacketManager.getPacketID(packet.getClass());
        RedisManager.getRedisListener(ChannelType.PACKET_TO_SINGLE_SECTOR).sendMessage(serverName + "#" + packetID + "@" + GsonUtil.toJson(packet));
    }


    public static void sendUser(RedisPacket packet, String sectorName)
    {
        System.out.println("Sending user to sector " + sectorName);
        RedisManager.getRedisListener(ChannelType.USER).sendMessage(sectorName, packet);
    }



}
