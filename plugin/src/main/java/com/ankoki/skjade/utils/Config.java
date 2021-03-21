package com.ankoki.skjade.utils;

import com.ankoki.skjade.SkJade;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Config {

    public static boolean PROTOCOL_LIB_ENABLED, HOLOGRAPHIC_DISPLAYS_ENABLED, ELEMENTALS_ENABLED;

    private final SkJade plugin;

    private FileConfiguration config;
    private File cfile;

    public Config(SkJade plugin) {
        this.plugin = plugin;
        reloadConfig();
    }

    public void reloadConfig() {
        if (cfile == null) {
            cfile = new File(plugin.getDataFolder(), "config.yml");
        }
        if (!cfile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(cfile);
        matchConfigFile();
        loadFile();
    }

    private void matchConfigFile() {
        try {
            boolean hasUpdated = false;
            InputStream stream = plugin.getResource(cfile.getName());
            assert stream != null;
            InputStreamReader is = new InputStreamReader(stream);
            YamlConfiguration defLand = YamlConfiguration.loadConfiguration(is);
            for (String key : defLand.getConfigurationSection("").getKeys(true)) {
                if (!config.contains(key)) {
                    config.set(key, defLand.get(key));
                    hasUpdated = true;
                }
            }
            for (String key : config.getConfigurationSection("").getKeys(true)) {
                if (!defLand.contains(key)) {
                    config.set(key, null);
                    hasUpdated = true;
                }
            }
            if (hasUpdated)
                config.save(cfile);
            is.close();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFile() {
        PROTOCOL_LIB_ENABLED = config.getBoolean("protocol-lib-enabled");
        HOLOGRAPHIC_DISPLAYS_ENABLED = config.getBoolean("holographic-displays-enabled");
        ELEMENTALS_ENABLED = config.getBoolean("elementals-enabled");
    }
}
