package pl.justpvp.bungee.packets.impl.user;

import lombok.Data;
import lombok.NonNull;
import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;


@Data
public class ProxyLeavePacket extends RedisPacket {

    @NonNull private String playerName,proxyName;

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
