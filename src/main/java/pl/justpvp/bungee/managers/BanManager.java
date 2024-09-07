package pl.justpvp.bungee.managers;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.auth.LoginManager;
import pl.justpvp.bungee.data.Ban;
import pl.justpvp.bungee.packets.bans.CreateBanPacket;
import pl.justpvp.bungee.packets.bans.DeleteBanPacket;
import pl.justpvp.bungee.packets.bans.UnBanPacket;
import pl.justpvp.bungee.redis.channels.RedisChannel;
import pl.justpvp.bungee.redis.client.RedisClient;
import pl.justpvp.bungee.util.GsonUtil;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class BanManager {

    private static HashMap<UUID, Ban> bans =  new HashMap<>();

    public static void createBan(final UUID uuid, final String reason, final String admin, final long expireTime){
        final Ban ban = new Ban(uuid,reason,admin,System.currentTimeMillis(),expireTime);

        final CreateBanPacket packet = new CreateBanPacket(ban.getUuid(),ban.getReason(),ban.getAdmin(),ban.getCreateTime(),ban.getExpireTime(),ban.isUnban());
        RedisClient.sendProxiesPacket(packet);
    }

    public static Ban getBan(UUID uuid){
        final Ban ban = getBans().get(uuid);

        return ban;
    }

    public static void deleteBan(final Ban ban){
        ban.delete();

        final DeleteBanPacket packet = new DeleteBanPacket(ban.getUuid());
        RedisClient.sendProxiesPacket(packet);

    }
    public static void setup(){
        RedisChannel.INSTANCE.BANS.forEach(((uuid, s) -> {
            Ban ban = GsonUtil.fromJson(s,Ban.class);
            bans.put(uuid,ban);
        }));
        BungeePlugin.INSTANCE.getLogger().log(Level.INFO, "Loaded " + bans.size() + " bans!");
    }


    public static HashMap<UUID, Ban> getBans() {
        return bans;
    }

}
