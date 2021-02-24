package com.ankoki.skjade;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.ankoki.skjade.api.NMS;
import com.ankoki.skjade.commands.SkJadeCmd;
import com.ankoki.skjade.listeners.PlayerJoin;
import com.ankoki.skjade.utils.Config;
import com.ankoki.skjade.utils.UpdateChecker;
import com.ankoki.skjade.utils.Utils;
import com.ankoki.skjade.utils.Version;
import org.bstats.bukkit.Metrics;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

/** IMPORTANT
 *
 * SkJade is named SkJade because Jade West from Victorious is absolutely
 * gorgeous. We love Liz Gillies. Ok bye now <3
 */
public class SkJade extends JavaPlugin {

    private static boolean beta;
    private static SkJade instance;
    private static String version;
    private static Version serverVersion;
    private PluginManager pluginManager;
    private SkriptAddon addon;
    private Logger logger;
    private final int pluginId = Integer.parseInt(Integer.toString(10131));
    private Metrics metrics;
    private static NMS nmsHandler = null;
    private static boolean nmsEnabled = false;
    private static boolean latest = true;
    private static Config config = null;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        pluginManager = this.getServer().getPluginManager();
        logger = this.getLogger();
        version = this.getDescription().getVersion();
        if (!isSkriptEnabled()) {
            logger.info("Skript wasn't found. Are you sure it's installed and up to date?");
            pluginManager.disablePlugin(this);
            return;
        }
        config = new Config(this);
        loadNMS();
        addon = Skript.registerAddon(this);
        this.loadElements();
        if (isPluginEnabled("ProtocolLib") && Config.PROTOCOL_LIB_ENABLED) {
            logger.info("ProtocolLib was found! Enabling support");
            this.loadProtocolElements();
        }
        if (isPluginEnabled("HolographicDisplays") && Config.HOLOGRAPHIC_DISPLAYS_ENABLED) {
            logger.info("HolographicDisplays was found! Enabling support");
            this.loadHDElements();
        }
        if (isPluginEnabled("Elementals") && Config.ELEMENTALS_ENABLED) {
            Plugin elementals = pluginManager.getPlugin("Elementals");
            assert elementals != null;
            if (Utils.checkPluginVersion(elementals, 1, 4)) {
                logger.info("Elementals was found! Enabling support");
                this.loadElementalsElements();
            } else {
                logger.info("Elementals was found! However it is an early version! Please upgrade to atleast 1.4.");
            }
        }
        this.registerListeners(new PlayerJoin());
        if (version.endsWith("-beta")) {
            logger.warning("You are running on an unstable release and SkJade could potentionally not " +
                    "work correctly!");
            logger.warning("I recommend switching to a non-beta version of SkJade, especially if you're " +
                    "runninng on a production server, as data might be lost!");
            beta = true;
        }
        metrics = new Metrics(this, pluginId);
        this.getServer().getPluginCommand("skjade").setExecutor(new SkJadeCmd());
        this.loadServerVersion();
        logger.info(String.format("SkJade v%s has been successfully enabled in %.2f seconds (%sms)",
                version, (float) System.currentTimeMillis() - start, System.currentTimeMillis() - start));
        UpdateChecker checker = new UpdateChecker("Ankoki-Dev", "SkJade");
        if (!checker.isLatest()) {
            logger.info("You are not running the latest version of SkJade! Please update here:");
            logger.info("https://www.github.com/Ankoki-Dev/SkJade/releases/latest");
            latest = false;
        }
    }

    private void loadNMS() {
        String packageName = this.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        try {
            final Class<?> clazz = Class.forName("com.ankoki.skjade.nms." + version + ".NMSHandler");
            if (NMS.class.isAssignableFrom(clazz)) {
                nmsHandler = (NMS) clazz.getConstructor().newInstance();
                nmsEnabled = true;
                logger.info("NMS Support for " + version + " loaded!");
            } else {
                logger.severe("Could not find any NMS support for this version! Please note SkJade only supports the latest sub-version of each version.");
                logger.info("SkJade will remain enabled, however anything using NMS will not function!");
            }
        } catch (Exception ex) {
            logger.severe("Could not find any NMS support for this version! Please note SkJade only supports the latest sub-version of each version.");
            logger.info("SkJade will remain enabled, however anything using NMS will not function!");
        }
    }

    private boolean isSkriptEnabled() {
        Plugin skript = pluginManager.getPlugin("Skript");
        if (skript == null) return false;
        if (!skript.isEnabled()) return false;
        return Skript.isAcceptRegistrations();
    }

    private boolean isPluginEnabled(String pluginName) {
        Plugin plugin = pluginManager.getPlugin(pluginName);
        if (plugin == null) return false;
        return plugin.isEnabled();
    }

    private boolean loadElements() {
        try {
            addon.loadClasses("com.ankoki.skjade.elements"/*,
                    "expressions",
                    "effects",
                    "events",
                    "conditions"*/);
        } catch (IOException ex) {
            logger.info("Something went horribly wrong!");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean loadHDElements() {
        try {
            addon.loadClasses("com.ankoki.skjade.hooks.holograms"/*,
                    "expressions",
                    "effects",
                    "conditions",
                    "events"*/);
        } catch (IOException ex) {
            logger.info("Something went horribly wrong!");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private void loadServerVersion() {
        String packageName = this.getServer().getClass().getPackage().getName();
        serverVersion = Version.valueOf(packageName.substring(packageName.lastIndexOf('.') + 1));
    }

    private boolean loadElementalsElements() {
        try {
            addon.loadClasses("com.ankoki.skjade.hooks.elementals"/*,
                    "expressions",
                    "effects",
                    "events",
                    "conditions"*/);
        } catch (IOException ex) {
            logger.info("Something went horribly wrong!");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean loadProtocolElements() {
        try {
            addon.loadClasses("com.ankoki.skjade.hooks.protocollib"/*,
                    "expressions",
                    "effects",
                    "events",
                    "conditions"*/);
        } catch (IOException ex) {
            logger.info("Something went horribly wrong!");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> this.pluginManager.registerEvents(listener, this));
    }

    public static boolean isBeta() {
        return beta;
    }

    public static String getVersion() {
        return version;
    }

    public static Version getServerVersion() {
        return serverVersion;
    }

    public static SkJade getInstance() {
        return instance;
    }

    public static boolean isNmsEnabled() {
        return nmsEnabled;
    }

    public static NMS getNmsHandler() {
        return nmsHandler;
    }

    public static boolean isLatest() {
        return latest;
    }

    public static Config getOwnConfig() {
        return config;
    }
}
