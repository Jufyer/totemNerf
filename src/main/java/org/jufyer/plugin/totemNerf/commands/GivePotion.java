package org.jufyer.plugin.totemNerf.commands;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class GivePotion implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    ItemStack resetPotion = new ItemStack(Material.POTION);
    resetPotion.setAmount(1);
    ItemMeta meta = resetPotion.getItemMeta();
    meta.setCustomModelData(123);
    meta.setDisplayName("Potion of Purification");
    resetPotion.setItemMeta(meta);

    Player player = (Player) sender;
    player.getInventory().addItem(resetPotion);

    return false;
  }
}
