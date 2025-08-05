package me.pirotkiller.skyrace.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UChat {
    public String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String permission = format("&cYou do not have permission to access this command.");
    public String prefix = format("&8[&bSkyRace&8] &e");

    public void sendMessage(Player player, String message) {
        player.sendMessage(prefix + format(message));
    }


}
