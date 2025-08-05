package me.pirotkiller.skyrace.managers;

import me.pirotkiller.skyrace.SkyRace;
import me.pirotkiller.skyrace.countdowns.GameFinishCount;
import me.pirotkiller.skyrace.countdowns.GameStartCount;
import me.pirotkiller.skyrace.states.gameStates;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public class GameManager {
    private final WorldManager worldManager;
    private PlayerManager playerManager;
    private SkyRace main;
    private final BlockManager blockManager;

    public int playerLimit;
    public int minPlayerLimit;
    private gameStates gameState = gameStates.WAITING;
    private GameStartCount gameStartCount;
    private GameFinishCount gameFinishCount;
    private Player Winner;

    public SkyRace getMain1() {
        return main;
    }

    public Player getWinner() {
        return Winner;
    }

    public GameManager(SkyRace main) {
        this.main = main;
        this.blockManager = new BlockManager(this);
        this.worldManager = new WorldManager(getMain1(), this);
        this.playerLimit = main.getConfig().getInt("player-limit");
        this.minPlayerLimit = main.getConfig().getInt("min-players");
        this.playerManager = new PlayerManager(this, getMain1());
        this.gameFinishCount = new GameFinishCount(getMain1(), this);
        worldManager.loadWorld();
    }


    public ArrayList<String> alive = new ArrayList<>();
    public ArrayList<String> spectating = new ArrayList<>();
    public ArrayList<String> vanished = new ArrayList<>();

    public gameStates getGameState() {
        return gameState;
    }

    public void setGameState(gameStates gameState) {
        if (this.gameState == gameStates.STARTING && gameState != gameStates.ACTIVE) return;
        if (this.gameState == gameStates.ACTIVE && gameState != gameStates.FINISH) return;
        if (this.gameState == gameStates.FINISH && gameState != gameStates.RESTARTING) return;
        if (this.gameState == gameState) return;
        this.gameState = gameState;
        switch (this.gameState) {
            case WAITING:
                handleOnWaiting();
                break;
            case STARTING:
                handleOnStarting();
                break;
            case ACTIVE:
                handleOnActive();
                break;
            case RESTARTING:
                handleOnGameRestarting();
                break;
            case FINISH:
                handleOnGameEnd();
                break;
        }
    }

    public void cleanUP() {
        if (alive != null) {
            alive.clear();
        }
        if (spectating != null) {
            spectating.clear();
        }
        if (vanished != null) {
            vanished.clear();
        }
    }

    private void handleOnWaiting() {

    }

    private void handleOnStarting() {
        int a = 0;
        gameStartCount = new GameStartCount(getMain1(), this);
        for (String s :
                alive) {
            a += 1;
            gameStartCount.startCountdown(Bukkit.getPlayer(ChatColor.stripColor(s)), a);
        }
    }

    private void handleOnActive() {

    }

    private void handleOnGameEnd() {
        Winner = Bukkit.getPlayerExact(ChatColor.stripColor(alive.get(0)));
        playerManager.handlePlayersOnGameEnd();
        gameFinishCount.startCountdown();
    }

    private void handleOnGameRestarting() {
        getPlayerManager().handlePlayersOnRestarting();
        worldManager.unloadWorld();
        worldManager.loadWorld();
        cleanUP();
        setGameState(gameStates.PRELOBBY);
        Winner = null;
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }
}
