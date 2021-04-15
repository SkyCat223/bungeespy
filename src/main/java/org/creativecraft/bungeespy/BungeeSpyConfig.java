package org.creativecraft.bungeespy;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class BungeeSpyConfig {
    private final BungeeSpy plugin;
    private Configuration config;

    public BungeeSpyConfig(BungeeSpy plugin) {
        this.plugin = plugin;
    }

    /**
     * Retrieve the plugin configuration.
     */
    public Configuration getConfig() {
        return this.config;
    }

    /**
     * Load the plugin configuration.
     */
    public void loadConfig() {
        if (!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdir();
        }

        File file = new File(this.plugin.getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = this.plugin.getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            this.config = getProvider().load(
                new File(this.plugin.getDataFolder(), "config.yml")
            );
        } catch (Exception e) {
            //
        }
    }

    /**
     * Save the plugin configuration.
     */
    public void saveConfig() {
        try {
            getProvider().save(this.config, new File(this.plugin.getDataFolder(), "config.yml"));
        } catch (Exception e) {
            //
        }
    }

    /**
     * Retrieve the configuration provider.
     */
    public ConfigurationProvider getProvider() {
        return ConfigurationProvider.getProvider(YamlConfiguration.class);
    }
}
