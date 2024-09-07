package pl.justpvp.bungee.redis;

import net.md_5.bungee.BungeeCord;
import org.redisson.api.RedissonClient;
import pl.justpvp.bungee.redis.enums.ChannelType;
import pl.justpvp.bungee.redis.factory.RedisFactory;
import pl.justpvp.bungee.redis.listeners.api.RedisListener;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class RedisManager {

    private final static Map<ChannelType, RedisListener> topics = new HashMap<>();
    private static RedissonClient redisConnection;

    public static void setup()
    {
        redisConnection = RedisFactory.createRedisConnection();
    }

    public static RedisListener getRedisListener(final ChannelType type)
    {
        return topics.get(type);
    }

    public static void registerListener(final RedisListener listener)
    {
        topics.put(listener.getType(), listener);
        BungeeCord.getInstance().getLogger().log(Level.INFO, "Registered redis listener for " + listener.getType().name());
    }


    public static RedissonClient getRedisConnection() {
        return redisConnection;
    }
}
