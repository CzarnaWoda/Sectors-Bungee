package pl.justpvp.bungee.packets.bans;

import lombok.Data;
import lombok.NonNull;
import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;

@Data
public class DeleteIPBanPacket extends RedisPacket {
    @NonNull private String ip;
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
