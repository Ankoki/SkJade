package com.ankoki.skjade.utils;

import com.ankoki.skjade.SkJade;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config {

    public static boolean HOLOGRAMS_ENABLED, VERSION_ALERTS, BINFLOP_ENABLED, LASERS_ENABLED, MISC_ENABLED;
    public static String HOLOGRAM_PLUGIN = "<none>";

    private final SkJade plugin;

    private FileConfiguration config;
    private File file;

    public Config(SkJade plugin) {
        this.plugin = plugin;
        reloadConfig();
    }

    public void reloadConfig() {
        if (file == null) file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) plugin.saveResource("config.yml", false);
        config = YamlConfiguration.loadConfiguration(file);
        loadFile();
    }

    private void loadFile() {
        if (config.contains("hologram-plugin")) HOLOGRAM_PLUGIN = config.getString("hologram-plugin");
        else SkJade.getInstance().getLogger().severe("Required key 'hologram-plugin' was not found.");
        if (config.contains("new-version-alerts")) HOLOGRAMS_ENABLED = config.getBoolean("new-version-alerts");
        else SkJade.getInstance().getLogger().severe("Required key 'new-version-alerts' was not found.");
        if (config.contains("binflop-elements-enabled")) BINFLOP_ENABLED = config.getBoolean("binflop-elements-enabled");
        else SkJade.getInstance().getLogger().severe("Required key 'binflop-elements-enabled' was not found.");
        if (config.contains("laser-elements-enabled")) LASERS_ENABLED = config.getBoolean("laser-elements-enabled");
        else SkJade.getInstance().getLogger().severe("Required key 'laser-elements-enabled' was not found.");
        if (config.contains("holographic-elements-enabled")) HOLOGRAMS_ENABLED = config.getBoolean("holographic-elements-enabled");
        else SkJade.getInstance().getLogger().severe("Required key 'holographic-elements-enabled' was not found.");
        if (config.contains("misc-elements-enabled")) MISC_ENABLED = config.getBoolean("misc-elements-enabled");
        else SkJade.getInstance().getLogger().severe("Required key 'misc-elements-enabled' was not found.");
    }
}
