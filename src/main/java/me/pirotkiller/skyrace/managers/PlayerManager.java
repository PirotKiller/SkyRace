package me.pirotkiller.skyrace.managers;

import me.pirotkiller.skyrace.SkyRace;
import me.pirotkiller.skyrace.states.gameStates;
import me.pirotkiller.skyrace.utils.UChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;


public class PlayerManager {
    private GameManager gameManager;
    private final SkyRace main;

    public PlayerManager(GameManager gameManager, SkyRace main) {
        this.gameManager = gameManager;
        this.main = main;
    }

    ItemStack leave = new ItemStack(Material.BARRIER);
    ItemMeta leave_meta = leave.getItemMeta();

    public void handlePlayerAtWaiting(Player player) {
        leave_meta.setDisplayName("Leave");
        leave.setItemMeta(leave_meta);
        gameManager.alive.add(ChatColor.stripColor(player.getDisplayName()));
        player.teleport(gameManager.getWorldManager().lobbyLocation);
        player.getInventory().clear();
        player.setExp(0);
        player.setTotalExperience(0);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getInventory().setItem(8, leave);
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.sendMessage(new UChat().prefix + "Welcome to the SkyRace!");
        for (String s :
                gameManager.alive) {
            Bukkit.getPlayer(ChatColor.stripColor(s)).sendMessage(new UChat().prefix + player.getDisplayName() + " has joined the SkyRace.");
        }

        if (gameManager.alive.size() == gameManager.minPlayerLimit) {
            gameManager.setGameState(gameStates.STARTING);
        } else if (gameManager.alive.size() < gameManager.minPlayerLimit) {
            gameManager.setGameState(gameStates.WAITING);
        }
    }

    public void handlePlayerOn5Sec(Player player, int playerNumber) {
        player.teleport(gameManager.getWorldManager().getSpawnLocation(playerNumber)); // spawn location
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.clear();
        playerInventory.setChestplate(new ItemStack(Material.ELYTRA));
        playerInventory.setItem(0, new ItemStack(Material.FIREWORK_ROCKET, 2));
        playerInventory.setItem(1, new ItemStack(Material.LAVA_BUCKET, 1));
        playerInventory.setItem(2, new ItemStack(Material.LAVA_BUCKET, 1));
    }

    public void handlePlayerOnDeathAtActiveGame(Player player) {
        for (String s :
                gameManager.alive) {
            Bukkit.getPlayer(ChatColor.stripColor(s)).sendMessage(new UChat().prefix + player.getDisplayName() + " has been eliminated!");
        }
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.getInventory().clear();
        player.setAllowFlight(true);
        player.setGameMode(GameMode.SPECTATOR);
//        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 50000, 2));
        gameManager.alive.remove(ChatColor.stripColor(player.getDisplayName()));
        gameManager.spectating.add(ChatColor.stripColor(player.getDisplayName()));
        player.teleport(gameManager.getWorldManager().getGameSpectateLocation());
        if (gameManager.alive.size() == 1) {
            gameManager.spectating.add(gameManager.alive.get(0));
            gameManager.setGameState(gameStates.FINISH);
            gameManager.getWinner().setGameMode(GameMode.CREATIVE);
            gameManager.getWinner().setAllowFlight(true);
            gameManager.alive.remove(ChatColor.stripColor(gameManager.getWinner().getDisplayName()));
        }
//        disablePlayerCollision(player);
//        for (String s :
//                gameManager.alive) {
//            player.hidePlayer(Bukkit.getPlayerExact(ChatColor.stripColor(s)));
//        }

    }

    public void handlePlayersOnGameEnd() {
        for (String s : gameManager.spectating) {
            Bukkit.getPlayerExact(s).teleport(gameManager.getWorldManager().lobbyLocation);
            Bukkit.getPlayerExact(s).getInventory().clear();
            Bukkit.getPlayerExact(s).sendMessage(new UChat().prefix + new UChat()
                    .format("&c" + gameManager.getWinner().getDisplayName() + " &eHas won the game"));
        }

    }

    public void handlePlayersOnRestarting() {
        for (String s : gameManager.spectating) {
            Bukkit.getPlayerExact(s).teleport(Bukkit.getWorld("world").getSpawnLocation());
            Bukkit.getPlayerExact(s).setGameMode(GameMode.SURVIVAL);
        }
    }

//    public void disablePlayerCollision(Player player) {
//        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
//        Team team = scoreboard.getTeam("noCollision");
//
//        if (team == null) {
//            team = scoreboard.registerNewTeam("noCollision");
//            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
//            team.setCanSeeFriendlyInvisibles(true);
//            team.setDisplayName("No Collision");
//        }
//
//        if (!team.hasEntry(player.getName())) {
//            team.addEntry(player.getName());
//        }
//    }

}
