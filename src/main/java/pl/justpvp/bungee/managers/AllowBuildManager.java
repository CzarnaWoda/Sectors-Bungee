package pl.justpvp.bungee.managers;

import net.md_5.bungee.BungeeCord;
import pl.justpvp.bungee.redis.channels.RedisChannel;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class AllowBuildManager {

    private static Set<String> allowedNicks = new HashSet<>();


    public static void setup()
    {
        allowedNicks.addAll(RedisChannel.INSTANCE.ALLOWED_BUILD);
        BungeeCord.getInstance().getLogger().log(Level.INFO, "Zaladowano " + allowedNicks.size() + " userow z allow buildem!");
    }


    public static boolean isWhitelisted(final String name)
    {
        return allowedNicks.contains(name);
    }

    public static void addUser(final String name)
    {
        allowedNicks.add(name);
    }

    public static void removeUser(final String name)
    {
        allowedNicks.remove(name);
    }
}

