package com.stormai.plugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HealthManager {

    private final Plugin plugin;
    private final File dataFile;
    private final FileConfiguration dataConfig;
    private final Map<UUID, Double> playerHealths = new HashMap<>();

    public HealthManager(Plugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "player_healths.yml");

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        loadPlayerData();
    }

    public void setPlayerMaxHealth(UUID uuid, double maxHealth) {
        playerHealths.put(uuid, maxHealth);
    }

    public double getPlayerMaxHealth(UUID uuid) {
        return playerHealths.getOrDefault(uuid, 20.0);
    }

    public void loadPlayerData() {
        for (String uuidStr : dataConfig.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(uuidStr);
                double maxHealth = dataConfig.getDouble(uuidStr, 20.0);
                playerHealths.put(uuid, maxHealth);
            } catch (IllegalArgumentException e) {
                // Invalid UUID format
            }
        }
    }

    public void savePlayerData() {
        for (Map.Entry<UUID, Double> entry : playerHealths.entrySet()) {
            dataConfig.set(entry.getKey().toString(), entry.getValue());
        }

        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerDataAsync() {
        new BukkitRunnable() {
            @Override
            public void run() {
                savePlayerData();
            }
        }.runTaskAsynchronously(plugin);
    }
}