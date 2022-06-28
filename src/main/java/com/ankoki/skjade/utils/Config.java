package com.ankoki.skjade.utils;

import com.ankoki.skjade.SkJade;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config {

    public static boolean HOLOGRAMS_ENABLED, VERSION_ALERTS = true;
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
        if (config.contains("holograms-enabled")) HOLOGRAMS_ENABLED = config.getBoolean("holograms-enabled");
        else SkJade.getInstance().getLogger().severe("Required key 'holograms-enabled' was not found.");
        if (config.contains("hologram-plugin")) HOLOGRAM_PLUGIN = config.getString("hologram-plugin");
        else SkJade.getInstance().getLogger().severe("Required key 'hologram-plugin' was not found.");
        if (config.contains("new-version-alerts")) HOLOGRAMS_ENABLED = config.getBoolean("new-version-alerts");
        else SkJade.getInstance().getLogger().severe("Required key 'new-version-alerts' was not found.");
    }
}
