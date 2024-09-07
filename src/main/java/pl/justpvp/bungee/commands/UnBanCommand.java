package pl.justpvp.bungee.commands;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import pl.justpvp.bungee.BungeePlugin;
import pl.justpvp.bungee.auth.BungeeUser;
import pl.justpvp.bungee.data.Ban;
import pl.justpvp.bungee.managers.BanManager;
import pl.justpvp.bungee.packets.bans.DeleteBanPacket;
import pl.justpvp.bungee.packets.bans.UnBanPacket;
import pl.justpvp.bungee.packets.chat.GlobalChatMessage;
import pl.justpvp.bungee.redis.client.RedisClient;
import pl.justpvp.bungee.util.ChatUtil;

public class UnBanCommand extends Command {
    public UnBanCommand() {
        super("unban", "admin.unban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final String usage = "/unban <gracz>";

        if (args.length < 1){
            ChatUtil.sendMessage(sender,"&4Poprawne uzycie: &c" + usage);
            return;
        }
        final BungeeUser user = BungeePlugin.getBungeeUserManager().getUser(args[0]);
        if (user == null){
            ChatUtil.sendMessage(sender, "&4Blad: &cTaki uzytkownik nie istnieje!");
            return;
        }
        final Ban ban = BanManager.getBan(user.getUuid());
        if (ban == null){
            ChatUtil.sendMessage(sender, "&4Blad: &cTen uzytkownik nie ma bana!");
            return;
        }
        BanManager.deleteBan(ban);

        final String message = ChatUtil.fixColor("&4&lBAN &8->> &7Uzytkownik &c" + user.getLastName() + " &7zostal odbanowany!");

        final GlobalChatMessage m = new GlobalChatMessage(message);

        RedisClient.sendProxiesPacket(m);

    }
}
