package com.ankoki.skjade.utils;

import com.ankoki.skjade.SkJade;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config {

    public static boolean HOLOGRAPHIC_DISPLAYS_ENABLED, VERSION_ALERTS = true;

    private final SkJade plugin;

    private FileConfiguration config;
    private File file;

    public Config(SkJade plugin) {
        this.plugin = plugin;
        reloadConfig();
    }

    public void reloadConfig() {
        if (file == null) {
            file = new File(plugin.getDataFolder(), "config.yml");
        }
        if (!file.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
        loadFile();
    }

    private void loadFile() {
        if (config.contains("holograms-enabled")) HOLOGRAPHIC_DISPLAYS_ENABLED = config.getBoolean("holograms-enabled");
        else SkJade.getInstance().getLogger().severe("Required key 'holograms-enabled' was not found.");
        if (config.contains("new-version-alerts")) HOLOGRAPHIC_DISPLAYS_ENABLED = config.getBoolean("new-version-alerts");
        else SkJade.getInstance().getLogger().severe("Required key 'new-version-alerts' was not found.");
    }
}
