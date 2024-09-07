package pl.justpvp.bungee.packets.bans;

import lombok.Data;
import lombok.NonNull;
import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;

import java.util.UUID;

@Data
public class DeleteBanPacket extends RedisPacket {

    @NonNull private UUID uuid;

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
