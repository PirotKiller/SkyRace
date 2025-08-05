package me.pirotkiller.skyrace.countdowns;

import me.pirotkiller.skyrace.SkyRace;
import me.pirotkiller.skyrace.managers.GameManager;
import me.pirotkiller.skyrace.states.gameStates;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GameFinishCount {
    private SkyRace main;
    private GameManager gameManager;
    public BukkitRunnable br;

    public GameFinishCount(SkyRace main, GameManager gameManager) {
        this.main = main;
        this.gameManager = gameManager;
    }

    public void startCountdown() {
        br = new BukkitRunnable() {
            int number = 10;

            @Override
            public void run() {
                if (number == 0) {
                    Bukkit.broadcastMessage("ggggg");
                    gameManager.setGameState(gameStates.RESTARTING);
                    this.cancel();
                }
                number--;
            }
        };
        br.runTaskTimer(main, 20L, 20L);

    }

}
