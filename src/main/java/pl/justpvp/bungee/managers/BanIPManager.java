package pl.justpvp.bungee.managers;

import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.data.BanIP;
import pl.justpvp.bungee.packets.bans.CreateIPBanPacket;
import pl.justpvp.bungee.packets.bans.DeleteIPBanPacket;
import pl.justpvp.bungee.redis.channels.RedisChannel;
import pl.justpvp.bungee.redis.client.RedisClient;
import pl.justpvp.bungee.util.GsonUtil;

import java.util.HashMap;
import java.util.logging.Level;

public class BanIPManager {
    private static HashMap<String, BanIP> ipbans =  new HashMap<>();

    public static void createBan(final String ip, final String reason, final String admin, final long expireTime){
        final BanIP banIP = new BanIP(ip,reason,admin,expireTime);

        final CreateIPBanPacket packet = new CreateIPBanPacket(banIP.getIp(),banIP.getReason(),banIP.getAdmin(),banIP.getCreateTime(),banIP.getExpireTime(),banIP.isUnban());
        RedisClient.sendProxiesPacket(packet);
    }

    public static BanIP getBan(String ip){
        final BanIP banIP = getIpbans().get(ip);

        return banIP;
    }
    public static void deleteBan(BanIP banIP){
        final DeleteIPBanPacket deleteIPBanPacket = new DeleteIPBanPacket(banIP.getIp());
        RedisClient.sendProxiesPacket(deleteIPBanPacket);

        banIP.delete();
    }

    public static void setup(){
        RedisChannel.INSTANCE.IP_BANS.forEach((ip, s) -> {
            BanIP banIP = GsonUtil.fromJson(s, BanIP.class);

            ipbans.put(ip,banIP);
        });
        BungeePlugin.INSTANCE.getLogger().log(Level.INFO, "Loaded " + ipbans.size() + " IP-bans!");
    }
    public static HashMap<String, BanIP> getIpbans() {
        return ipbans;
    }
}
