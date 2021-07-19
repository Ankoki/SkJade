package com.ankoki.skjade;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.Converters;
import ch.njol.util.coll.CollectionUtils;
import com.ankoki.pastebinapi.api.PasteBuilder;
import com.ankoki.skjade.commands.SkJadeCmd;
import com.ankoki.skjade.elements.lasers.Laser;
import com.ankoki.skjade.elements.pastebinapi.PasteManager;
import com.ankoki.skjade.listeners.PlayerJoin;
import com.ankoki.skjade.utils.*;
import com.ankoki.skjade.utils.events.RealTimeEvent;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * IMPORTANT
 * <p>
 * SkJade is named SkJade because Jade West from Victorious is absolutely
 * gorgeous. We love Liz Gillies. Ok bye now <3
 */
public class SkJade extends JavaPlugin {

    private boolean beta;
    private static SkJade instance;
    private String version;
    private Version serverVersion;
    private PluginManager pluginManager;
    private SkriptAddon addon;
    private boolean nmsEnabled = false;
    private boolean latest = true;
    private Config config = null;
    private final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        pluginManager = this.getServer().getPluginManager();
        version = this.getDescription().getVersion();
        if (!isSkriptEnabled()) {
            Console.info("Skript wasn't found. Are you sure it's installed and up to date?");
            pluginManager.disablePlugin(this);
            return;
        }
        config = new Config(this);
        this.loadNMS();
        this.loadClassInfo();
        if (Utils.getServerMajorVersion() > 12) {
            new NonLegacyClassInfo();
        }
        addon = Skript.registerAddon(this);

        this.loadElements();
        if (isPluginEnabled("ProtocolLib") && Config.PROTOCOL_LIB_ENABLED) {
            Console.info("ProtocolLib was found! Enabling support");
            this.loadProtocolElements();
        }
        if (isPluginEnabled("HolographicDisplays") && Config.HOLOGRAPHIC_DISPLAYS_ENABLED) {
            Console.info("HolographicDisplays was found! Enabling support");
            this.loadHDElements();
        }
        if (isPluginEnabled("Elementals") && Config.ELEMENTALS_ENABLED) {
            Plugin elementals = pluginManager.getPlugin("Elementals");
            assert elementals != null;
            if (Utils.checkPluginVersion(elementals, 1, 4)) {
                Console.info("Elementals was found! Enabling support");
                this.loadElementalsElements();
            } else {
                Console.info("Elementals was found, however it is an outdated version! Please upgrade to atleast version 1.4.");
            }
        }

        this.registerListeners(new PlayerJoin());
        if (version.endsWith("-beta")) {
            Console.warning("You are running on an unstable release, SkJade could potentionally " +
                    "function incorrectly!");
            Console.warning("Switching to a non-beta version of SkJade is HIGHLY recommended, especially if you're " +
                    "runninng on a production server, as data might be lost!");
            beta = true;
        }
        new Metrics(this, 10131);
        this.getServer().getPluginCommand("skjade").setExecutor(new SkJadeCmd());
        this.loadServerVersion();
        this.startRealTime();

        long fin = System.currentTimeMillis() - start;
        Console.info("SkJade v" + version + " has been successfully enabled in " + df.format(fin / 1000.0) + " seconds (" +
                fin + "ms)");

        new Thread(() -> {
            UpdateChecker checker = new UpdateChecker("Ankoki-Dev", "SkJade");
            if (!checker.isLatest()) {
                Console.info("You are not running the latest version of SkJade! Please update here:");
                Console.info("https://www.github.com/Ankoki-Dev/SkJade/releases/latest");
                latest = false;
            }
        }).start();

        if (serverVersion.isLegacy()) {
            Console.warning("Please note SkJade does not support legacy versions. The supported versions are 1.13+.");
            Console.warning("You have no reason to not use the latest server version. SkJade will still be enabled, " +
                    "however you may encounter some issues which may not get fixed due to not supporting fossil versions.");
        }
    }

    private void loadNMS() {
        if (Utils.getServerMajorVersion() < 13) {
            Console.warning("Could not find any NMS support for " + version + "! Please note SkJade only supports " +
                    "the latest sub-version of each version above 1.13.");
            Console.info("SkJade will remain enabled, however anything using NMS will not be enabled!");
        } else {
            nmsEnabled = true;
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

    private void loadElements() {
        try {
            addon.loadClasses("com.ankoki.skjade.elements",
                    "expressions",
                    "effects",
                    "events",
                    "conditions",
                    "pastebinapi",
                    "lasers");
        } catch (IOException ex) {
            Console.info("Something went horribly wrong!");
            ex.printStackTrace();
        }
    }

    private void loadHDElements() {
        try {
            addon.loadClasses("com.ankoki.skjade.hooks.holograms");
        } catch (IOException ex) {
            Console.info("Something went horribly wrong!");
            ex.printStackTrace();
        }
    }

    private void loadServerVersion() {
        String packageName = this.getServer().getClass().getPackage().getName();
        String ver = packageName.substring(packageName.lastIndexOf('.') + 1);
        try {
            serverVersion = Version.valueOf(ver);
        } catch (Exception ex) {
            Console.warning("You are using an unknown version (" + ver + ")! SkJade will not function as intended");
        }
    }

    private void loadElementalsElements() {
        try {
            addon.loadClasses("com.ankoki.skjade.hooks.elementals");
        } catch (IOException ex) {
            Console.info("Something went horribly wrong!");
            ex.printStackTrace();
        }
    }

    private void loadProtocolElements() {
        try {
            addon.loadClasses("com.ankoki.skjade.hooks.protocollib");
        } catch (IOException ex) {
            Console.info("Something went horribly wrong!");
            ex.printStackTrace();
        }
    }

    private void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> this.pluginManager.registerEvents(listener, this));
    }

    private void loadClassInfo() {
        //Pastebin ClassInfo
        Classes.registerClass(new ClassInfo<>(PasteBuilder.class, "paste")
                .user("paste?s?")
                .name("Paste")
                .description("A PasteBuilder created with SkJade.")
                .since("1.0.0")
                .changer(new Changer<PasteBuilder>() {
                    @Nullable
                    @Override
                    public Class<?>[] acceptChange(ChangeMode mode) {
                        if (mode == ChangeMode.DELETE || mode == ChangeMode.RESET || mode == ChangeMode.REMOVE_ALL) {
                            return CollectionUtils.array();
                        }
                        return null;
                    }

                    @Override
                    public void change(PasteBuilder[] what, @Nullable Object[] delta, ChangeMode mode) {
                        switch (mode) {
                            case DELETE:
                                PasteManager.deletePaste(what);
                                break;
                            case RESET:
                            case REMOVE_ALL:
                                PasteManager.resetPaste(what);
                        }
                    }
                }));

        //Character ClassInfo
        Classes.registerClass(new ClassInfo<>(Character.class, "character")
                .user("char(acter)?s?")
                .name("Character")
                .description("A single character.")
                .since("1.1.0"));

        Converters.registerConverter(Character.class, String.class, String::valueOf);
        Converters.registerConverter(Character.class, Integer.class, Character::getNumericValue);

        //Laser ClassInfo
        Classes.registerClass(new ClassInfo<>(Laser.class, "laser")
                .user("laser?s?")
                .name("Laser")
                .description("A guardian beam.")
                .since("1.3.1"));
    }

    private void startRealTime() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> Bukkit.getPluginManager().callEvent(new RealTimeEvent(new Date())), 0L, 20 * 60L);
    }

    public boolean isBeta() {
        return beta;
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