package pl.justpvp.bungee.listeners;

import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.apache.commons.lang3.StringUtils;
import org.redisson.misc.Hash;
import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.auth.BungeeUser;
import pl.justpvp.bungee.proxies.Proxy;
import pl.justpvp.bungee.proxies.ProxyManager;
import pl.justpvp.bungee.redis.channels.RedisChannel;
import sun.swing.StringUIClientPropertyKey;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TabCompleteListener implements Listener {

    private static final List<String> commandsUserTab = Arrays.asList("/ban","/unban","/baninfo","/banip","/tempban","/unbanip");
    private static HashMap<Connection,Long> times = new HashMap<>();
    @EventHandler
    public void onTabComplete(TabCompleteEvent e){
        if(times.get(e.getSender()) != null && times.get(e.getSender()) >= System.currentTimeMillis()){
            return;
        }
        String partialPlayerName = e.getCursor().toLowerCase();

        String[] split = partialPlayerName.split(" ");
        String name = split[split.length -1];

        final String command = split[0];
        if(commandsUserTab.contains(command)) {
            if(name.length() >= 3) {
                int lastSpaceIndex = partialPlayerName.lastIndexOf(' ');
                if (lastSpaceIndex >= 0) {
                    partialPlayerName = partialPlayerName.substring(lastSpaceIndex + 1);
                }
                for (BungeeUser user : BungeePlugin.getBungeeUserManager().getAuthUsers()) {
                    if (user.getLastName().toLowerCase().startsWith(partialPlayerName)) {
                        e.getSuggestions().add(user.getLastName());
                    }
                }
                times.put(e.getSender(), System.currentTimeMillis() + 8000);
            }
        }else{
            if(name.length() >= 4){
                int lastSpaceIndex = partialPlayerName.lastIndexOf(' ');
                if (lastSpaceIndex >= 0) {
                    partialPlayerName = partialPlayerName.substring(lastSpaceIndex + 1);
                }
                for(String pName : RedisChannel.INSTANCE.ONLINE_PLAYERS){
                    if (pName.toLowerCase().startsWith(partialPlayerName)) {
                        e.getSuggestions().add(pName);
                    }
                }
                times.put(e.getSender(), System.currentTimeMillis() + 8000);
            }
        }
    }
}
