package pl.justpvp.bungee.packets.impl.proxy;

import lombok.Getter;
import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;


@Getter
public class ProxyDisablePacket extends RedisPacket {

    private String proxyName;

    public ProxyDisablePacket(){
    }
    public ProxyDisablePacket(String proxyName){
        this.proxyName = proxyName;
    }
    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
