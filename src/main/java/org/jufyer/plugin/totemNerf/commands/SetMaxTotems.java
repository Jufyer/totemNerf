package org.jufyer.plugin.totemNerf.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jufyer.plugin.totemNerf.Main;

public class SetMaxTotems implements CommandExecutor {
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (sender.isOp()){
      if (args.length == 1){
        int maxTotems = Integer.parseInt(args[0]);
        Main.setMaxTotemsFile(maxTotems);
        sender.sendMessage("§cYou set the maximum Amount of Totems a Player can have at the same time in their inventory to " + maxTotems);
      }
    }

    return false;
  }
}
