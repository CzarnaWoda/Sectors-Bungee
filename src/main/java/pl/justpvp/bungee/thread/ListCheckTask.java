package pl.justpvp.bungee.thread;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.justpvp.bungee.configs.ProxyConfig;
import pl.justpvp.bungee.packets.impl.proxy.ProxyStatusPacket;
import pl.justpvp.bungee.proxies.Proxy;
import pl.justpvp.bungee.proxies.ProxyManager;
import pl.justpvp.bungee.redis.client.RedisClient;
import pl.justpvp.bungee.thread.api.ScheduledTask;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ListCheckTask extends ScheduledTask {


    public ListCheckTask(ScheduledExecutorService executorService) {
        super(executorService);
    }

    @Override
    public void runTask() {
        getExecutorService().scheduleAtFixedRate(() -> {
            final Proxy proxy = ProxyManager.getCurrentProxy();
            for(String name : proxy.getRedisOnlinePlayers())
            {
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);
                if(player != null && player.isConnected())
                {
                    continue;
                }

            }
        }, 1, 10, TimeUnit.SECONDS);
    }
}

