package pl.justpvp.bungee.listeners;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.blazingpack.bpauth.BlazingPackAuthEvent;
import pl.blazingpack.bpauth.BlazingPackHandshakeAuthEvent;
import pl.justpvp.bungee.BungeePlugin;

public class BlazingPackAuthListener implements Listener {


    @EventHandler
    public void auth(BlazingPackHandshakeAuthEvent e){
        BungeePlugin.getLoginManager().getPremium().put(e.getInitialHandler().getName(),e.isPremium());
    }
}
