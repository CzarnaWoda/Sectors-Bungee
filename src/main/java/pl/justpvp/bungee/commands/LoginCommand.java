package pl.justpvp.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.auth.BungeeUser;
import pl.justpvp.bungee.util.ChatUtil;

public class LoginCommand extends Command {


    public LoginCommand() {
        super("login", "auth.login", "l");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            BungeeUser u = BungeePlugin.getBungeeUserManager().getUser(player);

            if (!player.getServer().getInfo().getName().equalsIgnoreCase("limbo")) {
                ChatUtil.sendMessage(sender, "&4Blad: mozesz zalogowac sie tylko na serwerze logowania");
                return;
            }
            if (u == null) {
                ChatUtil.sendMessage(player, "&6&lAUTH &8->> &7Zarejestruj sie za pomoca &6/register <haslo> <haslo>");
                return;
            }
            if(u.isPremium()){
                player.disconnect(new TextComponent(ChatUtil.fixColor("&4Blad: &cZaloguj sie ponowanie !")));
                return;
            }
            if (args.length != 1) {
                ChatUtil.sendMessage(player, "&6&lAUTH &8->> &7Zaloguj sie za pomoca &6/login <haslo>");
                return;
            }
            if (u.getPassword().equals(args[0])) {
                ChatUtil.sendMessage(player, "&8->> &7Wprowadziles poprawnie swoje &6HASLO");
                BungeePlugin.getLoginManager().sendToLastSector(player, u);
                BungeePlugin.getLoginManager().getLogged().add(player.getName());
            }else{
                player.disconnect(new TextComponent(ChatUtil.fixColor("&4Blad: &cWprowadziles niepoprawne haslo!")));
            }
        }
    }
}