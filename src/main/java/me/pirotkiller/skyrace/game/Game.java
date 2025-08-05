package me.pirotkiller.skyrace.game;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Game implements ConfigurationSerializable {
    private final GameSettings gameSettings;
    private final Set<GamePlayers> players = new HashSet<>();

    Game(GameSettings settings) {
        this.gameSettings = settings;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public Set<GamePlayers> getPlayers() {
        return players;
    }

    @Override
    public Map<String, Object> serialize() {
        return Map.of("game-settings", gameSettings);
    }
}
