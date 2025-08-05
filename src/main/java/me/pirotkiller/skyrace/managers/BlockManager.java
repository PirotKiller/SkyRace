package me.pirotkiller.skyrace.managers;

import org.bukkit.Material;

import java.util.List;

public class BlockManager {
    private List<Material> mineAbleBlocks;
    private GameManager gameManager;

    public BlockManager(GameManager gameManager) {
        this.gameManager = gameManager;
        mineAbleBlocks.add(Material.OBSIDIAN);
    }

    public boolean canBeMined(Material block) {
        return mineAbleBlocks.contains(block);
    }
}
