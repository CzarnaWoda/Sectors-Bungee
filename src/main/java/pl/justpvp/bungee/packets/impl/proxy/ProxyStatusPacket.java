package pl.justpvp.bungee.packets.impl.proxy;

import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;

public class ProxyStatusPacket extends RedisPacket {

    private String proxyName;


    public ProxyStatusPacket(String proxyName) {
        this.proxyName = proxyName;
    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }

    public String getProxyName() {
        return proxyName;
    }
}
