package pl.justpvp.bungee.packets.chat;

import lombok.Data;
import lombok.NonNull;
import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;

@Data
public class GlobalChatMessage extends RedisPacket {
    @NonNull private String message;

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
