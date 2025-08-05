package me.pirotkiller.skyrace.commands;

import me.pirotkiller.skyrace.SkyRace;
import me.pirotkiller.skyrace.managers.GameManager;
import me.pirotkiller.skyrace.states.gameStates;
import me.pirotkiller.skyrace.utils.UChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FinishCommand implements CommandExecutor {
    private SkyRace main;
    private GameManager gameManager;

    public FinishCommand(GameManager gameManager, SkyRace main) {
        this.main = main;
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;
        Player p = (Player) sender;
        if (gameManager.getGameState() == gameStates.ACTIVE) {
            gameManager.setGameState(gameStates.FINISH);
        } else {
            p.sendMessage(new UChat().prefix + "Game has not started yet!!");
        }
        return false;
    }
}
