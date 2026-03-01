package com.stormai.plugin;

import com.stormai.plugin.commands.LSCommand;
import com.stormai.plugin.listeners.PlayerListener;
import com.stormai.plugin.utils.HealthManager;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private HealthManager healthManager;

    @Override
    public void onEnable() {
        instance = this;

        // Initialize health manager
        this.healthManager = new HealthManager(this);

        // Register events
        getServer().getPluginManager().registerEvents(new PlayerListener(this, healthManager), this);

        // Register commands
        getCommand("lifesteal").setExecutor(new LSCommand(healthManager));

        // Register Heart item recipe
        getServer().addRecipe(HeartItem.getHeartItemRecipe());

        // Load player health data
        healthManager.loadPlayerData();

        getLogger().info("LifeSteal plugin enabled!");
    }

    @Override
    public void onDisable() {
        // Save player health data
        healthManager.savePlayerData();

        getLogger().info("LifeSteal plugin disabled!");
    }

    public static Main getInstance() {
        return instance;
    }
}