package pl.justpvp.bungee.sectors;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SectorManager {

    private final static LinkedHashMap<String, Sector> sectors = new LinkedHashMap<>();

    public static void registerSector(final Sector sector)
    {
        sectors.put(sector.getSectorName(), sector);
    }

    public static Sector getSector(final String sectorName)
    {
        return sectors.get(sectorName);
    }

    public static LinkedHashMap<String, Sector> getSectors() {
        return sectors;
    }

}
