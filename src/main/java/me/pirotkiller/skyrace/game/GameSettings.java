package me.pirotkiller.skyrace.game;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;

public class GameSettings implements ConfigurationSerializable {
    public static final GameSettings DEFAULT_SETTING = new GameSettings(2, 10);
    private final int min_players;
    private final int max_players;

    public GameSettings(int min_players, int max_players) {
        this.min_players = min_players;
        this.max_players = max_players;
    }

    public int getMin_players() {
        return min_players;
    }

    public int getMax_players() {
        return max_players;
    }

    public static GameSettings deserialize(Map<String, Object> data) {
        final int min_players = (int) data.get("min-players");
        final int max_players = (int) data.get("max-players");
        
        return new GameSettings(min_players, max_players);
    }

    @Override
    public Map<String, Object> serialize() {
        return Map.of(
                "min-players", min_players,
                "max-players", max_players
        );
    }
}
