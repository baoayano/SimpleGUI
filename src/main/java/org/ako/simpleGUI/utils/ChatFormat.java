package org.ako.simpleGUI.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class ChatFormat {
    public static String convert(Player p, String s) {
        return PlaceholderAPI.setPlaceholders(p, s.replaceAll("&", "§"));
    }

    public static String convertOnlyColor(String s) {
        return s.replaceAll("&", "§");
    }
}
