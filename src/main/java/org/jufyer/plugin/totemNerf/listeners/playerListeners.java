package org.jufyer.plugin.totemNerf.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class playerListeners implements Listener {

  // EventHandler to monitor inventory clicks
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (event.getWhoClicked() instanceof Player) {
      Player player = (Player) event.getWhoClicked();
      ensureSingleTotem(player.getInventory());
    }
  }

  // EventHandler to monitor inventory drags
  @EventHandler
  public void onInventoryDrag(InventoryDragEvent event) {
    if (event.getWhoClicked() instanceof Player) {
      Player player = (Player) event.getWhoClicked();
      ensureSingleTotem(player.getInventory());
    }
  }

  // EventHandler to prevent players from picking up more than one Totem of Undying
  @EventHandler
  public void onPlayerPickup(PlayerPickupItemEvent event) {
    if (event.getItem().getItemStack().getType() == Material.TOTEM_OF_UNDYING) {
      Player player = event.getPlayer();
      if (hasMultipleTotems(player.getInventory())) {
        event.setCancelled(true); // Cancel pickup
      }
    }
  }

  // Method to ensure only one Totem of Undying is present
  private void ensureSingleTotem(Inventory inventory) {
    int totemCount = 0;
    for (ItemStack item : inventory.getContents()) {
      if (item != null && item.getType() == Material.TOTEM_OF_UNDYING) {
        totemCount++;
        if (totemCount > 1) {
          inventory.remove(item);
        }
      }
    }
  }

  // Method to check if the inventory contains multiple Totems
  private boolean hasMultipleTotems(Inventory inventory) {
    int totemCount = 0;
    for (ItemStack item : inventory.getContents()) {
      if (item != null && item.getType() == Material.TOTEM_OF_UNDYING) {
        totemCount++;
      }
    }
    return totemCount > 1;
  }
}
