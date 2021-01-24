package com.ankoki.skjade;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.ankoki.skjade.commands.SkJadeCmd;
import com.ankoki.skjade.hooks.elementals.EleClassInfo;
import com.ankoki.skjade.hooks.holograms.HoloClassInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

//TODO make a seraliser for holograms so they are persistant over restart c:
public class SkJade extends JavaPlugin {

    private static boolean beta;
    private static SkJade instance;
    private static String version;
    private PluginManager pluginManager;
    private SkriptAddon addon;
    private Logger logger;

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
        addon = Skript.registerAddon(this);
        this.loadElements();

        if (isPluginEnabled("HolographicDisplays")) {
            logger.info("HolographicDisplays was found! Enabling support");
            this.loadHDElements();
        }
        if (isPluginEnabled("Citizens")) {
            logger.info("Citizens was found! Enabling support");
            this.loadCitizensElements();
        }
        if (isPluginEnabled("Elementals")) {
            logger.info("Elementals was found! Enabling support");
            this.loadElementalsElements();
        }
        if (version.endsWith("-beta")) {
            logger.warning("You are running on an unstable release and SkJade could potentionally not " +
                    "work correctly!");
            logger.warning("I recommend switching to a non-beta version of SkJade, especially if " +
                    "runninng on a production server, as data might be lost!");
        }
        this.registerCommand();
        logger.info(String.format("SkJade v%s has been successfully enabled in %.2f seconds (%sms)",
                version, (float) System.currentTimeMillis() - start, System.currentTimeMillis() - start));
        /*
        //This isn't on github just yet so this will cause errors.
        UpdateChecker checker = new UpdateChecker("Ankoki-Dev", "SkJade");
        if (checker.isOutdated) {
            logger.info("You are not running the latest version of SkJade! Please update here:");
            logger.info("https://www.github.com/Ankoki-Dev/SkJade/releases/latest");
        }
         */
    }

    @Override
    public void onDisable() {
        beta = false;
        instance = null;
        pluginManager = null;
        addon = null;
        logger = null;
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
            addon.loadClasses("com.ankoki.skjade.elements",
                    "expressions",
                    "effects",
                    "events",
                    "conditions");
            logger.info("All elements have been loaded successfully!");
        } catch (IOException ex) {
            logger.info("Something went horribly wrong!");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean loadHDElements() {
        try {
            addon.loadClasses("com.ankoki.skjade.hooks.holograms",
                    "expressions",
                    "effects",
                    "conditions",
                    "events");
            new HoloClassInfo();
            logger.info("HolographicDisplays hooks loaded successfully!");
        } catch (IOException ex) {
            logger.info("Something went horribly wrong!");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean loadCitizensElements() {
        try {
            addon.loadClasses("com.ankoki.skjade.hooks.citizens",
                    "expressions",
                    "effects",
                    "events",
                    "conditions");
        } catch (IOException ex) {
            logger.info("Something went horribly wrong!");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean loadElementalsElements() {
        try {
            addon.loadClasses("com.ankoki.skjade.hooks.elementals",
                    "expressions",
                    "effects",
                    "events",
                    "conditions");
            new EleClassInfo();
            logger.info("Elementals hooks loaded successfully!");
        } catch (IOException ex) {
            logger.info("Something went horribly wrong!");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private void registerCommand() {
        this.getServer().getPluginCommand("skjade").setExecutor(new SkJadeCmd());
    }

    public static boolean isBeta() {
        return beta;
    }

    public static String getVersion() {
        return version;
    }

    public static SkJade getInstance() {
        return instance;
    }
}
