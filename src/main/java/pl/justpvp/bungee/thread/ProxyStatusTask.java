package pl.justpvp.bungee.thread;

import pl.justpvp.bungee.configs.ProxyConfig;
import pl.justpvp.bungee.packets.impl.proxy.ProxyStatusPacket;
import pl.justpvp.bungee.redis.client.RedisClient;
import pl.justpvp.bungee.thread.api.ScheduledTask;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProxyStatusTask extends ScheduledTask {


    public ProxyStatusTask(ScheduledExecutorService executorService) {
        super(executorService);
    }

    @Override
    public void runTask() {
        getExecutorService().scheduleAtFixedRate(() -> {
            RedisClient.sendGlobalPacket(new ProxyStatusPacket(ProxyConfig.getCURRENTPROXY_NAME()));
        }, 1, 3, TimeUnit.SECONDS);
    }
}
