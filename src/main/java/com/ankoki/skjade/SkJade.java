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
import com.ankoki.skjade.hooks.holograms.HoloManager;
import com.ankoki.skjade.hooks.holograms.impl.decentholograms.DHProvider;
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

    private com.ankoki.roku.misc.Version version;
    private SkriptAddon addon;
    private boolean nmsEnabled = false;
    private boolean latest = true;
    private Config config = null;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        new Metrics(this, 10131);
        version = com.ankoki.roku.misc.Version.of(this.getDescription().getVersion());

        if (!Utils.isPluginEnabled("Skript") || !Skript.isAcceptRegistrations()) {
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

        if (!Config.MISC_ENABLED && !Config.BINFLOP_ENABLED && !Config.LASERS_ENABLED && !Config.HOLOGRAMS_ENABLED) {
            this.getLogger().severe("There are no SkJade elements enabled in the config. Disabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        if (Config.MISC_ENABLED) this.loadMiscElements();
        else this.getLogger().warning("Misc elements not enabled in the config. Skipping...");

        if (Config.BINFLOP_ENABLED) this.loadBinflopElements();
        else this.getLogger().warning("Binflop elements not enabled in the config. Skipping...");

        if (Config.LASERS_ENABLED) this.loadLaserElements();
        else this.getLogger().warning("Laser elements not enabled in the config. Skipping...");

        if (Config.HOLOGRAMS_ENABLED) {
            HoloManager.get().addProvider(new DHProvider());
            if (Utils.isPluginEnabled(Config.HOLOGRAM_PLUGIN) && HoloManager.get().hasProvider(Config.HOLOGRAM_PLUGIN)) {
                this.getLogger().info(Config.HOLOGRAM_PLUGIN + " was found! Enabling support.");
                HoloManager.get().setCurrentProvider(HoloManager.get().getProvider(Config.HOLOGRAM_PLUGIN));
                this.loadHologramElements();
            } else this.getLogger().severe("'" + Config.HOLOGRAM_PLUGIN + "' was either not found, or there is no SkJade provider for it. Hologram elements will not be enabled.");
        } else this.getLogger().warning("Holographic elements not enabled in the config. Skipping...");

        this.registerListeners(new PlayerJoin());
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

        future.thenApply(version -> {
            com.ankoki.roku.misc.Version latest = com.ankoki.roku.misc.Version.of(version);
            this.latest = latest.isNewerThan(this.version);
            if (!this.latest) this.getLogger().warning("You are due an update for SkJade. The latest version is v" + version + "! " +
                    "Find it at https://www.github.com/Ankoki/SkJade/releases/latest/");
            return false;
        });

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

    private void loadMiscElements() {
        if (Utils.getMinecraftMinor() > 12) NonLegacyClassInfo.register();
        try {
            addon.loadClasses("com.ankoki.skjade.elements",
                    "expressions",
                    "effects",
                    "events",
                    "conditions");
        } catch (IOException ex) {
            this.getLogger().severe("Something went horribly wrong loading misc elements.");
            ex.printStackTrace();
        }
    }

    private void loadBinflopElements() {
        try {
            addon.loadClasses("com.ankoki.skjade.elements", "binflop");
        } catch (IOException ex) {
            this.getLogger().severe("Something went horribly wrong loading binflop elements.");
            ex.printStackTrace();
        }
    }

    private void loadLaserElements() {
        Classes.registerClass(new ClassInfo<>(Laser.class, "laser")
                .user("laser?s?")
                .name("Laser")
                .description("A guardian beam.")
                .since("1.3.1"));
        try {
            addon.loadClasses("com.ankoki.skjade.elements", "lasers");
        } catch (IOException ex) {
            this.getLogger().severe("Something went horribly wrong loading laser elements.");
            ex.printStackTrace();
        }
    }

    private void loadHologramElements() {
        try {
            addon.loadClasses("com.ankoki.skjade.hooks.holograms");
        } catch (IOException ex) {
            this.getLogger().severe("Something went horribly wrong loading holographic elements.");
            ex.printStackTrace();
        }
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners)
            this.getServer().getPluginManager().registerEvents(listener, this);
    }

    public boolean isBeta() {
        return version.hasSuffix() && version.getSuffix().equalsIgnoreCase("beta");
    }

    public com.ankoki.roku.misc.Version getVersion() {
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