package org.creativecraft.bungeespy;

import co.aikar.commands.BungeeCommandManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.creativecraft.bungeespy.commands.BungeeSpyCommand;

import java.util.UUID;

public final class BungeeSpy extends Plugin {
    private BungeeSpyConfig config;

    @Override
    public void onEnable() {
        registerConfig();
        registerCommands();
        registerListener();
    }

    /**
     * Register the event listener.
     */
    public void registerListener() {
        getProxy().getPluginManager().registerListener(this, new BungeeSpyListener(this));
    }

    /**
     * Register the plugin configuration.
     */
    public void registerConfig() {
        config = new BungeeSpyConfig(this);
        config.loadConfig();
    }

    /**
     * Register the plugin commands.
     */
    public void registerCommands() {
        BungeeCommandManager commandManager = new BungeeCommandManager(this);

        commandManager.registerCommand(new BungeeSpyCommand(this));
    }

    /**
     * Retrieve the plugin configuration.
     *
     * @return Configuration
     */
    public Configuration getConfig() {
        return config.getConfig();
    }

    /**
     * Save the plugin configuration.
     */
    public void saveConfig() {
        config.saveConfig();
    }

    /**
     * Parse a message through translate alternate colors with the plugin prefix.
     *
     * @param sender The command sender.
     * @param value  The message value.
     */
    public void message(CommandSender sender, String value) {
        sender.sendMessage(
            ChatColor.translateAlternateColorCodes('&', getConfig().getString("locale.prefix") + " " + value)
        );
    }

    /**
     * Determine if the player is a spy.
     *
     * @return boolean
     */
    public boolean isSpy(UUID player) {
        return getConfig().contains("spies." + player);
    }

    /**
     * Set a spy.
     */
    public void setSpy(UUID player, Object value) {
        getConfig().set("spies." + player, value);
        saveConfig();
    }
}
