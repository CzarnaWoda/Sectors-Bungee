package pl.justpvp.bungee.packets;

import pl.justpvp.bungee.packets.handler.PacketHandler;

public abstract class RedisPacket {

    public abstract void handlePacket(final PacketHandler handler);

}
