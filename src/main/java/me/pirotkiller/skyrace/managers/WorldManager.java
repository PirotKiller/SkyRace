package me.pirotkiller.skyrace.managers;

import me.pirotkiller.skyrace.SkyRace;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.Configuration;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.bukkit.Bukkit.getServer;

public class WorldManager {
    private SkyRace main;
    private GameManager gameManager;

    private final String gameWorldName;
    private final File sourceFolder;
    private Configuration config;
    private final World gameWorld;
    private Double corner1x;
    private Double corner1y;
    private Double corner1z;
    private Double corner2x;
    private Double corner2y;
    private Double corner2z;

    public WorldManager(SkyRace main, GameManager gameManager) {
        this.gameManager = gameManager;
        this.main = main;
        this.config = main.getConfig();
        this.gameWorldName = config.getString("gameworld");
        this.gameWorld = Bukkit.getWorld(gameWorldName);
        this.sourceFolder = gameWorld.getWorldFolder();
        this.lobbyLocationx = config.getDouble("gamelobby.x");
        this.lobbyLocationy = config.getDouble("gamelobby.y");
        this.lobbyLocationz = config.getDouble("gamelobby.z");
        this.lobbyLocationPitch = config.getDouble("gamelobby.pitch");
        this.lobbyLocationYaw = config.getDouble("gamelobby.yaw");
        this.corner1x = config.getDouble("gamefinish.c1.x");
        this.corner1y = config.getDouble("gamefinish.c1.y");
        this.corner1z = config.getDouble("gamefinish.c1.z");
        this.corner2x = config.getDouble("gamefinish.c2.x");
        this.corner2y = config.getDouble("gamefinish.c2.y");
        this.corner2z = config.getDouble("gamefinish.c2.z");
    }

    public World targetWorld;
    private Location gameSpectateLocation;
    File path;
    double lobbyLocationx;
    double lobbyLocationy;
    double lobbyLocationz;
    double lobbyLocationPitch;
    double lobbyLocationYaw;
    public Location lobbyLocation;
    int max = 100;
    int min = 400;
    int b = (int) (Math.random() * (max - min + 1) + min);
    String newWorld = "world" + String.valueOf(b);
    public Location main_world_location = Bukkit.getWorld("world").getSpawnLocation();

    public void unloadWorld() {
        if (targetWorld != null) {
            getServer().unloadWorld(targetWorld, true);
            deleteWorld(path);
        }
    }

    public boolean deleteWorld(File path) {
        if (path.exists()) {
            File files[] = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    public Configuration getConfig() {
        return config;
    }

    public Location getSpawnLocation(int playerNumber) {
        return new Location(targetWorld,
                config.getDouble("gamespawn.p" + playerNumber + ".x"),
                config.getDouble("gamespawn.p" + playerNumber + ".y"),
                config.getDouble("gamespawn.p" + playerNumber + ".z"),
                (float) config.getDouble("gamespawn.p" + playerNumber + ".yaw"),
                (float) config.getDouble("gamespawn.p" + playerNumber + ".pitch"));
    }

    public void loadWorld() {
        File tag = new File(newWorld);
        copyWorld(sourceFolder, tag);
        new WorldCreator(newWorld).createWorld();
        targetWorld = Bukkit.getWorld(newWorld);
        Bukkit.getWorlds().add(targetWorld);
        path = targetWorld.getWorldFolder();
        lobbyLocation = new Location(targetWorld, lobbyLocationx, lobbyLocationy, lobbyLocationz, (float) lobbyLocationYaw, (float) lobbyLocationPitch);
    }

    public void copyWorld(File source, File targetWorld) {
        try {
            ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!targetWorld.exists())
                        targetWorld.mkdirs();
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(targetWorld, file);
                        copyWorld(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(targetWorld);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {

        }
    }

    public Location getGameSpectateLocation() {
        return new Location(targetWorld,
                config.getDouble("gameSpectate.x"),
                config.getDouble("gameSpectate.y"),
                config.getDouble("gameSpectate.z"),
                (float) config.getDouble("gameSpectate.yaw"),
                (float) config.getDouble("gameSpectate.pitch"));
    }

    public boolean isInsideBorder(Location loc) {
        Location corner1 = new Location(targetWorld, corner1x, corner1y, corner1z);
        Location corner2 = new Location(targetWorld, corner2x, corner2y, corner2z);
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        double minX = Math.min(corner1.getX(), corner2.getX());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        return x >= minX && x <= maxX &&
                y >= minY && y <= maxY &&
                z >= minZ && z <= maxZ;
    }


}
