package me.pirotkiller.skyrace.managers;

import me.pirotkiller.skyrace.SkyRace;
import org.bukkit.configuration.Configuration;

public class ConfigManager {
    private SkyRace main;
    private LobbyManager lobbyManager;

    public ConfigManager(SkyRace main, LobbyManager lobbyManager) {
        this.main = main;
        this.lobbyManager = lobbyManager;
    }

    private Configuration getConfig() {
        return main.getConfig();
    }

}
