package pl.justpvp.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.packets.impl.allowedbuild.AllowBuildPacket;
import pl.justpvp.bungee.redis.channels.RedisChannel;
import pl.justpvp.bungee.redis.client.RedisClient;
import pl.justpvp.bungee.util.ChatUtil;

public class BuildAddCommand extends Command {

    public BuildAddCommand() {
        super("buildadd", "admin.buildadd");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length != 2)
        {
            ChatUtil.sendMessage(sender, "&cPoprawne uzycie /buildadd <add/remove> <nick>");
            return;
        }
        if(args[0].equalsIgnoreCase("add"))
        {
            RedisClient.sendProxiesPacket(new AllowBuildPacket(args[1], true));
            RedisChannel.INSTANCE.ALLOWED_BUILD.add(args[1]);
            ChatUtil.sendMessage(sender, "&7Dodano gracza &c" + args[1] + " &7do whitelisty budowania!");
        }
        else if(args[0].equalsIgnoreCase("remove"))
        {
            RedisClient.sendProxiesPacket(new AllowBuildPacket(args[1], false));
            RedisChannel.INSTANCE.ALLOWED_BUILD.remove(args[1]);
            ChatUtil.sendMessage(sender, "&7Usunieto gracza &c" + args[1] + " &7z whitelisty budowania!");
        }
    }
}
