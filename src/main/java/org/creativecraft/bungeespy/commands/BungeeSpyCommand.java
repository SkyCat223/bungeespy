package org.creativecraft.bungeespy.commands;

import co.aikar.commands.annotation.*;
import co.aikar.commands.BaseCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import org.creativecraft.bungeespy.BungeeSpy;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@CommandAlias("bspy")
@Description("Spy on player commands network-wide.")
public final class BungeeSpyCommand extends BaseCommand implements Listener {
    @Dependency
    private BungeeSpy plugin;

    /**
     * Toggle network-wide command spying.
     *
     * @param player The proxied player.
     */
    @Default
    @Subcommand("toggle")
    @CommandPermission("bungeespy.use")
    @Description("Toggle network-wide command spying.")
    public void onBungeeSpyCommand(ProxiedPlayer player, @Optional String target) {
        UUID id = player.getUniqueId();

        if (plugin.isSpy(id)) {
            plugin.removeSpy(id);

            plugin.message(player, plugin.getConfig().getString("locale.toggle-off"));

            return;
        }

        plugin.addSpy(id);

        plugin.message(player, plugin.getConfig().getString("locale.toggle-on"));
    }

    /**
     * List players who have spy enabled.
     *
     * @param sender The command sender.
     */
    @Subcommand("list")
    @CommandPermission("bungeespy.list")
    @Description("List players who have spy enabled.")
    public void onBungeeSpyListCommand(CommandSender sender) {
        Collection<String> keys = plugin.getConfig().getSection("spies").getKeys();
        HashSet<String> spies = new HashSet<String>();

        keys.forEach(spy -> {
            ProxiedPlayer s = plugin.getProxy().getPlayer(UUID.fromString(spy));

            if (s == null) {
                return;
            }

            spies.add(s.getName());
        });

        plugin.message(
            sender,
            plugin.getConfig().getString("locale.list")
                .replace("{0}", spies.size() != 0 ?
                    String.join(", ", spies) :
                    plugin.getConfig().getString("locale.list-none"))
        );
    }

    /**
     * Reload the plugin configuration.
     *
     * @param sender The command sender.
     */
    @Subcommand("reload")
    @CommandPermission("bungeespy.reload")
    @Description("Reload the plugin configuration.")
    public void onBungeeSpyReloadCommand(CommandSender sender) {
        try {
            plugin.registerConfig();

            plugin.message(sender, plugin.getConfig().getString("locale.config-reload"));
        } catch (Exception e) {
            plugin.message(sender, plugin.getConfig().getString("locale.config-reload-failed"));
        }
    }
}
