package pl.justpvp.bungee.proxies;

import lombok.Getter;
import org.redisson.api.RSet;
import pl.justpvp.bungee.redis.RedisManager;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Proxy {

    private final String proxyName;
   // private final Set<String> onlinePlayers;
    //private final Set<String> onlinePlayerWithCases;
    private final RSet<String> redisOnlinePlayers;
    private Long lastUpdate;

    public Proxy(String proxyName){
        this.proxyName = proxyName;
       // this.onlinePlayers = new HashSet<>();
        //this.onlinePlayerWithCases = new HashSet<>();
        this.redisOnlinePlayers = RedisManager.getRedisConnection().getSet(proxyName + "-online");
        this.lastUpdate = -1L;
    }

   /* public void addOnlinePlayer(String name){
        onlinePlayers.add(name.toLowerCase());
        onlinePlayerWithCases.add(name);
    }*/

    /*
    public void removeOnlinePlayer(String name){
        onlinePlayers.remove(name.toLowerCase());
        onlinePlayerWithCases.remove(name);
    }*/

    public void addOnlinePlayerToRedis(String name){
        redisOnlinePlayers.addAsync(name);
    }

    public void removeOnlinePlayerToRedis(String name){
        redisOnlinePlayers.removeAsync(name);
    }

    public void clearOnlinePlayer(){
        //onlinePlayers.clear();
        //onlinePlayerWithCases.clear();
    }


    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
