package org.jufyer.plugin.totemNerf;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionType;
import org.jufyer.plugin.totemNerf.brewing.BrewingControler;
import org.jufyer.plugin.totemNerf.brewing.BrewingRecipe;
import org.jufyer.plugin.totemNerf.commands.GivePotion;
import org.jufyer.plugin.totemNerf.commands.SetMaxTotems;
import org.jufyer.plugin.totemNerf.listeners.PlayerListeners;

import java.io.*;
import java.util.Arrays;

public final class Main extends JavaPlugin{
  private static Main instance;

  public static Main getInstance() {
    return instance;
  }

  public static BrewingControler bc;

  @Override
  public void onEnable() {
    instance = this;
    getServer().getPluginManager().registerEvents(new PlayerListeners(), this);

    getCommand("setMaxTotems").setExecutor(new SetMaxTotems());
    getCommand("givePotion").setExecutor(new GivePotion());

    addBrewingRecipie();
  }

  @Override
  public void onDisable(){

  }

  public static void addBrewingRecipie() {
    ItemStack resetPotion = new ItemStack(Material.POTION);
    resetPotion.setAmount(1);
    ItemMeta meta = resetPotion.getItemMeta();
    meta.setCustomModelData(123);
    meta.setDisplayName("§rPotion of Purification");
    meta.setLore(Arrays.asList("§aSet's your maximum Health back to 10 Hearts."));
    ((PotionMeta) meta).setColor(Color.LIME);

    resetPotion.setItemMeta(meta);

    ItemStack bottle = new ItemStack(Material.POTION, 1);
    PotionMeta pmeta = (PotionMeta) bottle.getItemMeta();
    pmeta.setBasePotionType(PotionType.WATER);
    bottle.setItemMeta(pmeta);

    bc = new BrewingControler(Main.getInstance());
    BrewingRecipe recipe = new BrewingRecipe(
      new NamespacedKey(Main.getInstance(), "customPotion"),
      resetPotion,
      new ItemStack(Material.GOLDEN_APPLE),
      bottle
    );
    //Main.getInstance().getLogger().info(recipe.toString());
    bc.addRecipe(recipe);
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
      maxTotems = 1;
    }

    return maxTotems;
  }
}
