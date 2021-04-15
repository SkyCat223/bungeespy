package org.creativecraft.bungeespy.commands;

import co.aikar.commands.annotation.*;
import co.aikar.commands.BaseCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.creativecraft.bungeespy.BungeeSpy;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@CommandAlias("bungeespy")
@CommandPermission("bungeespy.use")
@Description("Spy on player commands network-wide.")
public final class BungeeSpyCommand extends BaseCommand implements Listener {
    private final BungeeSpy plugin;

    public BungeeSpyCommand(BungeeSpy plugin) {
        this.plugin = plugin;
    }

    /**
     * Retrieve the message of the day.
     *
     * @param player The proxied player.
     */
    @Default
    public void onBungeeSpyCommand(ProxiedPlayer player) {
        UUID id = player.getUniqueId();

        if (isSpy(id)) {
            setSpy(id, null);

            this.plugin.message(player, this.plugin.getConfig().getString("locale.toggle-off"));

            return;
        }

        setSpy(id, true);

        this.plugin.message(player, this.plugin.getConfig().getString("locale.toggle-on"));
    }

    @Subcommand("list")
    public void onBungeeSpyListCommand(CommandSender sender) {
        Collection<String> keys = this.plugin.getConfig().getSection("spies").getKeys();
        HashSet<String> spies = new HashSet<String>();

        keys.forEach(spy -> {
            ProxiedPlayer s = this.plugin.getProxy().getPlayer(UUID.fromString(spy));

            if (s == null) {
                return;
            }

            spies.add(s.getName());
        });

        this.plugin.message(
            sender,
            this.plugin.getConfig().getString("locale.list")
                .replace("{0}", spies.size() != 0 ?
                    String.join(", ", spies) :
                    this.plugin.getConfig().getString("locale.list-none"))
        );
    }

    /**
     * Determine if the player is a spy.
     *
     * @return boolean
     */
    public boolean isSpy(UUID player) {
        return this.plugin.getConfig().contains("spies." + player);
    }

    /**
     * Set a spy.
     */
    public void setSpy(UUID player, Object value) {
        this.plugin.getConfig().set("spies." + player, value);
        this.plugin.saveConfig();
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

        for (ProxiedPlayer p : this.plugin.getProxy().getPlayers()) {
            if (p.hasPermission("bungeespy.use") && isSpy(p.getUniqueId())) {
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
