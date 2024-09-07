package pl.justpvp.bungee.packets.impl.configs;

import lombok.Data;
import lombok.NonNull;
import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;

@Data
public class ConfigPacket extends RedisPacket {
    @NonNull private String path;
    @NonNull private Object value;


    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
