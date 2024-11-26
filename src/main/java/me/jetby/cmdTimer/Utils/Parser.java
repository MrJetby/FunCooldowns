package me.jetby.cmdTimer.Utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");

    public static String color(String text) {
        if (text == null) {
            return "Error processing the message";
        }
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String colorCode = text.substring(matcher.start() + 1, matcher.end());
            text = text.replace(matcher.group(), String.valueOf(ChatColor.of((String)colorCode)));
            matcher = pattern.matcher(text);
        }
        return ChatColor.translateAlternateColorCodes((char)'&', (String)text);
    }

}
