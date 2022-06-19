package com.ankoki.skjade;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;
import com.ankoki.roku.bukkit.BukkitImpl;
import com.ankoki.roku.web.JSON;
import com.ankoki.roku.web.WebRequest;
import com.ankoki.roku.web.exceptions.MalformedJsonException;
import com.ankoki.skjade.commands.SkJadeCmd;
import com.ankoki.skjade.elements.lasers.Laser;
import com.ankoki.skjade.listeners.PlayerJoin;
import com.ankoki.skjade.utils.*;
import org.bstats.bukkit.Metrics;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SkJade extends JavaPlugin {

    private static SkJade instance;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private String version;
    private SkriptAddon addon;
    private boolean nmsEnabled = false;
    private boolean latest = true;
    private Config config = null;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        version = this.getDescription().getVersion();
        if (!Utils.isPluginEnabled("Skript") && Skript.isAcceptRegistrations()) {
            this.getLogger().severe("Skript wasn't found. Are you sure it's installed and up to date?");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        } else if (Skript.getVersion().isSmallerThan(new ch.njol.skript.util.Version("2.6"))) {
            this.getLogger().severe("Skript is running a version lower than 2.6, which is unsupported.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        } else if (!Utils.isPluginEnabled("Roku")) BukkitImpl.setupRoku(this);

        config = new Config(this);

        addon = Skript.registerAddon(this);
        this.loadNMS();
        this.loadClassInfo();
        this.loadElements();

        if (Utils.isPluginEnabled("HolographicDisplays") && Config.HOLOGRAPHIC_DISPLAYS_ENABLED) {
            this.getLogger().info("HolographicDisplays was found! Enabling support");
            this.loadHologramElements();
        }

        this.registerListeners(new PlayerJoin());
        new Metrics(this, 10131);
        this.getServer().getPluginCommand("skjade").setExecutor(new SkJadeCmd());

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            String version = "INVALID";
            try {
                WebRequest request = new WebRequest("https://api.github.com/repos/Ankoki/SkJade/releases/latest", WebRequest.RequestType.GET);
                Optional<String> optional = request.execute();
                if (optional.isPresent()) {
                    try {
                        JSON json = new JSON(optional.get());
                        version = (String) json.get("tag_name");
                    } catch (MalformedJsonException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return version;
        });

        future.thenApply(version -> this.latest = version.equalsIgnoreCase(this.getVersion()));

        if (Version.currentIsLegacy()) {
            this.getLogger().warning("Please note SkJade does not support legacy versions. The supported versions are 1.13+.");
            this.getLogger().warning("You have no reason to not use the latest server version. SkJade will still be enabled, " +
                    "however you may encounter some issues which may not get fixed due to not supporting fossil versions.");
        }

        long fin = System.currentTimeMillis() - start;
        this.getLogger().info("SkJade v" + version + " has been successfully enabled in " + df.format(fin / 1000.0) + " seconds (" +
                fin + "ms)");
    }

    private void loadNMS() {
        if (Version.CURRENT_VERSION == Version.UNKNOWN || Version.currentIsLegacy()) {
            this.getLogger().warning("Could not find any NMS support for " + version + "! Please note SkJade only supports " +
                    "the latest sub-version of each version above 1.13.");
            this.getLogger().warning("There is also a chance you are using a version I haven't implemented support for yet.");
            this.getLogger().warning("SkJade will remain enabled, however anything using NMS will not be enabled!");
        } else nmsEnabled = true;
    }

    private void loadElements() {
        try {
            addon.loadClasses("com.ankoki.skjade.elements",
                    "expressions",
                    "effects",
                    "events",
                    "conditions",
                    "binflop",
                    "lasers");
        } catch (IOException ex) {
            this.getLogger().severe("Something went horribly wrong!");
            ex.printStackTrace();
        }
    }

    private void loadHologramElements() {
        try {
            addon.loadClasses("com.ankoki.skjade.hooks.holograms");
        } catch (IOException ex) {
            this.getLogger().severe("Something went horribly wrong!");
            ex.printStackTrace();
        }
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners)
            this.getServer().getPluginManager().registerEvents(listener, this);
    }

    private void loadClassInfo() {
        //Laser ClassInfo
        Classes.registerClass(new ClassInfo<>(Laser.class, "laser")
                .user("laser?s?")
                .name("Laser")
                .description("A guardian beam.")
                .since("1.3.1"));
        if (Utils.getMinecraftMinor() > 12) NonLegacyClassInfo.register();
    }

    public boolean isBeta() {
        return version.endsWith("-beta");
    }

    public String getVersion() {
        return version;
    }

    public static SkJade getInstance() {
        return instance;
    }

    public boolean isNmsEnabled() {
        return nmsEnabled;
    }

    public boolean isLatest() {
        return latest;
    }

    public Config getOwnConfig() {
        return config;
    }
}