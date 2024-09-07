package pl.justpvp.bungee.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatUtil {

    public static String fixColor(final String s) {
        if (s == null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', s
                .replace(">>", "\u00BB")
                .replaceAll("Â", "")
                .replace("<<", "\u00AB"))
                .replace("%X%", "✗")
                .replace("%V%", "√")
                .replace("*", "•");
    }
    public static void sendMessage(CommandSender sender, String text){
        sender.sendMessage(new TextComponent(fixColor(text)));
    }
    public static boolean isAlphanumeric(String str)
    {
        char[] charArray = str.toCharArray();
        for(char c:charArray)
        {
            if (!Character.isLetterOrDigit(c))
                return false;
        }
        return true;
    }
}
