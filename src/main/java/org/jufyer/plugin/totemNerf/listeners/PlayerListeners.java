package org.jufyer.plugin.totemNerf.listeners;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jufyer.plugin.totemNerf.Main;

import java.util.Arrays;

public class PlayerListeners implements Listener {
  @EventHandler
  public void onEntityPickupItem(EntityPickupItemEvent event) {
    Item item = event.getItem();
    if (event.getEntity() instanceof Player) {
      Player player = (Player) event.getEntity();

      if (item.getItemStack().getType().equals(Material.TOTEM_OF_UNDYING)) {
        int maxTotems = Main.getMaxTotems();
        int totalTotems = countTotems(player);

        if (totalTotems >= maxTotems) {
          player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cYou can only have " + maxTotems + " Totem(s) of Undying in your inventory!"));
          event.setCancelled(true);
        }
      }
    }
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    ItemStack itemStack = event.getCurrentItem();
    Inventory inv = event.getClickedInventory();

    if (event.getWhoClicked() instanceof Player) {
      Player player = (Player) event.getWhoClicked();

      if (itemStack != null && itemStack.getType().equals(Material.TOTEM_OF_UNDYING)) {
        int maxTotems = Main.getMaxTotems();
        int totalTotems = countTotems(player);

        if (totalTotems >= maxTotems) {
          if (!inv.getHolder().equals(player)) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cYou can only have " + maxTotems + " Totem(s) of Undying in your inventory!"));
            event.setCancelled(true);
          }
        }
      }
    }
  }

  @EventHandler
  public void onInventoryDrag(InventoryDragEvent event) {
    if (event.getWhoClicked() instanceof Player) {
      Player player = (Player) event.getWhoClicked();
      ItemStack itemStack = event.getCursor();
      if (itemStack.getType().equals(Material.TOTEM_OF_UNDYING)) {
        int maxTotems = Main.getMaxTotems();
        int totalTotems = countTotems(player);

        if (totalTotems >= maxTotems) {
          player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cYou can only have " + maxTotems + " Totem(s) of Undying in your inventory!"));
          event.setCancelled(true);
        }
      }
    }
  }

  @EventHandler
  public void onInventoryMoveItem(InventoryMoveItemEvent event) {
    if (event.getDestination().getHolder() instanceof Player) {
      Player player = (Player) event.getDestination().getHolder();
      ItemStack itemStack = event.getItem();
      if (itemStack.getType().equals(Material.TOTEM_OF_UNDYING)) {
        int maxTotems = Main.getMaxTotems();
        int totalTotems = countTotems(player);

        if (totalTotems >= maxTotems) {
          player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cYou can only have " + maxTotems + " Totem(s) of Undying in your inventory!"));
          event.setCancelled(true);
        }
      }
    }
  }

  private int countTotems(Player player) {
    int count = 0;
    for (ItemStack item : player.getInventory().getContents()) {
      if (item != null && item.getType().equals(Material.TOTEM_OF_UNDYING)) {
        count += item.getAmount();
      }
    }
    return count;
  }

  @EventHandler
  public void onEntityResurrect(EntityResurrectEvent event) {
    if (event.getEntity() instanceof Player){
      Player player = ((Player) event.getEntity()).getPlayer();
      if (player.getInventory().contains(Material.GOLDEN_APPLE)){
        double currentMaxHealth = player.getAttribute(Attribute.MAX_HEALTH).getBaseValue();
        player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(currentMaxHealth - 2);

        player.sendMessage("§cYour max health has been reduced by 1 due to the use of Totem of Undying.");
      }else {
        player.sendMessage("§cYour haven't had a Golden Apple in your Inventory so your Totem of Undying din't worked!");
        event.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
    ItemStack itemStack = event.getItem();
    if (itemStack.getType().equals(Material.POTION)){
      if (itemStack.getItemMeta().getCustomModelData() == 123){
        if (itemStack.getItemMeta().getDisplayName().equals("Potion of Purification")){
          ItemMeta meta = new ItemStack(Material.POTION).getItemMeta();
          meta.setCustomModelData(123);
          meta.setDisplayName("§rPotion of Purification");
          meta.setLore(Arrays.asList("§aSet's your maximum Health back to 10 Hearts."));
          ((PotionMeta) meta).setColor(Color.LIME);
          if (itemStack.getItemMeta().equals(meta)) {
            Player player = event.getPlayer();
            player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(20);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40,  10));
          }
        }
      }
    }
  }
}
