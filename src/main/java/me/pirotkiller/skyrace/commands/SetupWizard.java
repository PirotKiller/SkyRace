package me.pirotkiller.skyrace.commands;

import me.pirotkiller.skyrace.SkyRace;
import me.pirotkiller.skyrace.managers.SetupWizardManger;
import me.pirotkiller.skyrace.states.SetupState;
import me.pirotkiller.skyrace.utils.UChat;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SetupWizard implements TabExecutor {
    private SetupWizardManger setupWizardManger;
    private SkyRace main;
    World gameWorld;

    public SetupWizard(SetupWizardManger setupWizardManger, SkyRace main) {
        this.setupWizardManger = setupWizardManger;
        this.main = main;
        this.gameWorld = Bukkit.getWorld(main.getConfig().getString("gameworld"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("skyrace.setup")) {
                if (args.length == 1 && args[0].toString().equals("finish")) {
                    if (setupWizardManger.getSetupState() == SetupState.DISABLED) {
                        p.sendMessage(new UChat().prefix + "You are not in Setup Mode! Do /skysetup to setup SkyRace.");
                    } else if (main.getConfig().getInt("gamelobby.x") == 0) {
                        p.sendMessage(new UChat().prefix + "You have to set Coordinates First!");
                    } else if (main.getConfig().getInt("gamespawn.p1.y") == 0) {
                        p.sendMessage(new UChat().prefix + "You have to set Coordinates First!");
                    } else if (main.getConfig().getInt("gameSpectate.y") == 0) {
                        p.sendMessage(new UChat().prefix + "You have to set Coordinates First!");
                    } else if (main.getConfig().getInt("gamefinish.c1.y") == 0) {
                        p.sendMessage(new UChat().prefix + "You have to set Coordinates First!");
                    } else {
                        p.teleport(Bukkit.getWorld("world").getSpawnLocation());
                        p.sendMessage(new UChat().prefix + "Setup finished!");
                        p.sendMessage(new UChat().prefix + "Now player can join game and play using /skyjoin");
                        p.getInventory().clear();
                        setupWizardManger.setSetupState(SetupState.DISABLED);
                    }
                } else {
                    if (setupWizardManger.getSetupState() == SetupState.ENABLED) {
                        p.sendMessage(new UChat().prefix + "You are already in Setup Mode! ");
                        return true;
                    }
                    p.sendMessage(new UChat().prefix + "Hold the stick and right click to set Locations, And Make sure any y coordinate should not be 0(zero).");
                    setupWizardManger.setSetupState(SetupState.ENABLED);
                    p.teleport(gameWorld.getSpawnLocation());
                    // sets player world gameworld
                    setupWizardManger.setPlayerWorldAfter(gameWorld);
                    p.getInventory().clear();
                    p.setGameMode(GameMode.CREATIVE);
                    ItemStack setLobby = new ItemStack(Material.STICK);
                    ItemMeta setLobbyMeta = setLobby.getItemMeta();
                    setLobbyMeta.setDisplayName("Set Lobby Spawn");
                    setLobby.setItemMeta(setLobbyMeta);

                    ItemStack setGameSpawn = new ItemStack(Material.STICK);
                    ItemMeta setGameSpawnMeta = setGameSpawn.getItemMeta();
                    setGameSpawnMeta.setDisplayName("Set Game Spawn");
                    setGameSpawn.setItemMeta(setGameSpawnMeta);

                    ItemStack setGameSpectate = new ItemStack(Material.STICK);
                    ItemMeta setGameSpectateMeta = setGameSpectate.getItemMeta();
                    setGameSpectateMeta.setDisplayName("Set Game Spectate");
                    setGameSpectate.setItemMeta(setGameSpectateMeta);

                    ItemStack setFinish = new ItemStack(Material.STICK);
                    ItemMeta setFinishMeta = setFinish.getItemMeta();
                    setFinishMeta.setDisplayName("Set Finishing Point");
                    setFinish.setItemMeta(setFinishMeta);

                    p.getInventory().setItem(0, setLobby);
                    p.getInventory().setItem(1, setGameSpawn);
                    p.getInventory().setItem(2, setGameSpectate);
                    p.getInventory().setItem(3, setFinish);
                }
            } else {
                p.sendMessage(new UChat().permission);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            arguments.add("finish");
            return arguments;
        }
        return null;
    }
}
