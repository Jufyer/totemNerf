package org.jufyer.plugin.totemNerf;

import org.bukkit.plugin.java.JavaPlugin;
import org.jufyer.plugin.totemNerf.commands.SetMaxTotems;
import org.jufyer.plugin.totemNerf.listeners.PlayerListeners;

import java.io.*;

public final class Main extends JavaPlugin{
  private static Main instance;

  public static Main getInstance() {
    return instance;
  }

  @Override
  public void onEnable() {
    instance = this;
    getServer().getPluginManager().registerEvents(new PlayerListeners(), this);

    getCommand("setMaxTotems").setExecutor(new SetMaxTotems());
  }

  @Override
  public void onDisable(){

  }

  public static void setMaxTotemsFile(int numberOfTotems) {
    File folder = Main.getInstance().getDataFolder();
    File file = new File(folder, "maxTotems");

    try {
      if (!folder.exists() && !folder.mkdirs()) {
        throw new IOException("Directory couldn't be created: " + folder.getPath());
      }

      if (!file.exists() && !file.createNewFile()) {
        throw new IOException("File couldn't be created: " + file.getPath());
      }

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
        writer.write(String.valueOf(numberOfTotems));
      }

    } catch (IOException e) {
      throw new RuntimeException("Error while writing the data", e);
    }
  }

  public static int getMaxTotems() {
    String data;
    int maxTotems;
    try {
      BufferedReader reader = new BufferedReader(new BufferedReader(new FileReader(new File(Main.getInstance().getDataFolder(), "maxTotems"))));
      try {
        data = reader.readLine();
        maxTotems = Integer.parseInt(data);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    return maxTotems;
  }
}
