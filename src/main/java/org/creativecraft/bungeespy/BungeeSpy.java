package org.creativecraft.bungeespy;

import co.aikar.commands.BungeeCommandManager;
import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.creativecraft.bungeespy.commands.BungeeSpyCommand;

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
        getProxy().getPluginManager().registerListener(this, new BungeeSpyCommand(this));
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
     * Parse a message through MineDown including our prefix.
     *
     * @param sender The command sender.
     * @param value  The message value.
     */
    public void message(CommandSender sender, String value) {
        sender.sendMessage(
            MineDown.parse(getConfig().getString("locale.prefix") + " " + value)
        );
    }
}
