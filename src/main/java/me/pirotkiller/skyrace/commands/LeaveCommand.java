package me.pirotkiller.skyrace.commands;

import me.pirotkiller.skyrace.SkyRace;
import me.pirotkiller.skyrace.managers.GameManager;
import me.pirotkiller.skyrace.utils.UChat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class LeaveCommand implements CommandExecutor {
    private GameManager gameManager;
    private SkyRace main;

    public LeaveCommand(GameManager gameManager, SkyRace main) {
        this.gameManager = gameManager;
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (gameManager.alive.contains(ChatColor.stripColor(p.getDisplayName()))) {
                gameManager.alive.remove(ChatColor.stripColor(p.getDisplayName()));
            } else if (gameManager.spectating.contains(ChatColor.stripColor(p.getDisplayName()))) {
                gameManager.spectating.remove(ChatColor.stripColor(p.getDisplayName()));
            } else if (gameManager.vanished.contains(ChatColor.stripColor(p.getDisplayName()))) {
                gameManager.vanished.remove(ChatColor.stripColor(p.getDisplayName()));
            } else {
                p.sendMessage(new UChat().prefix + "You are not in game!");
                return true;
            }
            p.removePotionEffect(PotionEffectType.INVISIBILITY);
            p.setAllowFlight(false);
            p.getInventory().clear();
            p.setHealth(20);
            p.setSaturation(20);
            p.sendMessage(new UChat().prefix + "You left the game!");
            p.teleport(gameManager.getWorldManager().main_world_location); // send Player back to main World
        }


        return true;
    }
}
