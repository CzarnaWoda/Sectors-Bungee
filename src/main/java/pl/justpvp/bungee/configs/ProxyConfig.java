package pl.justpvp.bungee.configs;

import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import pl.justpvp.bungee.BungeePlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class ProxyConfig {
    @Getter
    private static File fileConfig;
    @Getter
    private static Configuration config;

    @Getter
    public static String MOTD_1;
    @Getter
    public static String MOTD_2;
    @Getter
    public static int MULTIPLER;
    @Getter
    public static String PROTOCOL;
    @Getter
    public static List<String> PLAYERSINFO;

    @Getter
    public static String REDIS_ADDRESS;
    @Getter
    public static String REDIS_PASSWORD;
    @Getter
    public static String CURRENTPROXY_NAME;
    @Getter
    public static int MOTD_MASK;
    public ProxyConfig(){
        MOTD_1 = getConfig().getString("motd.description.1");
        MOTD_2 = getConfig().getString("motd.description.2");
        MULTIPLER = getConfig().getInt("motd.multipler");
        PROTOCOL = getConfig().getString("motd.protocol");

        REDIS_ADDRESS = getConfig().getString("redis.address");
        REDIS_PASSWORD = getConfig().getString("redis.password");
        CURRENTPROXY_NAME = getConfig().getString("redis.servername");
        MOTD_MASK = getConfig().getInt("motd.mask");

        PLAYERSINFO = getConfig().getStringList("motd.playersinfo");

    }

    public static void saveDefaultConfig(){
        fileConfig = new File(BungeePlugin.INSTANCE.getDataFolder(), "config.yml");
        if(!BungeePlugin.INSTANCE.getDataFolder().exists()){
            BungeePlugin.INSTANCE.getDataFolder().mkdirs();
        }
        if(!fileConfig.exists()){
            try (InputStream in = BungeePlugin.INSTANCE.getResourceAsStream("config.yml")) {
                Files.copy(in, fileConfig.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(fileConfig, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadConfig(){
        if(fileConfig.exists()){
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, fileConfig);
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(fileConfig, config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try (InputStream in = BungeePlugin.INSTANCE.getResourceAsStream("config.yml")) {
                Files.copy(in, fileConfig.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveConfig(){
        if(fileConfig.exists()){
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, fileConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
