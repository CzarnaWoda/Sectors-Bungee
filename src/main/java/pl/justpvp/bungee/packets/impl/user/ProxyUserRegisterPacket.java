package pl.justpvp.bungee.packets.impl.user;


import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.handler.PacketHandler;

import java.util.UUID;

@Getter
public class ProxyUserRegisterPacket extends RedisPacket {

    private UUID uuid;
    private String  lastName, firstIP, lastIP, password;
    private boolean premium;

    public ProxyUserRegisterPacket(){}


    public ProxyUserRegisterPacket(UUID uuid , String lastName, String firstIP, String lastIP, String password, boolean premium){

        this.uuid = uuid;
        this.lastName = lastName;
        this.firstIP = firstIP;
        this.lastIP = lastIP;
        this.password = password;
        this.premium = premium;

    }
    public ProxyUserRegisterPacket(ProxiedPlayer player, String password, boolean premium){

        this.uuid = player.getUniqueId();
        this.lastName = player.getDisplayName();
        this.firstIP = player.getAddress().getAddress().getHostAddress();
        this.lastIP = player.getAddress().getAddress().getHostAddress();
        this.password = password;
        this.premium = premium;

    }

    @Override
    public void handlePacket(PacketHandler handler) {
        handler.handle(this);
    }
}
