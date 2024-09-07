package pl.justpvp.bungee.packets.whitelist;

import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;

public class WhitelistRemovePacket extends RedisPacket {

    private String userName;

    public WhitelistRemovePacket(String userName) {
        this.userName = userName;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getUserName() {
        return userName;
    }
}
