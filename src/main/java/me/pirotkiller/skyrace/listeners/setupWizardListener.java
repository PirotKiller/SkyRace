package me.pirotkiller.skyrace.listeners;

import me.pirotkiller.skyrace.SkyRace;
import me.pirotkiller.skyrace.managers.GameManager;
import me.pirotkiller.skyrace.managers.SetupWizardManger;
import me.pirotkiller.skyrace.states.SetupState;
import me.pirotkiller.skyrace.utils.UChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class setupWizardListener implements Listener {
    private final SetupWizardManger setupWizardManger;
    private SkyRace main;
    private GameManager gameManager;


    private int x;
    private int y;
    private int z;
    private int pitch;
    private int yaw;
    private int playerLimit;
    private int playerNumber = 1;
    private int finishCorner = 1;

    public setupWizardListener(SetupWizardManger setupWizardManger, GameManager gameManager, SkyRace main) {
        this.setupWizardManger = setupWizardManger;
        this.gameManager = gameManager;
        this.playerLimit = gameManager.playerLimit;
        this.main = main;
    }

    @EventHandler
    public void setupListener(PlayerInteractEvent e) {
        if (setupWizardManger.getSetupState() == SetupState.ENABLED) {
            Player p = e.getPlayer();
            if (e.getPlayer().hasPermission("skyrace.admin")) {
                // checks if player is in gameWorld
                if (p.getWorld() != setupWizardManger.getPlayerWorldAfter()) {
                    p.sendMessage(new UChat().prefix + "You are not in your GameWorld");
                    return;
                }
                if (e.getHand() == EquipmentSlot.OFF_HAND || p.getInventory().getItemInMainHand().getItemMeta() == null)
                    return;

                if ((p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Set Finishing Point") && e.getClickedBlock() == null))
                    return;

                if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
                    x = (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Set Finishing Point")) ? e.getClickedBlock().getLocation().getBlockX() : (int) p.getLocation().getX();
                    y = (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Set Finishing Point")) ? e.getClickedBlock().getLocation().getBlockY() : (int) p.getLocation().getY();
                    z = (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Set Finishing Point")) ? e.getClickedBlock().getLocation().getBlockZ() : (int) p.getLocation().getZ();
                    pitch = (int) p.getLocation().getPitch();
                    yaw = (int) p.getLocation().getYaw();

                    if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Set Lobby Spawn")) {
                        if (main.getConfig().getConfigurationSection("gamelobby.x") == null) {
                            main.getConfig().createSection("gamelobby.x");
                        }
                        main.getConfig().set("gamelobby.x", x);
                        if (main.getConfig().getConfigurationSection("gamelobby.y") == null) {
                            main.getConfig().createSection("gamelobby.y");
                        }
                        main.getConfig().set("gamelobby.y", y);
                        if (main.getConfig().getConfigurationSection("gamelobby.z") == null) {
                            main.getConfig().createSection("gamelobby.z");
                        }
                        main.getConfig().set("gamelobby.z", z);
                        if (main.getConfig().getConfigurationSection("gamelobby.pitch") == null) {
                            main.getConfig().createSection("gamelobby.pitch");
                        }
                        main.getConfig().set("gamelobby.pitch", pitch);
                        if (main.getConfig().getConfigurationSection("gamelobby.yaw") == null) {
                            main.getConfig().createSection("gamelobby.yaw");
                        }
                        main.getConfig().set("gamelobby.yaw", yaw);
                        main.saveConfig();
                        p.sendMessage(new UChat().prefix + "Lobby spawn (" + x + "," + y + "," + z + ") successfully added! ");

                    } else if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Set Game Spawn")) {
                        if (playerNumber <= playerLimit) {
                            // used for checking if path exist
//                            if (main.getConfig().getConfigurationSection("gamespawn.x") == null) {
//                                main.getConfig().createSection("gamespawn.x");
//                            }
                            main.getConfig().set("gamespawn.p" + playerNumber + ".x", x);

                            main.getConfig().set("gamespawn.p" + playerNumber + ".y", y);

                            main.getConfig().set("gamespawn.p" + playerNumber + ".z", z);

                            main.getConfig().set("gamespawn.p" + playerNumber + ".z", z);

                            main.getConfig().set("gamespawn.p" + playerNumber + ".pitch", pitch);

                            main.getConfig().set("gamespawn.p" + playerNumber + ".yaw", yaw);
                            main.saveConfig();
                            p.sendMessage(new UChat().prefix + "Game spawn (" + x + "," + y + "," + z + ") for player " + playerNumber + " successfully added! ");
                        } else {
                            playerNumber = 0;
                        }
                        playerNumber += 1;
                    } else if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Set Game Spectate")) {
                        main.getConfig().set("gameSpectate.x", x);

                        main.getConfig().set("gameSpectate.y", y);

                        main.getConfig().set("gameSpectate.z", z);

                        main.getConfig().set("gameSpectate.pitch", pitch);

                        main.getConfig().set("gameSpectate.yaw", yaw);
                        main.saveConfig();
                        p.sendMessage(new UChat().prefix + "Spectate point (" + x + "," + y + "," + z + ") successfully added! ");

                    } else if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Set Finishing Point")) {
                        if (finishCorner <= 2) {
                            main.getConfig().set("gamefinish.c" + finishCorner + ".x", x);

                            main.getConfig().set("gamefinish.c" + finishCorner + ".y", y);

                            main.getConfig().set("gamefinish.c" + finishCorner + ".z", z);
                            main.saveConfig();
                            p.sendMessage(new UChat().prefix + "Corner " + finishCorner + " for Finishing point (" + x + "," + y + "," + z + ") successfully added! ");
                        } else {
                            finishCorner = 0;
                        }
                        finishCorner += 1;
                    }
                }
            } else {
                p.sendMessage(new UChat().permission);
            }
        }
    }
}

