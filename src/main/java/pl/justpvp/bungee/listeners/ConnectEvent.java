package pl.justpvp.bungee.listeners;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.md_5.bungee.protocol.packet.Chat;
import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.auth.BungeeUser;
import pl.justpvp.bungee.auth.LoginManager;
import pl.justpvp.bungee.configs.ProxyConfig;
import pl.justpvp.bungee.data.Ban;
import pl.justpvp.bungee.data.BanIP;
import pl.justpvp.bungee.managers.AllowBuildManager;
import pl.justpvp.bungee.managers.BanIPManager;
import pl.justpvp.bungee.managers.BanManager;
import pl.justpvp.bungee.managers.WhitelistManager;
import pl.justpvp.bungee.packets.bans.DeleteBanPacket;
import pl.justpvp.bungee.packets.bans.UnBanPacket;
import pl.justpvp.bungee.packets.impl.user.*;
import pl.justpvp.bungee.proxies.Proxy;
import pl.justpvp.bungee.proxies.ProxyManager;
import pl.justpvp.bungee.redis.channels.RedisChannel;
import pl.justpvp.bungee.redis.client.RedisClient;
import pl.justpvp.bungee.util.ChatUtil;
import pl.justpvp.bungee.util.Util;

import java.nio.charset.StandardCharsets;

public class ConnectEvent implements Listener,Colors {




    @EventHandler(priority = EventPriority.HIGHEST)
    public void preJoin(final PreLoginEvent event)
    {
        if(event.isCancelled())
        {
            return;
        }
        final PendingConnection connection = event.getConnection();
        if(connection == null)
        {
            return;
        }
        if(WhitelistManager.isEnabled() && !WhitelistManager.isWhitelisted(connection.getName()))
        {
            event.setCancelReason(new TextComponent(ChatUtil.fixColor("&cNie jestes na whiteliscie!")));
            event.setCancelled(true);
            return;
        }
        final BungeeUser user = BungeePlugin.getBungeeUserManager().getUser(connection.getName());
        if(user != null) {
            final Ban ban = BanManager.getBan(user.getUuid());
            if (ban != null) {
                if (ban.isAlive()) {
                    final DeleteBanPacket packet = new DeleteBanPacket(ban.getUuid());
                    RedisClient.sendProxiesPacket(packet);

                } else {
                    String cancelReason = ChatUtil.fixColor("       &8->> &c&lJUSTPVP.PL &8<<-       " + "\n \n" + WarningColor + "       Zostales zbanowany\n" + SpecialSigns + "» " + MainColor + "Dnia: " + ImportantColor + Util.getDate(ban.getCreateTime()) + ((ban.getExpireTime() > 0L) ? "\n" + SpecialSigns + "» " + MainColor + "Do: " + ImportantColor + Util.getDate(ban.getExpireTime()) : "\n " + SpecialSigns + "» Do: " + ImportantColor + "na zawsze") + "\n" + SpecialSigns + "» " + MainColor + "Przez: " + ImportantColor + ban.getAdmin() + "\n" + SpecialSigns + "» " + MainColor + "Powod: " + ImportantColor + UnderLined + ban.getReason() + "\n\n" + SpecialSigns + "» " + MainColor + "Kontakt TeamSpeak3:" + ImportantColor + "justpvp.pl");

                    event.setCancelReason(new TextComponent(cancelReason));
                    event.setCancelled(true);
                    return;
                }
            }
        }
        final BanIP banIP = BanIPManager.getBan(event.getConnection().getAddress().getAddress().getHostAddress());
        if(banIP != null){
            if(!banIP.isUnban()){
                if(banIP.getExpireTime() >= System.currentTimeMillis()){
                    BanIPManager.deleteBan(banIP);
                }else{
                    String cancelReason = ChatUtil.fixColor("       &8->> &c&lJUSTPVP.PL &8<<-       " + "\n \n" + WarningColor + "       Zostales zbanowany\n" + SpecialSigns + "» " + MainColor + "Dnia: " + ImportantColor + Util.getDate(banIP.getCreateTime()) + ((banIP.getExpireTime() > 0L) ? "\n" + SpecialSigns + "» " + MainColor + "Do: " + ImportantColor + Util.getDate(banIP.getExpireTime()) : "\n " + SpecialSigns + "» Do: " + ImportantColor + "na zawsze") + "\n" + SpecialSigns + "» " + MainColor + "Przez: " + ImportantColor + banIP.getAdmin() + "\n" + SpecialSigns + "» " + MainColor + "Powod: " + ImportantColor + UnderLined + banIP.getReason() + "\n\n" + SpecialSigns + "» " + MainColor + "Kontakt TeamSpeak3:" + ImportantColor + "justpvp.pl");

                    event.setCancelReason(new TextComponent(cancelReason));
                    event.setCancelled(true);
                    return;
                }
            }
        }
        final String name = connection.getName();

        for(String pName : RedisChannel.INSTANCE.ONLINE_PLAYERS){
            if (pName.equalsIgnoreCase(name)) {
                event.setCancelReason(new TextComponent(ChatUtil.fixColor("&cTaki nick juz jest na serwerze!")));
                event.setCancelled(true);
                return;
            }
        }

        /*if(RedisChannel.INSTANCE.ONLINE_PLAYERS.contains(name.toLowerCase()))
        {
            event.setCancelReason(new TextComponent(ChatUtil.fixColor("&cTaki nick juz jest na serwerze!")));
            event.setCancelled(true);
            return;
        }*/
        /*for(Proxy proxy : ProxyManager.getProxies().values()){
            if(proxy.getOnlinePlayers().contains(name.toLowerCase())){
                event.setCancelReason(new TextComponent(ChatUtil.fixColor("&cTaki nick juz jest na serwerze!")));
                event.setCancelled(true);
                return;
            }
        }*/
        if(BungeePlugin.getLoginManager().getCurrentJoins() >= 9){
            event.setCancelReason(new TextComponent(ChatUtil.fixColor("&cZbyt wiele osob chce sie zalogowac!")));
            event.setCancelled(true);
            return;
        }
        BungeePlugin.getLoginManager().registerIntent(event, event.getConnection().getName());
        //System.out.println(event.getConnection().getName() + ", premium" + (event.getConnection().isOnlineMode() ? 1 : 0));
        /*
        final Ban ban = this.plugin.getBanManager().getBan(connection.getName());
        if(ban == null)
        {
            return;
        }
        if(System.currentTimeMillis() > ban.endTime())
        {
            this.plugin.getBanManager().removeBan(ban);
            return;
        }
        event.setCancelReason(ban.getReason());
        event.setCancelled(true);

         */

    }

    @EventHandler
    public void onChangeServer(final ServerConnectEvent e){
        final ProxiedPlayer player = e.getPlayer();
        final BungeeUser user = BungeePlugin.getBungeeUserManager().getUser(player.getUniqueId());
        player.sendData("BP|ShowPingOnTab", "false".getBytes());
        if(!BungeePlugin.getLoginManager().getLogged().contains(e.getPlayer().getDisplayName())){
            return;
        }
        if(user != null){
            final UserChangeSectorPacket packet = new UserChangeSectorPacket(player.getDisplayName(),e.getTarget().getName());
            RedisClient.sendProxiesPacket(packet);
        }
        if(!AllowBuildManager.isWhitelisted(player.getName())) {
            if (e.getTarget().getName().equalsIgnoreCase("kopalnia") || e.getTarget().getName().equalsIgnoreCase("magazyn")) {
                final byte[] b = "tttttttttttttak".getBytes();
                player.sendData("MC|Cbr600F2", b);
            } else {
                final byte[] b = "Wylacz".getBytes();
                player.sendData("MC|Cbr600F2", b);
            }
        }
    }


    @EventHandler
    public void onJoin(final ServerConnectedEvent e) {
        //LOGOWANIE
        final ProxiedPlayer player = e.getPlayer();
        final String name = player.getName();
        RedisChannel.INSTANCE.ONLINE_PLAYERS.add(name);
        ProxyManager.getCurrentProxy().addOnlinePlayerToRedis(player.getDisplayName());
        if (BungeePlugin.getLoginManager().getLogged().contains(name)) {
            return;
        }
        final Boolean premium = BungeePlugin.getLoginManager().getPremium().getIfPresent(name);
        if (premium == null) {
            player.disconnect(new TextComponent(ChatUtil.fixColor("&4Blad logowania #3")));
            return;
        }
        /*final ProxyJoinPacket packet1 = new ProxyJoinPacket(player.getDisplayName(), ProxyConfig.getCURRENTPROXY_NAME());
        RedisClient.sendProxiesPacket(packet1);*/
        player.sendData("BP|ShowPingOnTab", "false".getBytes());
        if (BungeePlugin.getBungeeUserManager().getUser(player.getUniqueId()) == null) {
            //BungeePlugin.INSTANCE.getLogger().log(Level.INFO, "proxy name: " + BungeePlugin.getMotdConfig().getCurrentProxyName() + " name " + player.getName());
            //final Packet packet = new PlayerJoinProxyPacket(BungeePlugin.getMotdConfig().getCurrentProxyName(), player.getName());
            //BungeePlugin.getSectorClient().sendGlobalProxyPacket(packet);
            if (premium) {
                ChatUtil.sendMessage(player, "&6AUTH &8->> &7Zostales zalogowany jako &e&lPREMIUM");
                final ProxyUserRegisterPacket packet = new ProxyUserRegisterPacket(player,"PREMIUM",premium);
                RedisClient.sendProxiesPacket(packet);
                BungeePlugin.getLoginManager().getLogged().add(player.getName());
                final BungeeUser user = BungeePlugin.getBungeeUserManager().createUser(player,"PREMIUM",premium);
                BungeePlugin.getLoginManager().sendToLastSector(player, user);
            } else {
                ChatUtil.sendMessage(player, "&6&lAUTH &8->> &7Zarejestruj sie za pomoca &6/register <haslo> <haslo>");
            }
        }else{
            final BungeeUser user = BungeePlugin.getBungeeUserManager().getUser(player.getUniqueId());
            user.setLastName(player.getDisplayName());
            user.setLastIP(player.getAddress().getAddress().getHostAddress());

            user.updateall();
            final BungeeUserUpdatePacket packet = new BungeeUserUpdatePacket(user.getUuid(), user.getLastIP(), user.getLastName());
            RedisClient.sendProxiesPacket(packet);

            if(user.isPremium()){
                ChatUtil.sendMessage(player, "&6AUTH &8->> &7Zostales zalogowany jako &e&lPREMIUM");
                BungeePlugin.getLoginManager().getLogged().add(player.getName());
                BungeePlugin.getLoginManager().sendToLastSector(player, user);
            }else{
                ChatUtil.sendMessage(player, "&6&lAUTH &8->> &7Zaloguj sie za pomoca &6/login <haslo>");
            }
        }
    }

}
