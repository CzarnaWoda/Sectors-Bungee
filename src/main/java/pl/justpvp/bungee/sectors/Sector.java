package pl.justpvp.bungee.sectors;

import org.redisson.api.RSet;
import pl.justpvp.bungee.redis.RedisManager;

import java.util.HashSet;
import java.util.Set;

public class Sector {

    private final String sectorName;
    private int onlinePlayers;
    private double tps;
    private Long lastUpdate;

    public Sector(String sectorName) {
        this.sectorName = sectorName;
        this.onlinePlayers = 0;
        this.tps = 20;
        this.lastUpdate = -1L;
    }

    public String getSectorName() {
        return sectorName;
    }


    public double getTps() {
        return tps;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setTps(double tps) {
        this.tps = tps;
    }

    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public void addOnlinePlayers(final int amount)
    {
        this.onlinePlayers = this.onlinePlayers + amount;
    }

    public boolean isOnline()
    {
        return System.currentTimeMillis() - this.lastUpdate < 8000L;
    }
}
