package org.jufyer.plugin.totemNerf.commands;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class GivePotion implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (sender.isOp()){
      ItemStack resetPotion = new ItemStack(Material.POTION);
      resetPotion.setAmount(1);
      ItemMeta meta = resetPotion.getItemMeta();
      meta.setCustomModelData(123);
      meta.setDisplayName("§rPotion of Purification");
      meta.setLore(Arrays.asList("§aSet's your maximum Health back to 10 Hearts."));
      ((PotionMeta) meta).setColor(Color.LIME);


      resetPotion.setItemMeta(meta);

      Player player = (Player) sender;
      player.getInventory().addItem(resetPotion);
    }else {
      sender.sendMessage("§cYou don't have the permission to do this!");
    }
    return false;
  }
}
