package pl.justpvp.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import pl.justpvp.bungee.managers.WhitelistManager;
import pl.justpvp.bungee.packets.whitelist.WhitelistAddPacket;
import pl.justpvp.bungee.packets.whitelist.WhitelistDisablePacket;
import pl.justpvp.bungee.packets.whitelist.WhitelistEnablePacket;
import pl.justpvp.bungee.packets.whitelist.WhitelistRemovePacket;
import pl.justpvp.bungee.redis.channels.RedisChannel;
import pl.justpvp.bungee.redis.client.RedisClient;
import pl.justpvp.bungee.util.ChatUtil;

public class WhitelistCommand extends Command {

    public WhitelistCommand() {
        super("whitelist", "admin.whitelist");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final String usage = "/whitelist add/remove|on/off <nick>";

        if (args.length != 2) {
            ChatUtil.sendMessage(sender, "&4Poprawne uzycie: &c" + usage);
            return;
        }
        if (args[0].equalsIgnoreCase("add"))
        {
            if(WhitelistManager.isWhitelisted(args[1]))
            {
                ChatUtil.sendMessage(sender, "&cTen gracz jest juz na whiteliscie!");
                return;
            }
            RedisClient.sendProxiesPacket(new WhitelistAddPacket(args[1]));
            WhitelistManager.addToWhitelist(args[1]);
            RedisChannel.INSTANCE.WHITELISTED.add(args[1]);
            ChatUtil.sendMessage(sender, "&7Dodano gracza &c" + args[1] + " &7do whitelisty!");
        }
        else if(args[0].equalsIgnoreCase("remove"))
        {
            if(!WhitelistManager.isWhitelisted(args[1]))
            {
                ChatUtil.sendMessage(sender, "&cTen gracz nie jest dodany do whitelisty!");
                return;
            }
            RedisClient.sendProxiesPacket(new WhitelistRemovePacket(args[1]));
            RedisChannel.INSTANCE.WHITELISTED.remove(args[1]);
            ChatUtil.sendMessage(sender, "&7Usunieto gracza &c" + args[1] + " &7z whitelisty!");
        }
        else if(args[0].equalsIgnoreCase("on"))
        {
            RedisChannel.INSTANCE.WHITELIST_ENABLED.set(0, true);
            RedisClient.sendProxiesPacket(new WhitelistEnablePacket());
        }
        else if(args[0].equalsIgnoreCase("off"))
        {
            RedisChannel.INSTANCE.WHITELIST_ENABLED.set(0, false);
            RedisClient.sendProxiesPacket(new WhitelistDisablePacket());
        }
    }
}
