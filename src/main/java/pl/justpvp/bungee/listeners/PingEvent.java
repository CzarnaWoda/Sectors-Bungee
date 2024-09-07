package pl.justpvp.bungee.listeners;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.configs.ProxyConfig;
import pl.justpvp.bungee.proxies.ProxyManager;
import pl.justpvp.bungee.util.ChatUtil;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class PingEvent implements Listener {


    @EventHandler(priority = 1)
    public void onPing(ProxyPingEvent e)
    {
        final ServerPing response = e.getResponse();
        if(response == null)
        {
            return;
        }
        final ServerPing serverPing = e.getResponse();
        final List<ServerPing.PlayerInfo> customPlayerInfo = new ArrayList<>();
        final int[] index = {1};
        ProxyConfig.PLAYERSINFO.forEach(s -> {
            customPlayerInfo.add(new ServerPing.PlayerInfo(ChatUtil.fixColor(s.replace("{PROXYNAME}" , ProxyManager.getCurrentProxy().getProxyName())),String.valueOf(index[0])));
            index[0]++;
        });
        serverPing.getPlayers().setSample(customPlayerInfo.toArray((new ServerPing.PlayerInfo[0])));
        final String protocol = ProxyConfig.PROTOCOL;
        final double multipler = ProxyConfig.MULTIPLER;
        final String motd = ChatUtil.fixColor(ProxyConfig.MOTD_1) + "\n" + ChatUtil.fixColor(ProxyConfig.MOTD_2);
        serverPing.setDescriptionComponent(new TextComponent(motd));
        serverPing.getPlayers().setOnline((-1337));
        serverPing.getPlayers().setMax(-1337);
        if(!protocol.equalsIgnoreCase("none")){
            if(ProxyConfig.MOTD_MASK != 0){
                serverPing.setVersion(new ServerPing.Protocol(ChatUtil.fixColor(protocol.replace("{ONLINE}",String.valueOf(ProxyConfig.MOTD_MASK)).replace("{MAX}",String.valueOf(ProxyConfig.MOTD_MASK))),1337));
            }else
                serverPing.setVersion(new ServerPing.Protocol(ChatUtil.fixColor(protocol.replace("{ONLINE}",String.valueOf(BungeePlugin.ONLINE)).replace("{MAX}",String.valueOf(BungeePlugin.ONLINE + 1))),1337));
        }
    }

}
