package pl.justpvp.bungee.packets.impl.bans;

import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;

import java.util.UUID;

public class BanAddPacket extends RedisPacket {

    private UUID uuid;
    private String reason;
    private String admin;
    private Long expireTime;

    public BanAddPacket(UUID uuid, String reason, String admin, Long expireTime) {
        this.uuid = uuid;
        this.reason = reason;
        this.admin = admin;
        this.expireTime = expireTime;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getReason() {
        return reason;
    }

    public String getAdmin() {
        return admin;
    }

    public Long getExpireTime() {
        return expireTime;
    }
}
