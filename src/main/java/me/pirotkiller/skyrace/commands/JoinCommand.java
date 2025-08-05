package me.pirotkiller.skyrace.commands;

import me.pirotkiller.skyrace.SkyRace;
import me.pirotkiller.skyrace.managers.GameManager;
import me.pirotkiller.skyrace.states.gameStates;
import me.pirotkiller.skyrace.utils.UChat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JoinCommand implements TabExecutor {
    private GameManager gameManager;
    private SkyRace main;
    int lobbyy;
    int gamespawny;
    int finishy;
    int spectatey;

    public JoinCommand(GameManager gameManager, SkyRace main) {
        this.gameManager = gameManager;
        this.main = main;
        this.lobbyy = this.main.getConfig().getInt("gamelobby.y");
        this.gamespawny = this.main.getConfig().getInt("gamespawn.p" + gameManager.playerLimit + ".y");
        this.finishy = this.main.getConfig().getInt("gamefinish.c1.y");
        this.spectatey = this.main.getConfig().getInt("gameSpectate.y");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            // check game is set up or not
            if (lobbyy == 0) {
                p.sendMessage(new UChat().prefix + "Game is not setup by the owner of server.");
                return true;
            }
            if (gamespawny == 0) {
                p.sendMessage(new UChat().prefix + "Game is not setup by the owner of server.");
                return true;
            }
            if (spectatey == 0) {
                p.sendMessage(new UChat().prefix + "Game is not setup by the owner of server.");
                return true;
            }
            if (finishy == 0) {
                p.sendMessage(new UChat().prefix + "Game is not setup by the owner of server.");
                return true;
            }

            if (args.length > 0 && args[0].equals("list")) {
                p.sendMessage(gameManager.alive.toString());
            } else if (args.length > 0 && args[0].equals("staterest")) {
                gameManager.setGameState(gameStates.PRELOBBY);
                gameManager.alive.clear();
                gameManager.vanished.clear();
                gameManager.spectating.clear();
                p.sendMessage("rest");
            } else if (args.length > 0 && args[0].equals("state")) {
                p.sendMessage(String.valueOf(gameManager.getGameState()));
            } else if (gameManager.getGameState() == gameStates.STARTING || gameManager.getGameState() == gameStates.WAITING || gameManager.getGameState() == gameStates.PRELOBBY) {
                if (gameManager.alive.size() <= gameManager.playerLimit) {
                    if (!gameManager.alive.contains(ChatColor.stripColor(p.getDisplayName()))) {
                        gameManager.getPlayerManager().handlePlayerAtWaiting(p);
                    } else {
                        p.sendMessage(new UChat().prefix + "You are already in game.");
                    }
                } else {
                    p.sendMessage(new UChat().prefix + "Lobby is Full.");
                }
            } else {
                p.sendMessage(new UChat().prefix + "Game is in progress!");
            }

        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            arguments.add("list");
            arguments.add("staterest");
            arguments.add("state");
            return arguments;
        }
        return null;
    }
}
