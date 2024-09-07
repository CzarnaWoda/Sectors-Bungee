package pl.justpvp.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.redisson.api.RSet;
import pl.justpvp.bungee.auth.BungeeUserManager;
import pl.justpvp.bungee.auth.LoginManager;
import pl.justpvp.bungee.auth.MotdCommand;
import pl.justpvp.bungee.commands.*;
import pl.justpvp.bungee.configs.ProxiesConfig;
import pl.justpvp.bungee.configs.ProxyConfig;
import pl.justpvp.bungee.configs.SectorsConfig;
import pl.justpvp.bungee.listeners.*;
import pl.justpvp.bungee.managers.AllowBuildManager;
import pl.justpvp.bungee.managers.BanIPManager;
import pl.justpvp.bungee.managers.BanManager;
import pl.justpvp.bungee.managers.WhitelistManager;
import pl.justpvp.bungee.packets.impl.proxy.ProxyDisablePacket;
import pl.justpvp.bungee.packets.impl.proxy.ProxyEnablePacket;
import pl.justpvp.bungee.proxies.Proxy;
import pl.justpvp.bungee.proxies.ProxyManager;
import pl.justpvp.bungee.redis.RedisManager;
import pl.justpvp.bungee.redis.channels.RedisChannel;
import pl.justpvp.bungee.redis.client.RedisClient;
import pl.justpvp.bungee.redis.enums.ChannelType;
import pl.justpvp.bungee.redis.factory.RedisFactory;
import pl.justpvp.bungee.redis.listeners.GlobalPacketListener;
import pl.justpvp.bungee.redis.listeners.ProxiesPacketListener;
import pl.justpvp.bungee.redis.listeners.api.RedisListener;
import pl.justpvp.bungee.thread.ProxyStatusTask;
import pl.justpvp.bungee.thread.api.ScheduledTask;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class BungeePlugin extends Plugin {

    public static BungeePlugin INSTANCE;
    public static BungeeUserManager bungeeUserManager;
    public static LoginManager loginManager;
    public static int ONLINE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        ProxyConfig.saveDefaultConfig();
        new ProxyConfig();
        RedisManager.setup();
        RedisChannel.INSTANCE.setupChannels();
        ProxiesConfig.saveDefaultConfig();
        new ProxiesConfig();
        SectorsConfig.saveDefaultConfig();
        new SectorsConfig();
        final Set<RedisListener> redisListeners = new HashSet<>(Arrays.asList(
                new GlobalPacketListener(ChannelType.GLOBAL_PACKETS, RedisChannel.INSTANCE.globalPacketTopic),
                new ProxiesPacketListener<>(ChannelType.PACKET_TO_PROXIES, RedisChannel.INSTANCE.proxiesPacketTopic)
        ));
        for (RedisListener redisListener : redisListeners) {
            RedisManager.registerListener(redisListener);
        }
        bungeeUserManager = new BungeeUserManager(this);
        loginManager = new LoginManager(this);
        getBungeeUserManager().setup();

        final Proxy proxy = ProxyManager.getCurrentProxy();

        proxy.getRedisOnlinePlayers().clear();

        for(Proxy proxies : ProxyManager.getProxies().values()){
            /*RSet<String> redisList = proxies.getRedisOnlinePlayers();
            for(String name : redisList){
                proxies.addOnlinePlayer(name);
            }*/
            getLogger().log(Level.INFO, "Proxy " + proxies.getProxyName() + " zaladowalo " + proxies.getRedisOnlinePlayers().size() + " graczy online!");
        }
        final ProxyEnablePacket packet = new ProxyEnablePacket(ProxyConfig.getCURRENTPROXY_NAME());
        RedisClient.sendProxiesPacket(packet);
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        final Set<ScheduledTask> tasks = new HashSet<>(Arrays.asList(new ProxyStatusTask(executorService)));
        for (ScheduledTask task : tasks) {
            task.runTask();
        }
        List<Listener> listeners = Arrays.asList(new BlazingPackAuthListener(),new ConnectEvent(),new DisconnectEvent(),new PingEvent(), new TabCompleteListener());

        for(Listener listener : listeners){
            getProxy().getPluginManager().registerListener(this, listener);
        }

        this.getProxy().getPluginManager().registerCommand(this, new RegisterCommand(this));
        this.getProxy().getPluginManager().registerCommand(this, new LoginCommand());
        this.getProxy().getPluginManager().registerCommand(this, new BanCommand());
        this.getProxy().getPluginManager().registerCommand(this, new TempBanCommand());
        this.getProxy().getPluginManager().registerCommand(this, new UnBanCommand());
        this.getProxy().getPluginManager().registerCommand(this, new BanIPCommand());
        this.getProxy().getPluginManager().registerCommand(this, new UnBanIPCommand());
        this.getProxy().getPluginManager().registerCommand(this, new BanInfoCommand());
        this.getProxy().getPluginManager().registerCommand(this, new MotdCommand());
        this.getProxy().getPluginManager().registerCommand(this, new WhitelistCommand());
        this.getProxy().getPluginManager().registerCommand(this, new BuildAddCommand());

        executorService.scheduleWithFixedDelay(() -> {
            getLoginManager().setCurrentJoins(0);
        },5 ,5, TimeUnit.SECONDS);
        executorService.scheduleWithFixedDelay(() -> {
            int online = 0;
            for(Proxy proxies : ProxyManager.getProxies().values()){
                online = online + proxies.getRedisOnlinePlayers().size();
            }
            ONLINE = online;
        },1,3, TimeUnit.SECONDS);

        BanManager.setup();
        BanIPManager.setup();
        WhitelistManager.setup();
        AllowBuildManager.setup();
    }

    @Override
    public void onDisable() {
        final Proxy proxy = ProxyManager.getCurrentProxy();
        proxy.getRedisOnlinePlayers().clear();

        for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
        {
            RedisChannel.INSTANCE.ONLINE_PLAYERS.remove(player.getName());
        }

        final ProxyDisablePacket packet = new ProxyDisablePacket(ProxyConfig.getCURRENTPROXY_NAME());
        RedisClient.sendProxiesPacket(packet);
    }

    public static BungeeUserManager getBungeeUserManager() {
        return bungeeUserManager;
    }

    public static LoginManager getLoginManager() {
        return loginManager;
    }
}
