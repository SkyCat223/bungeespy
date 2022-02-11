package org.creativecraft.bungeespy.listener;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.creativecraft.bungeespy.BungeeSpyPlugin;

public class EventListener implements Listener {
    private final BungeeSpyPlugin plugin;

    /**
     * Initialize the event listener instance.
     */
    public EventListener(BungeeSpyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Sends server and network commands to admins.
     *
     * @param event The chat event.
     */
    @EventHandler
    public void onChatEvent(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        if (!event.isCommand() && !event.isProxyCommand()) {
            return;
        }

        String command = event.getMessage().split(" ")[0].toLowerCase();

        if (plugin.getConfig().getStringList("excluded-servers").contains(player.getServer().getInfo().getName())) {
            return;
        }

        if (command.startsWith("//") && !plugin.getConfig().getBoolean("show-worldedit")) {
            return;
        }

        if (plugin.getConfig().getList("blacklist").contains(command)) {
            return;
        }

        for (ProxiedPlayer p : plugin.getProxy().getPlayers()) {
            if (p.hasPermission("bungeespy.use") && plugin.getUserData().contains("players." + p.getUniqueId())) {
                if (p.getUniqueId() == player.getUniqueId() && !plugin.getConfig().getBoolean("show-own-commands")) {
                    continue;
                }

                p.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                        '&',
                        plugin.localize("messages.generic.message")
                            .replace("{0}", player.getName())
                            .replace("{1}", event.getMessage())
                    )
                );
            }
        }
    }
}
