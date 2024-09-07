package pl.justpvp.bungee.auth;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.redis.channels.RedisChannel;
import pl.justpvp.bungee.util.GsonUtil;

import javax.jws.soap.SOAPBinding;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class BungeeUserManager {

    private HashMap<UUID, BungeeUser> authUsers = new HashMap<>();
    private Map<String, BungeeUser> authUsersCache = new HashMap<>();
    private BungeePlugin plugin;

    public BungeeUserManager(BungeePlugin plugin){
        this.plugin = plugin;
    }


    public BungeeUser createUserOnProxy(ProxiedPlayer player, String password, boolean premium){
        BungeeUser u = new BungeeUser(player.getUniqueId(),player.getDisplayName(),player.getAddress().getAddress().getHostAddress(),player.getAddress().getAddress().getHostAddress(),password,premium);

        authUsers.put(player.getUniqueId(),u);
        authUsersCache.put(player.getDisplayName(), u);


        return u;
    }
    public BungeeUser createUserOnProxy(UUID uuid, String name, String firstIP, String lastIP, String password, boolean premium){
        BungeeUser u = new BungeeUser(uuid,name,firstIP,lastIP,password,premium);

        authUsers.put(uuid,u);
        authUsersCache.put(name, u);

        u.insert();

        return u;
    }
    public BungeeUser createUser(ProxiedPlayer player, String password, boolean premium){
        BungeeUser u = createUserOnProxy(player,password,premium);

        u.insert();
        return u;
    }

    public BungeeUser getUserByIP(String ip){
        for (BungeeUser user :  getAuthUsers()){
            if(user.getFirstIP().equalsIgnoreCase(ip) || user.getLastIP().equalsIgnoreCase(ip)){
                return user;
            }
        }
        return null;
    }
    public BungeeUser getUser(UUID uuid){
        return authUsers.get(uuid);
    }

    public Collection<BungeeUser> getAuthUsers()
    {
        return authUsers.values();
    }

    public BungeeUser getUser(ProxiedPlayer proxiedPlayer){
        return authUsers.get(proxiedPlayer.getUniqueId());
    }

    public BungeeUser getUser(String name){
       return authUsersCache.get(name);
    }

    public void setup(){
        final AtomicInteger ai = new AtomicInteger(0);
        RedisChannel.INSTANCE.PROXY_USERS.forEach((uuid,s)->{
            final BungeeUser user = GsonUtil.fromJson(s,BungeeUser.class);

            authUsers.put(uuid,user);
            authUsersCache.put(user.getLastName(), user);

            ai.getAndIncrement();
        });
        BungeePlugin.INSTANCE.getLogger().log(Level.INFO, "ZALADOWALEM UZYTKOWNIKOW: " + ai.get());
    }
}
