package pl.justpvp.bungee.packets.impl.user;

import lombok.Data;
import lombok.NonNull;
import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;

import java.util.UUID;

@Data
public class BungeeUserUpdatePacket extends RedisPacket {

    @NonNull private UUID uuid;
    @NonNull private String lastIP;
    @NonNull private String lastName;


    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
