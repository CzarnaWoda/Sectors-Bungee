package pl.justpvp.bungee.proxies;

import pl.justpvp.bungee.configs.ProxyConfig;

import java.util.HashMap;
import java.util.Map;

public class ProxyManager {

    private static Map<String, Proxy> proxies =  new HashMap<>();

    public static Proxy getProxy(String proxyName){
        return proxies.get(proxyName);
    }

    public static void registerProxy(Proxy proxy){
        proxies.put(proxy.getProxyName(),proxy);
    }

    public static Proxy getCurrentProxy(){
        return proxies.get(ProxyConfig.getCURRENTPROXY_NAME());
    }

    public static Map<String, Proxy> getProxies() {
        return proxies;
    }
}
