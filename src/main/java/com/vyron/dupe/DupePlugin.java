package com.vyron.dupe;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class DupePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("DupePlugin enabled. /dupe is restricted to " +
                getConfig().getString("allowed-name", "VyronYT"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("dupe")) return false;

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use /dupe.");
            return true;
        }

        String allowedName = getConfig().getString("allowed-name", "VyronYT");
        String allowedUuid = getConfig().getString("allowed-uuid", "");

        boolean nameOk = player.getName().equals(allowedName);
        boolean uuidOk = allowedUuid == null || allowedUuid.isBlank()
                || player.getUniqueId().toString().equalsIgnoreCase(allowedUuid.trim());

        if (!nameOk || !uuidOk) {
            player.sendMessage("§cYou are not allowed to use this command.");
            return true;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand.getType().isAir()) {
            player.sendMessage("§cHold an item in your main hand first.");
            return true;
        }

        // Give a full copy of what's in hand = doubled.
        Map<Integer, ItemStack> leftover = player.getInventory().addItem(hand.clone());
        // If inventory is full, drop the rest at your feet.
        for (ItemStack extra : leftover.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), extra);
        }

        player.sendMessage("§aItem duplicated!");
        return true;
    }
}
