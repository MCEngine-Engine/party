package io.github.mcengine.spigotmc.party.engine;

import io.github.mcengine.api.core.MCEngineCoreApi;
import io.github.mcengine.api.core.Metrics;
import io.github.mcengine.common.party.MCEnginePartyCommon;
import io.github.mcengine.common.party.command.MCEnginePartyCommand;
import io.github.mcengine.common.party.listener.MCEnginePartyListener;
import io.github.mcengine.common.party.tabcompleter.MCEnginePartyCompleter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main Spigot plugin class for the MC Engine Party system.
 * Handles plugin lifecycle, command, and event registration.
 */
public class MCEnginePartySpigotMC extends JavaPlugin {

    /**
     * The main party logic handler.
     */
    private MCEnginePartyCommon partyCommon;

    /**
     * Called when the plugin is enabled.
     * Registers commands, listeners, loads extensions and checks for updates.
     */
    @Override
    public void onEnable() {
        // Initialize bStats metrics
        new Metrics(this, 25764);

        // Save default config if it doesn't exist
        saveDefaultConfig();

        // Check config if plugin is enabled
        boolean enabled = getConfig().getBoolean("enable", false);
        if (!enabled) {
            getLogger().warning("Plugin is disabled in config.yml (enable: false). Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // =========================
        // Register MC Engine Party
        // =========================

        // Initialize core party logic
        this.partyCommon = new MCEnginePartyCommon(this);

        // Register command executor for /party
        this.getCommand("party").setExecutor(new MCEnginePartyCommand(partyCommon));

        // Register tab completer for /party
        this.getCommand("party").setTabCompleter(new MCEnginePartyCompleter());

        // Register listener for player party leave on quit
        /* getServer().getPluginManager().registerEvents(new MCEnginePartyListener(partyCommon), this); */

        // Load extensions
        MCEngineCoreApi.loadExtensions(
            this,
            "io.github.mcengine.api.party.extension.library.IMCEnginePartyLibrary",
            "libraries",
            "Library"
        );
        MCEngineCoreApi.loadExtensions(
            this,
            "io.github.mcengine.api.party.extension.api.IMCEnginePartyAPI",
            "apis",
            "API"
        );
        MCEngineCoreApi.loadExtensions(
            this,
            "io.github.mcengine.api.party.extension.addon.IMCEnginePartyAddOn",
            "addons",
            "AddOn"
        );
        MCEngineCoreApi.loadExtensions(
            this,
            "io.github.mcengine.api.party.extension.dlc.IMCEnginePartyDLC",
            "dlcs",
            "DLC"
        );

        // Check for plugin updates
        MCEngineCoreApi.checkUpdate(this, getLogger(), "github", "MCEngine", "party-engine", getConfig().getString("github.token", "null"));
    }

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        // No special shutdown logic needed.
    }
}
