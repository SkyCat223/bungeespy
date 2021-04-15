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
    @CommandPermission("bungeespy.use")
    @Description("Toggle network-wide command spying.")
    public void onBungeeSpyCommand(ProxiedPlayer player) {
        UUID id = player.getUniqueId();

        if (this.plugin.isSpy(id)) {
            this.plugin.setSpy(id, null);

            this.plugin.message(player, this.plugin.getConfig().getString("locale.toggle-off"));

            return;
        }

        this.plugin.setSpy(id, true);

        this.plugin.message(player, this.plugin.getConfig().getString("locale.toggle-on"));
    }

    @Subcommand("list")
    @CommandPermission("bungeespy.list")
    @Description("List players who have spy enabled.")
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

    @Subcommand("reload")
    @CommandPermission("bungeespy.reload")
    @Description("Reload the BungeeSpy configuration.")
    public void onBungeeSpyReloadCommand(CommandSender sender) {
        try {
            this.plugin.registerConfig();

            this.plugin.message(sender, this.plugin.getConfig().getString("locale.config-reload"));
        } catch (Exception e) {
            this.plugin.message(sender, this.plugin.getConfig().getString("locale.config-reload-failed"));
        }
    }
}
