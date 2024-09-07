package pl.justpvp.bungee.packets.whitelist;

import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;

public class WhitelistDisablePacket extends RedisPacket {
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
