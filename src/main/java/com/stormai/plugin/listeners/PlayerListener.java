package com.stormai.plugin.listeners;

import com.stormai.plugin.Main;
import com.stormai.plugin.utils.HealthManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private final Main plugin;
    private final HealthManager healthManager;

    public PlayerListener(Main plugin, HealthManager healthManager) {
        this.plugin = plugin;
        this.healthManager = healthManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        double maxHealth = healthManager.getPlayerMaxHealth(player.getUniqueId());

        // Set player's max health to saved value
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        player.setHealth(Math.min(player.getHealth(), maxHealth));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Data is saved on disable, but we can save here too if needed
        healthManager.savePlayerData();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        double currentMaxHealth = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        // Check if player died at minimum health (elimination)
        if (currentMaxHealth <= 2.0) {
            Bukkit.broadcastMessage("§c" + victim.getName() + " has been eliminated for reaching minimum health!");
            victim.setGameMode(org.bukkit.GameMode.SPECTATOR);
            return;
        }

        // Find killer
        Entity killerEntity = victim.getKiller();
        if (killerEntity instanceof Player killer) {
            double killerMaxHealth = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            double victimMaxHealth = currentMaxHealth;

            // Transfer health: killer gains 1 heart, victim loses 1 heart
            if (killerMaxHealth < 40.0) {
                killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(killerMaxHealth + 2.0);
                healthManager.setPlayerMaxHealth(killer.getUniqueId(), killerMaxHealth + 2.0);
            }

            if (victimMaxHealth > 2.0) {
                victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(victimMaxHealth - 2.0);
                healthManager.setPlayerMaxHealth(victim.getUniqueId(), victimMaxHealth - 2.0);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            double currentHealth = player.getHealth();
            double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

            // Prevent health from going below minimum
            if (currentHealth - event.getFinalDamage() < 2.0 && maxHealth <= 2.0) {
                event.setDamage(currentHealth - 2.0);
            }
        }
    }
}