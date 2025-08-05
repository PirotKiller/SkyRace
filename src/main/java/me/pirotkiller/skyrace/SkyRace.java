package me.pirotkiller.skyrace;

import me.pirotkiller.skyrace.commands.FinishCommand;
import me.pirotkiller.skyrace.commands.JoinCommand;
import me.pirotkiller.skyrace.commands.LeaveCommand;
import me.pirotkiller.skyrace.commands.SetupWizard;
import me.pirotkiller.skyrace.game.Game;
import me.pirotkiller.skyrace.game.GameSettings;
import me.pirotkiller.skyrace.listeners.Playerlistener;
import me.pirotkiller.skyrace.listeners.setupWizardListener;
import me.pirotkiller.skyrace.managers.GameManager;
import me.pirotkiller.skyrace.managers.SetupWizardManger;
import me.pirotkiller.skyrace.states.gameStates;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;


public final class SkyRace extends JavaPlugin {
    private GameManager gameManager;
    private SetupWizardManger setupWizardManger;

    @Override
    public void onLoad() {
        ConfigurationSerialization.registerClass(Game.class);
        ConfigurationSerialization.registerClass(GameSettings.class);

    }

    @Override
    public void onEnable() {
        if (getConfig().getString("gameworld").trim().isEmpty() || Objects.equals(getConfig().getString("gameworld"), "")) {
            getServer().getPluginManager().disablePlugin(this);
        }
        new WorldCreator(getConfig().getString("gameworld")).createWorld();
        this.setupWizardManger = new SetupWizardManger();
//        this.gameManager = new GameManager(this);
        gameManager.setGameState(gameStates.PRELOBBY);
        System.out.println("[SkyRace]" + "Plugin Enabled...");
        getServer().getPluginManager().registerEvents(new Playerlistener(gameManager), this);
        getServer().getPluginManager().registerEvents(new setupWizardListener(setupWizardManger, gameManager, this), this);
        registerCommands();
        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        System.out.println("[SkyRace]" + "Plugin Disabled...");
        gameManager.getWorldManager().unloadWorld();
        gameManager.cleanUP();
    }

    //Commands
    private void registerCommands() {
        getCommand("skyjoin").setExecutor(new JoinCommand(gameManager, this));
        getCommand("skysetup").setExecutor(new SetupWizard(setupWizardManger, this));
        getCommand("skyleave").setExecutor(new LeaveCommand(gameManager, this));
        getCommand("skyend").setExecutor(new FinishCommand(gameManager, this));
    }
}
