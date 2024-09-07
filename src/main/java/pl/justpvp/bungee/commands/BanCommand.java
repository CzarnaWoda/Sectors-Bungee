package pl.justpvp.bungee.commands;

import jdk.nashorn.internal.objects.Global;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang3.StringUtils;
import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.auth.BungeeUser;
import pl.justpvp.bungee.auth.BungeeUserManager;
import pl.justpvp.bungee.data.Ban;
import pl.justpvp.bungee.managers.BanManager;
import pl.justpvp.bungee.packets.chat.GlobalChatMessage;
import pl.justpvp.bungee.redis.client.RedisClient;
import pl.justpvp.bungee.util.ChatUtil;

public class BanCommand extends Command {
    public BanCommand() {
        super("ban", "admin.ban", "createban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        final String usage = "/ban <nick> <reason>";

        if (args.length < 1){
            ChatUtil.sendMessage(sender,"&4Poprawne uzycie: &c" + usage);
            return;
        }
        final BungeeUser user = BungeePlugin.getBungeeUserManager().getUser(args[0]);
        if (user == null){
            ChatUtil.sendMessage(sender, "&4Blad: &cTaki uzytkownik nie istnieje!");
            return;
        }
        if (sender.getName().equalsIgnoreCase(args[0])){
            ChatUtil.sendMessage(sender,"&4Blad: &cNie mozesz zbanowac samego siebie!");
            return;
        }
        final String admin = sender.getName().equals("CONSOLE") ? "Konsola" : sender.getName();
        String reason = "Administrator ma zawsze racje!";
        if (args.length > 1) {
            reason = StringUtils.join(args, " ", 1, args.length);
        }
        final Ban ban = BanManager.getBan(user.getUuid());
        if (ban != null && !ban.isUnban()){
            ChatUtil.sendMessage(sender, "&4Blad: &cTen uzytkownik juz ma bana!");
            return;
        }
        if(ban != null){
            BanManager.deleteBan(ban);
        }
        BanManager.createBan(user.getUuid(), reason, admin, 0L);

        final GlobalChatMessage message = new GlobalChatMessage("&4&lBAN &8->> &7Uzytkownik &4" + user.getLastName() + "&7 zostal &c&lPERMAMETNIE &7zbanowany z powodem: &4" + reason);

        RedisClient.sendProxiesPacket(message);
    }
}
