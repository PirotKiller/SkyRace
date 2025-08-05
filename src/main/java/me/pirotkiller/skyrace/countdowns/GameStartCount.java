package me.pirotkiller.skyrace.countdowns;

import me.pirotkiller.skyrace.SkyRace;
import me.pirotkiller.skyrace.managers.GameManager;
import me.pirotkiller.skyrace.states.gameStates;
import me.pirotkiller.skyrace.utils.UChat;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStartCount {
    private SkyRace main;
    private GameManager gameManager;
    public BukkitRunnable br;

    public GameStartCount(SkyRace main, GameManager gameManager) {
        this.main = main;
        this.gameManager = gameManager;
    }

    public void startCountdown(Player player, int playerPosition) {
        br = new BukkitRunnable() {
            int number = 10;

            @Override
            public void run() {
                if (number > 0) {
                    switch (number) {
                        case 10, 4, 3, 2, 1 ->
                                player.sendMessage(new UChat().prefix + "Game starting in " + number + " seconds.");
                        case 5 -> {
                            player.sendMessage(new UChat().prefix + "Game starting in 5 second.");
                            gameManager.getPlayerManager().handlePlayerOn5Sec(player, playerPosition);
                        }
                    }
                    number--;
                } else {
                    player.sendMessage(new UChat().prefix + "The game has now started!");
                    gameManager.setGameState(gameStates.ACTIVE);
                    this.cancel();
                }
            }
        };
        br.runTaskTimer(main, 20L, 20L);

    }


}
