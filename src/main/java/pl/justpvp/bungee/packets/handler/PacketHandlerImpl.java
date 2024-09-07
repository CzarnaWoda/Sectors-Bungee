package pl.justpvp.bungee.packets.handler;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.auth.BungeeUser;
import pl.justpvp.bungee.configs.ProxyConfig;
import pl.justpvp.bungee.data.Ban;
import pl.justpvp.bungee.data.BanIP;
import pl.justpvp.bungee.listeners.Colors;
import pl.justpvp.bungee.managers.AllowBuildManager;
import pl.justpvp.bungee.managers.BanIPManager;
import pl.justpvp.bungee.managers.BanManager;
import pl.justpvp.bungee.managers.WhitelistManager;
import pl.justpvp.bungee.packets.bans.*;
import pl.justpvp.bungee.packets.chat.GlobalChatMessage;
import pl.justpvp.bungee.packets.impl.allowedbuild.AllowBuildPacket;
import pl.justpvp.bungee.packets.impl.bans.BanAddPacket;
import pl.justpvp.bungee.packets.impl.configs.ConfigPacket;
import pl.justpvp.bungee.packets.impl.itemshop.AddSlotReservationPacket;
import pl.justpvp.bungee.packets.impl.proxy.ProxyDisablePacket;
import pl.justpvp.bungee.packets.impl.proxy.ProxyEnablePacket;
import pl.justpvp.bungee.packets.impl.proxy.ProxyStatusPacket;
import pl.justpvp.bungee.packets.impl.sectors.SectorStatusPacket;
import pl.justpvp.bungee.packets.impl.user.*;
import pl.justpvp.bungee.packets.whitelist.WhitelistAddPacket;
import pl.justpvp.bungee.packets.whitelist.WhitelistDisablePacket;
import pl.justpvp.bungee.packets.whitelist.WhitelistEnablePacket;
import pl.justpvp.bungee.packets.whitelist.WhitelistRemovePacket;
import pl.justpvp.bungee.proxies.Proxy;
import pl.justpvp.bungee.proxies.ProxyManager;
import pl.justpvp.bungee.sectors.Sector;
import pl.justpvp.bungee.sectors.SectorManager;
import pl.justpvp.bungee.util.ChatUtil;
import pl.justpvp.bungee.util.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;

public class PacketHandlerImpl implements PacketHandler, Colors {

    @Override
    public void handle(ProxyUserRegisterPacket packet) {

        BungeePlugin.INSTANCE.getBungeeUserManager().createUserOnProxy(packet.getUuid(),packet.getLastName(),packet.getFirstIP(),packet.getLastIP(),packet.getPassword(),packet.isPremium());
    }

    @Override
    public void handle(ProxyEnablePacket packet) {
        final Proxy proxy = ProxyManager.getProxy(packet.getProxyName());

        proxy.clearOnlinePlayer();
    }

    @Override
    public void handle(ProxyDisablePacket packet) {
        final Proxy proxy = ProxyManager.getProxy(packet.getProxyName());
        if(proxy != null) {
            proxy.clearOnlinePlayer();
        }
    }

    @Override
    public void handle(ProxyJoinPacket packet) {
        final Proxy proxy = ProxyManager.getProxy(packet.getProxyName());
        if(proxy != null) {
           // proxy.addOnlinePlayer(packet.getPlayerName());
        }
    }

    @Override
    public void handle(ProxyLeavePacket packet) {
        final Proxy proxy = ProxyManager.getProxy(packet.getProxyName());
        if(proxy != null) {
           // proxy.removeOnlinePlayer(packet.getPlayerName());
        }
    }

    @Override
    public void handle(UserChangeSectorPacket packet) {
        final BungeeUser user = BungeePlugin.getBungeeUserManager().getUser(packet.getPlayerName());

        user.setLastSector(packet.getSectorName());
    }

    @Override
    public void handle(ProxyStatusPacket packet) {
        final Proxy proxy = ProxyManager.getProxy(packet.getProxyName());
        if(proxy != null)
        {
            proxy.setLastUpdate(System.currentTimeMillis());
        }
    }

    @Override
    public void handle(SectorStatusPacket packet) {
        final Sector sector = SectorManager.getSector(packet.getSectorName());
        if(sector != null)
        {
            sector.setLastUpdate(System.currentTimeMillis());
            sector.setTps(packet.getSectorTPS());
            sector.setOnlinePlayers(packet.getOnlinePlayers());
        }
    }

    @Override
    public void handle(CreateBanPacket packet) {
        final Ban ban = new Ban(packet.getUuid(),packet.getReason(),packet.getAdmin(),packet.getCreateTime(),packet.getExpireTime(),packet.isUnban());
        BanManager.getBans().put(ban.getUuid(), ban);

        final ProxiedPlayer player = BungeePlugin.INSTANCE.getProxy().getPlayer(packet.getUuid());

        if (player != null && player.isConnected()){
            String cancelReason = ChatUtil.fixColor("       &8->> &c&lJUSTPVP.PL &8<<-       " + "\n \n" + WarningColor + "       Zostales zbanowany\n" + SpecialSigns + "» " + MainColor + "Dnia: " + ImportantColor + Util.getDate(ban.getCreateTime()) + ((ban.getExpireTime() > 0L) ? "\n" + SpecialSigns + "» " + MainColor + "Do: " + ImportantColor + Util.getDate(ban.getExpireTime()) : "\n " + SpecialSigns + "» Do: " + ImportantColor + "na zawsze") + "\n" + SpecialSigns + "» " + MainColor + "Przez: " + ImportantColor + ban.getAdmin() + "\n" + SpecialSigns + "» " + MainColor + "Powod: " + ImportantColor + UnderLined + ban.getReason() + "\n\n" + SpecialSigns + "» " + MainColor + "Kontakt TeamSpeak3:" + ImportantColor + "justpvp.pl");
            player.disconnect(new TextComponent(cancelReason));
        }

    }

    @Override
    public void handle(UnBanPacket packet) {
        final Ban ban = BanManager.getBan(packet.getUuid());

        ban.setUnban(true);
    }

    @Override
    public void handle(DeleteBanPacket packet) {
        BanManager.getBans().remove(packet.getUuid());
    }

    @Override
    public void handle(GlobalChatMessage packet) {
        ProxyServer.getInstance().broadcast(new TextComponent(ChatUtil.fixColor(packet.getMessage())));
    }

    @Override
    public void handle(BungeeUserUpdatePacket packet) {
        final BungeeUser user = BungeePlugin.getBungeeUserManager().getUser(packet.getUuid());

        user.setLastIP(packet.getLastIP());
        user.setLastName(packet.getLastName());
    }

    @Override
    public void handle(CreateIPBanPacket packet) {
        final BanIP banIP = new BanIP(packet.getIp(),packet.getReason(),packet.getAdmin(),packet.getCreateTime(),packet.getExpireTime(),packet.isUnban());
        BanIPManager.getIpbans().put(banIP.getIp(),banIP);

        final Proxy proxy = ProxyManager.getCurrentProxy();
        final BungeeUser user = BungeePlugin.getBungeeUserManager().getUserByIP(banIP.getIp());
        if(proxy.getRedisOnlinePlayers().contains(user.getLastName())) {
            final ProxiedPlayer player = BungeePlugin.INSTANCE.getProxy().getPlayer(user.getUuid());

            if (player != null && player.isConnected()) {
                String cancelReason = ChatUtil.fixColor("       &8->> &c&lJUSTPVP.PL &8<<-       " + "\n \n" + WarningColor + "       Zostales zbanowany\n" + SpecialSigns + "» " + MainColor + "Dnia: " + ImportantColor + Util.getDate(banIP.getCreateTime()) + ((banIP.getExpireTime() > 0L) ? "\n" + SpecialSigns + "» " + MainColor + "Do: " + ImportantColor + Util.getDate(banIP.getExpireTime()) : "\n " + SpecialSigns + "» Do: " + ImportantColor + "na zawsze") + "\n" + SpecialSigns + "» " + MainColor + "Przez: " + ImportantColor + banIP.getAdmin() + "\n" + SpecialSigns + "» " + MainColor + "Powod: " + ImportantColor + UnderLined + banIP.getReason() + "\n\n" + SpecialSigns + "» " + MainColor + "Kontakt TeamSpeak3:" + ImportantColor + "justpvp.pl");
                player.disconnect(new TextComponent(cancelReason));
            }
        }
    }

    @Override
    public void handle(DeleteIPBanPacket packet) {
        BanIPManager.getIpbans().remove(packet.getIp());
    }

    @Override
    public void handle(AddSlotReservationPacket addSlotReservationPacket) {
        final BungeeUser user = BungeePlugin.getBungeeUserManager().getUser(addSlotReservationPacket.getUserName());
        if(user != null)
        {
            user.setHasReservation(true);
        }
    }

    @Override
    public void handle(ConfigPacket packet) {
        if(packet.getPath().equalsIgnoreCase("motd.playersinfo")){
            ProxyConfig.getConfig().set(packet.getPath(), Collections.singletonList(packet.getValue()));
        }else{
            ProxyConfig.getConfig().set(packet.getPath(),packet.getValue());
            ProxyConfig.saveConfig();
        }
        new ProxyConfig();
    }

    @Override
    public void handle(WhitelistAddPacket packet) {
        WhitelistManager.addToWhitelist(packet.getUserName());
    }

    @Override
    public void handle(WhitelistRemovePacket packet) {
        WhitelistManager.removeFromWhitelist(packet.getUserName());
    }

    @Override
    public void handle(WhitelistEnablePacket packet) {
        WhitelistManager.setEnabled(true);
        BungeePlugin.INSTANCE.getLogger().log(Level.INFO, "Whitelista zostala wlaczona!");
    }

    @Override
    public void handle(WhitelistDisablePacket packet) {
        WhitelistManager.setEnabled(false);
        BungeePlugin.INSTANCE.getLogger().log(Level.INFO, "Whitelista zostala wylaczona!");
    }

    @Override
    public void handle(AllowBuildPacket allowBuildPacket) {
        if(allowBuildPacket.isAdding()){
            AllowBuildManager.addUser(allowBuildPacket.getNickName());
        }
        else{
            AllowBuildManager.removeUser(allowBuildPacket.getNickName());
        }
    }

    @Override
    public void handle(BanAddPacket packet) {
        final Ban ban = new Ban(packet.getUuid(),packet.getReason(),packet.getAdmin(), System.currentTimeMillis() ,packet.getExpireTime(), false);
        BanManager.getBans().put(ban.getUuid(), ban);

        final ProxiedPlayer player = BungeePlugin.INSTANCE.getProxy().getPlayer(packet.getUuid());

        if (player != null && player.isConnected()){
            String cancelReason = ChatUtil.fixColor("       &8->> &c&lJUSTPVP.PL &8<<-       " + "\n \n" + WarningColor + "       Zostales zbanowany\n" + SpecialSigns + "» " + MainColor + "Dnia: " + ImportantColor + Util.getDate(ban.getCreateTime()) + ((ban.getExpireTime() > 0L) ? "\n" + SpecialSigns + "» " + MainColor + "Do: " + ImportantColor + Util.getDate(ban.getExpireTime()) : "\n " + SpecialSigns + "» Do: " + ImportantColor + "na zawsze") + "\n" + SpecialSigns + "» " + MainColor + "Przez: " + ImportantColor + ban.getAdmin() + "\n" + SpecialSigns + "» " + MainColor + "Powod: " + ImportantColor + UnderLined + ban.getReason() + "\n\n" + SpecialSigns + "» " + MainColor + "Kontakt TeamSpeak3:" + ImportantColor + "justpvp.pl");
            player.disconnect(new TextComponent(cancelReason));
        }
    }
}
