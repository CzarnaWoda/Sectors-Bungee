package pl.justpvp.bungee.auth;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PreLoginEvent;
import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.packets.impl.user.UserChangeSectorPacket;
import pl.justpvp.bungee.redis.client.RedisClient;
import pl.justpvp.bungee.sectors.Sector;
import pl.justpvp.bungee.sectors.SectorManager;
import pl.justpvp.bungee.util.ChatUtil;

import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class LoginManager {

    private BungeePlugin plugin;
    private HashMap<String,PreLoginEvent> waiting;
    private Cache<String, Boolean> premium;
    private ArrayList<String> logged;
    private Cache<String,String> ips;
    private int currentJoins;

    public LoginManager(BungeePlugin plugin){
        this.plugin = plugin;
        this.waiting = new HashMap<>();
        this.premium = CacheBuilder.newBuilder().expireAfterWrite(20,TimeUnit.SECONDS).build();
        this.logged = new ArrayList<>();
        ips = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.SECONDS).build();
        currentJoins = 0;

    }

    public void registerIntent(PreLoginEvent e ,String name){
        waiting.put(name,e);
        e.registerIntent(plugin);
        addCurrentJoints(1);
        final String ip = e.getConnection().getAddress().getAddress().getHostAddress();
        if(getIps().getIfPresent(ip) != null){
            unregisterIntent(name, "Mozesz wejsc dopiero za 15s!");
            return;
        }
        getIps().put(ip,name);
        if(premium.getIfPresent(name) == null){
            final BungeeUser user = BungeePlugin.getBungeeUserManager().getUser(e.getConnection().getName());
            if(user == null){
                unregisterIntent(name,"&4Blad logowania #2");
            }else{
                premium.put(name,user.isPremium());
                System.out.println("Putuje do premium: " + name + ", premium: " + (user.isPremium() ? 1 : 0));
                unregisterIntent(name, premium.getIfPresent(name));
            }
        }else{
            unregisterIntent(name,premium.getIfPresent(name));
        }
    }

    private void unregisterIntent(String name, String reason){
        final PreLoginEvent  e = waiting.get(name);
        e.setCancelReason(new TextComponent(ChatUtil.fixColor(reason)));
        e.setCancelled(true);
        e.completeIntent(plugin);
        waiting.remove(name);
    }
    private void unregisterIntent(String name, boolean premium){
        final PreLoginEvent e = waiting.get(name);
        e.getConnection().setOnlineMode(premium);
        e.completeIntent(plugin);
        waiting.remove(name);
    }

    public Cache<String, Boolean> getPremium() {
        return premium;
    }

    public ArrayList<String> getLogged() {
        return logged;
    }

    public Cache<String, String> getIps() {
        return ips;
    }

    public void sendToLastSector(final ProxiedPlayer player, final BungeeUser user)
    {
        final Sector sector = getBestSector(user);
        if(sector == null)
        {
            player.disconnect(new TextComponent(ChatUtil.fixColor("&cNie ma obecnie wolnego sektora!")));
            return;
        }
        System.out.println("Wylosowano sektor " + sector.getSectorName());
        final ServerInfo target = ProxyServer.getInstance().getServerInfo(sector.getSectorName());
        player.connect(target);
        final UserChangeSectorPacket packet = new UserChangeSectorPacket(player.getDisplayName(),sector.getSectorName());
        user.setLastSector(sector.getSectorName());
        user.updateall();
        RedisClient.sendProxiesPacket(packet);
    }

    private Sector getBestSector(final BungeeUser user)
    {
        if(user.getLastSector().contains("kopalnia") || user.getLastSector().contains("magazyn"))
        {
            return SectorManager.getSector(user.getLastSector());
        }
        int online = 0;
        final List<Sector> sectors = new LinkedList<>(SectorManager.getSectors().values());
        sectors.sort(new SectorSorter());
        for(Sector sector : sectors)
        {
            if(sector.getSectorName().contains("magazyn") || sector.getSectorName().contains("kopalnia")){
                continue;
            }
            if(sector.isOnline())
            {
                online = online + sector.getOnlinePlayers();
                if(sector.getOnlinePlayers() >= 400 && sector.getTps() > 19 && user.hasSlotReservation())
                {
                    return sector;
                }
                if(sector.getOnlinePlayers() < 400 && sector.getTps() > 18)
                {
                    return sector;
                }
            }
        }
        return null;
    }

    public void setCurrentJoins(int currentJoins) {
        this.currentJoins = currentJoins;
    }

    public int getCurrentJoins() {
        return currentJoins;
    }
    public void addCurrentJoints(int add){
        this.currentJoins += add;
    }

    class SectorSorter implements Comparator<Sector>
    {

        @Override
        public int compare(Sector o1, Sector o2) {
            return o2.getOnlinePlayers() - o1.getOnlinePlayers();
        }
    }
}
