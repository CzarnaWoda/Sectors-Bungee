package pl.justpvp.bungee.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.configs.ProxyConfig;
import pl.justpvp.bungee.packets.impl.user.ProxyLeavePacket;
import pl.justpvp.bungee.proxies.ProxyManager;
import pl.justpvp.bungee.redis.channels.RedisChannel;
import pl.justpvp.bungee.redis.client.RedisClient;

public class DisconnectEvent implements Listener {


    @EventHandler
    public void onDisconnect(final PlayerDisconnectEvent event)
    {
        final ProxiedPlayer player = event.getPlayer();
        final String name = player.getName();
        RedisChannel.INSTANCE.ONLINE_PLAYERS.remove(name);
        ProxyManager.getCurrentProxy().removeOnlinePlayerToRedis(event.getPlayer().getDisplayName());
        BungeePlugin.getLoginManager().getLogged().remove(event.getPlayer().getDisplayName());
        /*final ProxyLeavePacket packet = new ProxyLeavePacket(event.getPlayer().getDisplayName(), ProxyConfig.getCURRENTPROXY_NAME());
        RedisClient.sendProxiesPacket(packet);*/
    }

}
