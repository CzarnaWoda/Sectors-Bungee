package pl.justpvp.bungee.redis.factory;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import pl.justpvp.bungee.configs.ProxyConfig;

public class RedisFactory {

    public static RedissonClient createRedisConnection()
    {
        org.redisson.config.Config config = new org.redisson.config.Config();
        config.useSingleServer()
                .setAddress("redis://" + ProxyConfig.REDIS_ADDRESS + ":6379")
                .setPassword(ProxyConfig.REDIS_PASSWORD);
        return Redisson.create(config);
    }

}
