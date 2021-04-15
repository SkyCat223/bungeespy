package org.creativecraft.bungeespy;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeSpyListener implements Listener {
    private final BungeeSpy plugin;

    public BungeeSpyListener(BungeeSpy plugin) {
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

        if (this.plugin.getConfig().getList("blacklist").contains(command)) {
            return;
        }


        for (ProxiedPlayer p : this.plugin.getProxy().getPlayers()) {
            if (p.hasPermission("bungeespy.use") && this.plugin.isSpy(p.getUniqueId())) {
                if (p.getUniqueId() == player.getUniqueId() && !this.plugin.getConfig().getBoolean("show-own-commands")) {
                    continue;
                }

                p.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                        '&',
                        this.plugin.getConfig().getString("locale.message")
                            .replace("{0}", player.getName())
                            .replace("{1}", event.getMessage())
                    )
                );
            }
        }
    }
}
