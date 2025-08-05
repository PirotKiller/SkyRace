package me.pirotkiller.skyrace.game;

import me.pirotkiller.skyrace.SkyRace;

import java.util.UUID;

public class GameManager {
    private final SkyRace main;

    public GameManager(SkyRace main) {
        this.main = main;
    }

    public Game CreateNewGame(GameSettings settings) {
        final Game game = new Game(GameSettings.DEFAULT_SETTING);
        savetToConfig(game);
        return game;
    }

    public Game createNewGame() {
        final Game game = new Game(GameSettings.DEFAULT_SETTING);
        savetToConfig(game);
        return game;
    }

    private void savetToConfig(Game game) {
        main.getConfig().set("games." + UUID.randomUUID(), game);
        main.saveConfig();
    }
}
