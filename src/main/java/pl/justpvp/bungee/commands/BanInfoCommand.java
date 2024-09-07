package pl.justpvp.bungee.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.auth.BungeeUser;
import pl.justpvp.bungee.data.Ban;
import pl.justpvp.bungee.data.BanIP;
import pl.justpvp.bungee.listeners.Colors;
import pl.justpvp.bungee.managers.BanIPManager;
import pl.justpvp.bungee.managers.BanManager;
import pl.justpvp.bungee.util.ChatUtil;
import pl.justpvp.bungee.util.Util;

public class BanInfoCommand extends Command implements Colors {
    public BanInfoCommand() {
        super("baninfo","admin.baninfo");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final String usage = "/baninfo <nick>";

        if (args.length < 1){
            ChatUtil.sendMessage(sender,"&4Poprawne uzycie: &c" + usage);
            return;
        }
        final BungeeUser user = BungeePlugin.getBungeeUserManager().getUser(args[0]);
        if (user == null){
            ChatUtil.sendMessage(sender, "&4Blad: &cTaki uzytkownik nie istnieje!");
            return;
        }
        final BanIP bip = BanIPManager.getBan(user.getLastIP());
        final Ban b = BanManager.getBan(user.getUuid());

        ChatUtil.sendMessage(sender, Util.replaceString(SpecialSigns + ">> " + MainColor + "Informacje o graczu " + SpecialSigns + "* " + WarningColor + user.getLastName() + SpecialSigns + " <<"));
        ChatUtil.sendMessage(sender, SpecialSigns + "  * " + MainColor + "Zbanowany: " + ImportantColor + ((b != null) ? (((b.getExpireTime() == 0L) ? "permamentnie" : "tymczasowo (wygasa " + Util.getDate(b.getExpireTime()) + ")") + MainColor + ", powod: " + ImportantColor + b.getReason() + MainColor + ", przez: " + ImportantColor + b.getAdmin()) : ChatColor.DARK_RED + "%X%"));
        ChatUtil.sendMessage(sender, SpecialSigns + "  * " + MainColor + "Zbanowany IP: " + ImportantColor + ((bip != null) ? (((bip.getExpireTime() == 0L) ? "permamentnie" : "tymczasowo (wygasa " + Util.getDate(bip.getExpireTime()) + ")") + MainColor + ", powod: " + ImportantColor + bip.getReason() + MainColor + ", przez: " + ImportantColor + bip.getAdmin()) : ChatColor.DARK_RED + "%X%"));
        ChatUtil.sendMessage(sender, Util.replaceString(SpecialSigns + ">> " + MainColor + "Informacje o graczu " + SpecialSigns + "* " + WarningColor + user.getLastName() + SpecialSigns + " <<"));

    }
}
