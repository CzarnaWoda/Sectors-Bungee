package pl.justpvp.bungee.packets.impl.allowedbuild;

import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;

public class AllowBuildPacket extends RedisPacket {

    private String nickName;
    private boolean isAdding;

    public AllowBuildPacket(String nickName, boolean isAdding) {
        this.nickName = nickName;
        this.isAdding = isAdding;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getNickName() {
        return nickName;
    }

    public boolean isAdding() {
        return isAdding;
    }
}
