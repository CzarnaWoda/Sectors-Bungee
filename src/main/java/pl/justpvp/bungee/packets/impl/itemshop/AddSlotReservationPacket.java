package pl.justpvp.bungee.packets.impl.itemshop;

import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;

public class AddSlotReservationPacket extends RedisPacket {

    private String userName;

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getUserName() {
        return userName;
    }

}
