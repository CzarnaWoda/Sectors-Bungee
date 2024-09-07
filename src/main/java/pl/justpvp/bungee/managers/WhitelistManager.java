package pl.justpvp.bungee.managers;

import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.redis.channels.RedisChannel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class WhitelistManager {

    private static Set<String> whitelisted = new HashSet<>();
    private static boolean isEnabled;

    public static void setup()
    {
        whitelisted.addAll(RedisChannel.INSTANCE.WHITELISTED);
        WhitelistManager.setEnabled(RedisChannel.INSTANCE.WHITELIST_ENABLED.get(0));
        BungeePlugin.INSTANCE.getLogger().log(Level.INFO, "Zaladowano " + whitelisted.size() + " graczy na whiteliscie!");
    }

    public static boolean isWhitelisted(final String name)
    {
        return whitelisted.contains(name);
    }

    public static void addToWhitelist(final String name)
    {
        whitelisted.add(name);
    }

    public static void removeFromWhitelist(final String name)
    {
        whitelisted.remove(name);
    }

    public static boolean isEnabled() {
        return isEnabled;
    }

    public static void setEnabled(boolean isEnabled) {
        WhitelistManager.isEnabled = isEnabled;
    }
}
