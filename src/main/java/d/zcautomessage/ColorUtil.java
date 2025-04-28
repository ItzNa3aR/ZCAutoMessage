package d.zcautomessage;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtil {
    private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]{6})");

    public static String color(String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hexCode = matcher.group(1);
            ChatColor color = ChatColor.of("#" + hexCode);
            matcher.appendReplacement(buffer, color.toString());
        }
        matcher.appendTail(buffer);

        String n = buffer.toString().replace('&', '§');
        return ChatColor.translateAlternateColorCodes('&', n);
    }
}
