package pl.justpvp.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.auth.BungeeUser;
import pl.justpvp.bungee.packets.impl.user.ProxyUserRegisterPacket;
import pl.justpvp.bungee.redis.client.RedisClient;
import pl.justpvp.bungee.util.ChatUtil;

public class RegisterCommand extends Command {

    public RegisterCommand(BungeePlugin plugin) {
        super("register", "auth.register", "reg","");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            final ProxiedPlayer player = (ProxiedPlayer) sender;
            if (!player.getServer().getInfo().getName().contains("limbo")) {
                ChatUtil.sendMessage(sender, "&4Blad: Tą komende mozesz wykonac tylko na serwerze logowania");
                return;
            }
            if (args.length < 2) {
                ChatUtil.sendMessage(sender, "&6&lAUTH &8->> &7Zarejestruj sie za pomoca &6/register <haslo> <haslo>");
                return;
            }
            if (args[0].length() < 6) {
                ChatUtil.sendMessage(sender, "&6&lAUTH &8->> &7Twoje haslo jest za slabe &8(&4MUSI MIEC WIECEJ NIZ 6 ZNAKOW&8)");
                return;
            }
            if (!args[0].equals(args[1])) {
                ChatUtil.sendMessage(sender, "&6&lAUTH &8->> &7Hasla &cNIE PASUJA &7do siebie");
                return;
            }
            if(!isAlphaNumeric(args[0])){
                ChatUtil.sendMessage(sender, "&6&lAUTH &8->> &cERROR #65 &8(&4&l^[a-zA-Z0-9_]*$&8) &cALPHANUMERIC REQEST !");
                return;
            }
            BungeeUser user = BungeePlugin.getBungeeUserManager().getUser(player);
            if(user != null){
                if(user.isPremium()){
                    player.disconnect(new TextComponent(ChatUtil.fixColor("&4Blad: &cZaloguj sie ponowanie !")));
                    return;
                }
                ChatUtil.sendMessage(sender, "&6&lAUTH &8->> &7Jestes juz &2&lZAREJESTROWANY");
                return;
            }
            final ProxyUserRegisterPacket packet = new ProxyUserRegisterPacket(player,args[0],false);
            RedisClient.sendProxiesPacket(packet);
            ChatUtil.sendMessage(sender, "&6&lAUTH &8->> &7Zostales poprawnie &2&lZAREJESTROWANY&7!");
            ChatUtil.sendMessage(sender, "&6&lAUTH &8->> &7Zaloguj sie &6&l/login <haslo> &7!");
        }
        }
    public static boolean isAlphaNumeric(String s) {
        return s.matches("^[a-zA-Z0-9_]*$");
    }

    public static boolean isAlphaNumeric1(String s) {
        return s.matches("^[A-Z0-9_]*$");
    }
    }
