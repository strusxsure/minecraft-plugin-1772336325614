package com.stormai.plugin.commands;

import com.stormai.plugin.utils.HealthManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LSCommand implements CommandExecutor {

    private final HealthManager healthManager;

    public LSCommand(HealthManager healthManager) {
        this.healthManager = healthManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§6LifeSteal Commands:");
            sender.sendMessage("§7/lifesteal withdraw <amount> - Convert health to Heart items");
            return true;
        }

        if (args[0].equalsIgnoreCase("withdraw")) {
            if (args.length < 2) {
                sender.sendMessage("§cUsage: /lifesteal withdraw <amount>");
                return true;
            }

            try {
                int amount = Integer.parseInt(args[1]);
                if (amount <= 0) {
                    sender.sendMessage("§cAmount must be positive!");
                    return true;
                }

                double currentHealth = player.getHealth();
                double maxHealth = player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();
                double currentMaxHealth = healthManager.getPlayerMaxHealth(player.getUniqueId());

                // Calculate available health to convert (can't go below 2 hearts)
                double availableHealth = currentHealth - 2.0;
                if (availableHealth <= 0) {
                    sender.sendMessage("§cYou don't have enough health to convert!");
                    return true;
                }

                if (amount > availableHealth / 2.0) {
                    sender.sendMessage("§cYou don't have enough health to convert that amount!");
                    return true;
                }

                // Convert health to Heart items
                for (int i = 0; i < amount; i++) {
                    ItemStack heartItem = HeartItem.createHeartItem();
                    if (player.getInventory().firstEmpty() == -1) {
                        player.getWorld().dropItemNaturally(player.getLocation(), heartItem);
                    } else {
                        player.getInventory().addItem(heartItem);
                    }
                    player.setHealth(player.getHealth() - 2.0);
                }

                sender.sendMessage("§aSuccessfully converted " + amount + " health to Heart items!");
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInvalid amount!");
            }
        }

        return true;
    }
}