import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
  `java-library`
  id("io.papermc.paperweight.userdev") version "1.7.3"
  id("xyz.jpenilla.run-paper") version "2.3.1" // Adds runServer and runMojangMappedServer tasks for testing
  id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.2.0" // Generates plugin.yml based on the Gradle config
}

group = "org.jufyer.plugin"
version = "1.0.0-SNAPSHOT"
description = "This Plugin applies a Totem  Nerf"

java {
  // Configure the java toolchain. This allows gradle to auto-provision JDK 21 on systems that only have JDK 11 installed for example.
  toolchain.languageVersion = JavaLanguageVersion.of(21)
}

dependencies {
  paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:1.21.4-R0.1-20241215.095037-18")
}

tasks {
  compileJava {
    options.release = 21
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
  }
}

// Configure plugin.yml generation
// - name, version, and description are inherited from the Gradle project.
bukkitPluginYaml {
  main = "org.jufyer.plugin.totemNerf.Main"
  load = BukkitPluginYaml.PluginLoadOrder.STARTUP
  authors.add("Jufyer")
  apiVersion = "1.21.4"
  commands {
    register("setMaxTotems")
    register("givePotion")
  }
}
