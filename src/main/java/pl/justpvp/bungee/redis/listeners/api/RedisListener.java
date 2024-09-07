package pl.justpvp.bungee.redis.listeners.api;

import org.redisson.api.RTopic;
import pl.justpvp.bungee.redis.enums.ChannelType;

public abstract class RedisListener<T> {

    private final ChannelType type;
    private final RTopic rTopic;

    public RedisListener(ChannelType type, RTopic rTopic) {
        this.type = type;
        this.rTopic = rTopic;
    }

    public abstract void sendMessage(T message);

    public void sendMessage(final String sector, final T data) {

    }

    public ChannelType getType() {
        return type;
    }

    public RTopic getTopic() {
        return rTopic;
    }
}
