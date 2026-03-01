package com.stormai.plugin.listeners;

import com.stormai.plugin.items.HeartItem;
import com.stormai.plugin.utils.HealthManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HeartItemListener implements Listener {

    private final HealthManager healthManager;

    public HeartItemListener(HealthManager healthManager) {
        this.healthManager = healthManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().name().contains("RIGHT_CLICK")) {
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();

            if (HeartItem.isHeartItem(item)) {
                event.setCancelled(true);

                double currentMaxHealth = player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();
                if (currentMaxHealth >= 40.0) {
                    player.sendMessage("§cYou already have maximum health!");
                    return;
                }

                // Consume the Heart item
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }

                // Increase max health by 1 heart (2 HP)
                player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(currentMaxHealth + 2.0);
                healthManager.setPlayerMaxHealth(player.getUniqueId(), currentMaxHealth + 2.0);

                player.sendMessage("§aYou consumed a Heart and gained +1 max health!");
            }
        }
    }
}