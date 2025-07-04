package io.github.mcengine.spigotmc.party.engine;

import io.github.mcengine.api.core.MCEngineApi;
import io.github.mcengine.api.core.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class MCEnginePartySpigotMC extends JavaPlugin {

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        new Metrics(this, 25764);
        saveDefaultConfig(); // Save config.yml if it doesn't exist

        boolean enabled = getConfig().getBoolean("enable", false);
        if (!enabled) {
            getLogger().warning("Plugin is disabled in config.yml (enable: false). Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Load extensions
        MCEngineApi.loadExtensions(
            this,
            "io.github.mcengine.api.party.extension.library.IMCEnginePartyLibrary",
            "libraries",
            "Library"
        );
        MCEngineApi.loadExtensions(
            this,
            "io.github.mcengine.api.party.extension.api.IMCEnginePartyAPI",
            "apis",
            "API"
        );
        MCEngineApi.loadExtensions(
            this,
            "io.github.mcengine.api.party.extension.addon.IMCEnginePartyAddOn",
            "addons",
            "AddOn"
        );
        MCEngineApi.loadExtensions(
            this,
            "io.github.mcengine.api.party.extension.dlc.IMCEnginePartyDLC",
            "dlcs",
            "DLC"
        );

        MCEngineApi.checkUpdate(this, getLogger(), "github", "MCEngine", "party-engine", getConfig().getString("github.token", "null"));
    }

    @Override
    public void onDisable() {}
}
