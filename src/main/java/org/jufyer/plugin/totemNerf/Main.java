package org.jufyer.plugin.totemNerf;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jufyer.plugin.totemNerf.listeners.playerListeners;

public final class Main extends JavaPlugin{
  private static Main instance;

  public static Main getInstance() {
    return instance;
  }

  @Override
  public void onEnable() {
    instance = this;
    Bukkit.getServer().getPluginManager().registerEvents(new playerListeners(), this);
  }

  @Override
  public void onDisable(){

  }
}
