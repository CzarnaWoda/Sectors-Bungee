package pl.justpvp.bungee.auth;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import pl.justpvp.bungee.configs.ProxyConfig;
import pl.justpvp.bungee.packets.impl.configs.ConfigPacket;
import pl.justpvp.bungee.redis.client.RedisClient;
import pl.justpvp.bungee.util.ChatUtil;

import java.util.Arrays;

public class MotdCommand extends Command {

    public MotdCommand() {
        super("motd", "ProxyConfig.motd");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        final String rightusage = "/motd [protocol/playerlist/multipler/description1/description2/mask] [motd]";
        if(args.length < 2){
            ChatUtil.sendMessage(commandSender, "&4Poprawne uzycie: " + rightusage);
            return;
        }
        switch (args[0]) {
            case "protocol":
                final ConfigPacket packet = new ConfigPacket("motd.protocol",args[1]);
                RedisClient.sendProxiesPacket(packet);
                ChatUtil.sendMessage(commandSender, "&7Ustawiono: &9" + args[0].toUpperCase() + " &7na &9" + args[1]);
                break;
            case "playerlist":
                final ConfigPacket packet1 = new ConfigPacket("motd.playersinfo",args[1]);
                RedisClient.sendProxiesPacket(packet1);
                ChatUtil.sendMessage(commandSender, "&7Ustawiono: &9" + args[0].toUpperCase() + " &7na &9" + args[1]);
                break;
            case "multipler":
                final ConfigPacket packet2 = new ConfigPacket("motd.multipler",args[1]);
                RedisClient.sendProxiesPacket(packet2);
                ChatUtil.sendMessage(commandSender, "&7Ustawiono: &9" + args[0].toUpperCase() + " &7na &9" + args[1]);
                break;
            case "description1":
                String[] motd = Arrays.copyOfRange(args,1,args.length);
                String motdfinal = String.join(" ", motd);
                final ConfigPacket packet3 = new ConfigPacket("motd.description.1",motdfinal);
                RedisClient.sendProxiesPacket(packet3);
                ChatUtil.sendMessage(commandSender, "&7Ustawiono: &9" + args[0].toUpperCase() + " &7na &9" + motdfinal);
                break;
            case "description2":
                String[] motd2 = Arrays.copyOfRange(args,1,args.length);
                String motdfinal2 = String.join(" ", motd2);
                final ConfigPacket packet4 = new ConfigPacket("motd.description.2",motdfinal2);
                RedisClient.sendProxiesPacket(packet4);
                ChatUtil.sendMessage(commandSender, "&7Ustawiono: &9" + args[0].toUpperCase() + " &7na &9" + motdfinal2);
                break;
            case "mask":
                int mask = Integer.parseInt(args[1]);
                final ConfigPacket packet5 = new ConfigPacket("motd.mask",mask);
                RedisClient.sendProxiesPacket(packet5);
                ChatUtil.sendMessage(commandSender, "&7Ustawiono: &9" + args[0].toUpperCase() + " &7na &9" + mask);
            default:
                ChatUtil.sendMessage(commandSender, "&4Poprawne uzycie: " + rightusage);
                break;
        }

    }
}
