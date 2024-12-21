package org.jufyer.plugin.totemNerf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;


public final class Main extends JavaPlugin implements Listener {
  private static Main instance;

  private final HashMap<UUID, Long> cooldowns = new HashMap<>();
  private final HashMap<UUID, Integer> uses = new HashMap<>();

  public static Main getInstance() {
    return instance;
  }

  @Override
  public void onEnable() {
    instance = this;
    Bukkit.getPluginManager().registerEvents(this, this);
  }

  @Override
  public void onDisable(){

  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    ItemStack item = player.getInventory().getItemInMainHand();

    if (item.getType() == Material.TOTEM_OF_UNDYING) {
      UUID playerId = player.getUniqueId();

      // Check cooldown
      if (cooldowns.containsKey(playerId)) {
        long timeLeft = (cooldowns.get(playerId) - System.currentTimeMillis()) / 1000;
        if (timeLeft > 0) {
          player.sendMessage(ChatColor.RED + "You must wait " + timeLeft + " seconds before using another totem.");
          event.setCancelled(true);
          return;
        }
      }

      // Apply effects and penalties
      handleTotemUse(player);
    }
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if (event.getWhoClicked() instanceof Player player) {
      // Limit to one totem in inventory
      int totemCount = 0;
      for (ItemStack item : player.getInventory().getContents()) {
        if (item != null && item.getType() == Material.TOTEM_OF_UNDYING) {
          totemCount++;
          if (totemCount > 1) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You can only carry one Totem of Undying at a time.");
            break;
          }
        }
      }
    }
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    Player player = event.getEntity();
    UUID playerId = player.getUniqueId();

    // Reset cooldown and uses on death
    cooldowns.remove(playerId);
    uses.remove(playerId);
  }

  private void handleTotemUse(Player player) {
    UUID playerId = player.getUniqueId();
    int currentUses = uses.getOrDefault(playerId, 0);

    // Apply penalties
    player.setMaxHealth(Math.max(2.0, player.getMaxHealth() - 2.0)); // Reduce max health by 1 heart
    player.sendMessage(ChatColor.YELLOW + "Your maximum health has been reduced by 1 heart.");

    // Add temporary debuff (e.g., Slowness)
    int debuffDuration = (currentUses + 1) * 20 * 10; // Increases with each use
    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, debuffDuration, 1));
    player.sendMessage(ChatColor.YELLOW + "You are slowed for " + debuffDuration / 20 + " seconds.");

    // Consume additional resources
    consumeResources(player);

    // Set cooldown
    cooldowns.put(playerId, System.currentTimeMillis() + 5 * 60 * 1000); // 5-minute cooldown

    // Increment use count
    uses.put(playerId, currentUses + 1);
  }

  private void consumeResources(Player player) {
    // Consume a golden apple if available
    if (player.getInventory().contains(Material.GOLDEN_APPLE)) {
      player.getInventory().removeItem(new ItemStack(Material.GOLDEN_APPLE, 1));
      player.sendMessage(ChatColor.GOLD + "A golden apple has been consumed.");
    } else {
      player.sendMessage(ChatColor.RED + "You need a golden apple in your inventory to use the totem.");
    }

    // Remove XP levels
    int levelsToRemove = Math.min(10, player.getLevel());
    player.setLevel(player.getLevel() - levelsToRemove);
    player.sendMessage("§c" + levelsToRemove + " XP levels have been removed.");
  }
}
