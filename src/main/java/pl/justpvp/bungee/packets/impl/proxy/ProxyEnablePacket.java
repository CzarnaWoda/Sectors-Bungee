package pl.justpvp.bungee.packets.impl.proxy;


import lombok.Getter;
import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;

@Getter
public class ProxyEnablePacket extends RedisPacket {

    private String proxyName;

    public ProxyEnablePacket(){}


    public ProxyEnablePacket(String proxyName){
        this.proxyName = proxyName;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
