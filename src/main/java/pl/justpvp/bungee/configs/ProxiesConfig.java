package pl.justpvp.bungee.configs;

import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.proxies.Proxy;
import pl.justpvp.bungee.proxies.ProxyManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

public class ProxiesConfig {
    @Getter
    private static File fileConfig;
    @Getter
    private static Configuration config;

    public ProxiesConfig(){

        for(String key : config.getSection("proxies").getKeys()){
            final Configuration c = config.getSection("proxies." + key);


            final String proxyName = c.getString("proxyname");

            final Proxy proxy = new Proxy(proxyName);

            ProxyManager.registerProxy(proxy);

            BungeePlugin.INSTANCE.getLogger().log(Level.INFO, "Zaladowalem obiekt proxy: " + proxyName);
        }
    }

    public static void saveDefaultConfig(){
        fileConfig = new File(BungeePlugin.INSTANCE.getDataFolder(), "proxies.yml");
        if(!BungeePlugin.INSTANCE.getDataFolder().exists()){
            BungeePlugin.INSTANCE.getDataFolder().mkdirs();
        }
        if(!fileConfig.exists()){
            try (InputStream in = BungeePlugin.INSTANCE.getResourceAsStream("proxies.yml")) {
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
            try (InputStream in = BungeePlugin.INSTANCE.getResourceAsStream("proxies.yml")) {
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
