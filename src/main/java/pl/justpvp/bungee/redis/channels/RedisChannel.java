package pl.justpvp.bungee.redis.channels;

import org.redisson.api.*;
import pl.justpvp.bungee.redis.RedisManager;

import java.util.UUID;

public class RedisChannel {

    public static RedisChannel INSTANCE = new RedisChannel();
    public RTopic globalPacketTopic;
    public RTopic proxiesPacketTopic;

    public RMap<UUID,String> BANS;
    public RMap<UUID,String> PROXY_USERS;
    public RMap<String, String> IP_BANS;
    public RSet<String> WHITELISTED;
    public RBitSet WHITELIST_ENABLED;
    public RSet<String> ALLOWED_BUILD;
    public RSet<String> ONLINE_PLAYERS;

    public void setupChannels()
    {
        globalPacketTopic = RedisManager.getRedisConnection().getTopic("globalPacketTopic");
        proxiesPacketTopic = RedisManager.getRedisConnection().getTopic("proxiesPacketTopic");

        PROXY_USERS = RedisManager.getRedisConnection().getMap("PROXY_USERS");
        BANS = RedisManager.getRedisConnection().getMap("BANS");
        IP_BANS =  RedisManager.getRedisConnection().getMap("IP_BANS");
        WHITELISTED = RedisManager.getRedisConnection().getSet("WHITELISTED_PROXY");
        WHITELIST_ENABLED = RedisManager.getRedisConnection().getBitSet("WHITELIST_ENABLED");
        ALLOWED_BUILD = RedisManager.getRedisConnection().getSet("ALLOWED_BUILD");
        ONLINE_PLAYERS = RedisManager.getRedisConnection().getSet("ONLINE_PLAYERS");
    }

    private RedisChannel() { }

}
