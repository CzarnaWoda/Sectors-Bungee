package pl.justpvp.bungee.packets.manager;

import pl.justpvp.bungee.packets.RedisPacket;
import pl.justpvp.bungee.packets.bans.*;
import pl.justpvp.bungee.packets.chat.GlobalChatMessage;
import pl.justpvp.bungee.packets.impl.allowedbuild.AllowBuildPacket;
import pl.justpvp.bungee.packets.impl.bans.BanAddPacket;
import pl.justpvp.bungee.packets.impl.configs.ConfigPacket;
import pl.justpvp.bungee.packets.impl.itemshop.AddSlotReservationPacket;
import pl.justpvp.bungee.packets.impl.proxy.ProxyDisablePacket;
import pl.justpvp.bungee.packets.impl.proxy.ProxyEnablePacket;
import pl.justpvp.bungee.packets.impl.proxy.ProxyStatusPacket;
import pl.justpvp.bungee.packets.impl.sectors.SectorStatusPacket;
import pl.justpvp.bungee.packets.impl.user.*;
import pl.justpvp.bungee.packets.whitelist.WhitelistAddPacket;
import pl.justpvp.bungee.packets.whitelist.WhitelistDisablePacket;
import pl.justpvp.bungee.packets.whitelist.WhitelistEnablePacket;
import pl.justpvp.bungee.packets.whitelist.WhitelistRemovePacket;

import java.util.HashMap;
import java.util.Map;

public class PacketManager {

    private static final Map<Integer, Class<? extends RedisPacket>> packetsByID = new HashMap<>();
    private static final Map<Class<? extends RedisPacket>, Integer> packetsByClass = new HashMap<>();



    public static void registerPacket(int id, Class<? extends RedisPacket> packetClass) {
        packetsByID.putIfAbsent(id, packetClass);
        packetsByClass.putIfAbsent(packetClass, id);
    }
    static {
        registerPacket(801, ProxyDisablePacket.class);
        registerPacket(802, ProxyEnablePacket.class);
        registerPacket(803, ProxyStatusPacket.class);
        registerPacket(511, SectorStatusPacket.class);
        registerPacket(805, ProxyJoinPacket.class);
        registerPacket(806, ProxyLeavePacket.class);
        registerPacket(807, ProxyUserRegisterPacket.class);
        registerPacket(808, UserChangeSectorPacket.class);
        registerPacket(809, CreateBanPacket.class);
        registerPacket(810, DeleteBanPacket.class);
        registerPacket(811, UnBanPacket.class);
        registerPacket(812, GlobalChatMessage.class);
        registerPacket(813, BungeeUserUpdatePacket.class);
        registerPacket(814, CreateIPBanPacket.class);
        registerPacket(815, DeleteIPBanPacket.class);
        registerPacket(816, ConfigPacket.class);

        //1500+ from spigot to bungee
        registerPacket(1500, AddSlotReservationPacket.class);
        registerPacket(1501, BanAddPacket.class);

        //whitelist 1600+
        registerPacket(1600, WhitelistAddPacket.class);
        registerPacket(1601, WhitelistRemovePacket.class);
        registerPacket(1602, WhitelistEnablePacket.class);
        registerPacket(1603, WhitelistDisablePacket.class);

        //allow build 1603+
        registerPacket(1604, AllowBuildPacket.class);
    }

    public static Class<? extends RedisPacket> getPacketClass(final int id){
        return packetsByID.get(id);
    }

    public static RedisPacket getPacket(int id) {
        Class<? extends RedisPacket> packet = packetsByID.get(id);
        if(packet == null){
            return null;
        }
        try {
            return packetsByID.get(id).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getPacketID(Class<? extends RedisPacket> clz) {
        return packetsByClass.get(clz);
    }

}
