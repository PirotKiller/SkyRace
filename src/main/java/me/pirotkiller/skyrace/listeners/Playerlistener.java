package me.pirotkiller.skyrace.listeners;

import me.pirotkiller.skyrace.SkyRace;
import me.pirotkiller.skyrace.managers.GameManager;
import me.pirotkiller.skyrace.states.gameStates;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Playerlistener implements Listener {
    private GameManager gameManager;
    private SkyRace main;

    public Playerlistener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void getPlayerJoinGame(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!gameManager.alive.contains(ChatColor.stripColor(p.getDisplayName()))) {
            p.teleport(gameManager.getWorldManager().main_world_location);
        } else if (!gameManager.alive.contains(ChatColor.stripColor(p.getDisplayName()))) {
            p.teleport(gameManager.getWorldManager().main_world_location);
        } else if (!gameManager.alive.contains(ChatColor.stripColor(p.getDisplayName()))) {
            p.teleport(gameManager.getWorldManager().main_world_location);
        }
    }

    public SkyRace getMain() {
        return main;
    }

    @EventHandler
    public void getPlayerQuitGame(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (gameManager.alive.contains(ChatColor.stripColor(p.getDisplayName()))) {
            gameManager.alive.remove(ChatColor.stripColor(p.getDisplayName()));
        }
        if (gameManager.spectating.contains(ChatColor.stripColor(p.getDisplayName()))) {
            gameManager.spectating.remove(ChatColor.stripColor(p.getDisplayName()));
        }
        if (gameManager.vanished.contains(ChatColor.stripColor(p.getDisplayName()))) {
            gameManager.vanished.remove(ChatColor.stripColor(p.getDisplayName()));
        }


        if (gameManager.alive.isEmpty()) {
            gameManager.setGameState(gameStates.PRELOBBY);
        } else if (gameManager.alive.size() < gameManager.minPlayerLimit) {
            gameManager.setGameState(gameStates.WAITING);
        }

    }

    @EventHandler
    public void onNearDeathAndDisablePVP(EntityDamageEvent event) {
        if (gameManager.getGameState() == gameStates.WAITING || gameManager.getGameState() == gameStates.STARTING || gameManager.getGameState() == gameStates.READY)
            event.setCancelled(true);
        if (event.getEntity() instanceof Player) {
            if (gameManager.getGameState() == gameStates.ACTIVE) {
                Player player = (Player) event.getEntity();
                double finalHealth = player.getHealth() - event.getFinalDamage();
                EntityDamageEvent.DamageCause damageCause = getDamageCause(event);
                if (event.getEntity().getType() == EntityType.PLAYER && gameManager.getGameState() == gameStates.ACTIVE && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    event.setDamage(0);
                    event.setCancelled(true);
                } else if (finalHealth <= 0) {
                    if (damageCause == EntityDamageEvent.DamageCause.LAVA || damageCause == EntityDamageEvent.DamageCause.FIRE_TICK || damageCause == EntityDamageEvent.DamageCause.FLY_INTO_WALL || damageCause == EntityDamageEvent.DamageCause.FALL || damageCause == EntityDamageEvent.DamageCause.FIRE || damageCause == EntityDamageEvent.DamageCause.SUFFOCATION) {
                        event.setCancelled(true);
                        gameManager.getPlayerManager().handlePlayerOnDeathAtActiveGame(player.getPlayer());
                    }
                }
            }
        }
    }

    private static EntityDamageEvent.DamageCause getDamageCause(EntityDamageEvent event) {
        EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();  //When player fall to death then this return null
        EntityDamageEvent.DamageCause damageCause;                               //##### to counter it we change damageCause to Fall because damageCause cant get null from damageEvent
        if (damageEvent == null)                                                 //#####
            damageCause = EntityDamageEvent.DamageCause.FALL;                    //#####
        else                                                                     //#####
            damageCause = damageEvent.getCause();                                //#####
        return damageCause;
    }

    @EventHandler
    public void playerLeaveGameByInteraction(PlayerInteractEvent e) {
        if (gameManager.getGameState() == gameStates.ACTIVE) return;
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (p.getInventory().getItemInMainHand().getItemMeta() == null)
                return;

            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName() == null)
                return;

            if (Objects.equals(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName(), "Leave")) {
                p.performCommand("skyleave");
            }
        }
    }

    @EventHandler
    public void playerBuildEvent(BlockPlaceEvent e) {
        if (e.getPlayer().getWorld() != gameManager.getWorldManager().targetWorld) return;
        if (gameManager.getGameState() == gameStates.ACTIVE || gameManager.getGameState() == gameStates.PRELOBBY || gameManager.getGameState() == gameStates.WAITING || gameManager.getGameState() == gameStates.STARTING) {
            if (e.getBlockPlaced().getType() != Material.LAVA) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void playerBreakEvent(BlockBreakEvent e) {
        if (e.getPlayer().getWorld() != gameManager.getWorldManager().targetWorld) return;
        if (gameManager.getGameState() == gameStates.ACTIVE || gameManager.getGameState() == gameStates.PRELOBBY || gameManager.getGameState() == gameStates.WAITING || gameManager.getGameState() == gameStates.STARTING) {
            e.setCancelled(true);
        }
    }

    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void disableMovementAndCheckPlayerTouchFinish(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (gameManager.getGameState() == gameStates.READY) {
            e.setCancelled(true);
        }
        if (gameManager.getGameState() == gameStates.ACTIVE) {
            if (gameManager.alive.contains(ChatColor.stripColor(p.getDisplayName()))) {
                if (gameManager.getWorldManager().isInsideBorder(p.getLocation())) {
                    p.teleport(gameManager.getWorldManager().getSpawnLocation(1));
                    p.getInventory().clear();
                    p.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
                    ItemStack rock = new ItemStack(Material.FIREWORK_ROCKET, 2);
                    ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 2);
                    p.getInventory().addItem(rock);
                    p.getInventory().addItem(lava);

                }
            }
        }
    }
}
