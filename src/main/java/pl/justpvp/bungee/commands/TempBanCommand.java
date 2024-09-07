package pl.justpvp.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.lang3.StringUtils;
import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.auth.BungeeUser;
import pl.justpvp.bungee.data.Ban;
import pl.justpvp.bungee.managers.BanManager;
import pl.justpvp.bungee.packets.bans.CreateBanPacket;
import pl.justpvp.bungee.packets.chat.GlobalChatMessage;
import pl.justpvp.bungee.redis.client.RedisClient;
import pl.justpvp.bungee.util.ChatUtil;
import pl.justpvp.bungee.util.Util;

public class TempBanCommand extends Command {
    public TempBanCommand(){
        super("tempban", "admin.tempban");
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        final String usage = "/tempban <gracz> <czas> [powod]";

        if(args.length < 2){
            ChatUtil.sendMessage(sender, "&4Poprawne uzycie: &c" + usage);
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
        final Ban ban = BanManager.getBan(user.getUuid());
        if (ban != null && !ban.isUnban()){
            ChatUtil.sendMessage(sender, "&4Blad: &cTen uzytkownik juz ma bana!");
            return;
        }
        if(ban != null){
            BanManager.deleteBan(ban);
        }
        final String admin = sender.getName().equals("CONSOLE") ? "Console" : sender.getName();
        String reason = "Administrator ma zawsze racje!";
        reason = StringUtils.join(args, " ", 2, args.length);
        final long time = Util.parseDateDiff(args[1], true);

        BanManager.createBan(user.getUuid(), reason, admin, time);

        final GlobalChatMessage message = new GlobalChatMessage("&4&lBAN &8->> &7Uzytkownik &4" + user.getLastName() + "&7 zostal &7zbanowany z powodem: &4" + reason + "&7 do dnia: &4" + Util.getDate(time));

        RedisClient.sendProxiesPacket(message);
    }
}
