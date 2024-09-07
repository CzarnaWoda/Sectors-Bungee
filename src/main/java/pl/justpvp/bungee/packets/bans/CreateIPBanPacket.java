package pl.justpvp.bungee.packets.bans;

import lombok.Data;
import lombok.NonNull;
import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;

@Data
public class CreateIPBanPacket extends RedisPacket {

    @NonNull private String reason,admin,ip;
    @NonNull private long createTime, expireTime;
    @NonNull private boolean unban;

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
