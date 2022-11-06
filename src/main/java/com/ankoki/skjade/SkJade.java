package com.ankoki.skjade;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.ankoki.roku.bukkit.BukkitImpl;
import com.ankoki.roku.misc.Version;
import com.ankoki.roku.web.JSON;
import com.ankoki.roku.web.WebRequest;
import com.ankoki.roku.web.exceptions.MalformedJsonException;
import com.ankoki.skjade.commands.SkJadeCmd;
import com.ankoki.skjade.elements.lasers.Laser;
import com.ankoki.skjade.hooks.holograms.api.HoloHandler;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import com.ankoki.skjade.hooks.holograms.api.SKJHoloLine;
import com.ankoki.skjade.hooks.holograms.impl.decentholograms.DHProvider;
import com.ankoki.skjade.utils.*;
import com.ankoki.skjade.utils.events.RealTimeEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bstats.bukkit.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jdt.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SkJade extends JavaPlugin implements Listener {

    private static SkJade instance;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private Version version;
    private SkriptAddon addon;
    private boolean nmsEnabled = false;
    private boolean latest = true;
    private Config config = null;

    @Override
    public void onEnable() {
        final long start = System.currentTimeMillis();
        instance = this;
        new Metrics(this, 10131);
        version = Version.of(this.getDescription().getVersion());

        if (!Utils.isPluginEnabled("Skript") || !Skript.isAcceptRegistrations()) {
            this.getLogger().severe("Skript wasn't found, or isn't accepting registrations. Are you sure it's installed and up to date?");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        } else if (Skript.getVersion().isSmallerThan(new ch.njol.skript.util.Version("2.6"))) {
            this.getLogger().severe("Skript is running a version lower than 2.6, which is unsupported. Please update to 2.6 or above.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        } else if (!Utils.isPluginEnabled("Roku"))
            BukkitImpl.setupRoku(this);

        config = new Config(this);
        addon = Skript.registerAddon(this);
        this.loadNMS();

        if (!Config.MISC_ENABLED && !Config.BINFLOP_ENABLED && !Config.LASERS_ENABLED && !Config.HOLOGRAMS_ENABLED) {
            this.getLogger().severe("There are no SkJade elements enabled in the config. Disabling plugin.");
            this.getServer().getPluginManager().disablePlugin(this);
        }

        if (Config.MISC_ENABLED)
            this.loadMiscElements();
        else
            this.getLogger().warning("Misc elements not enabled in the config. Skipping...");

        if (Config.BINFLOP_ENABLED)
            this.loadBinflopElements();
        else
            this.getLogger().warning("Binflop elements not enabled in the config. Skipping...");

        if (Config.LASERS_ENABLED)
            this.loadLaserElements();
        else
            this.getLogger().warning("Laser elements not enabled in the config. Skipping...");

        if (Config.HOLOGRAMS_ENABLED) {
            HoloHandler.get().addProvider(DHProvider.get());
            if (Utils.isPluginEnabled(Config.HOLOGRAM_PLUGIN) && HoloHandler.get().hasProvider(Config.HOLOGRAM_PLUGIN)) {
                this.getLogger().info(Config.HOLOGRAM_PLUGIN + " was found! Enabling support.");
                HoloHandler.get().setCurrentProvider(HoloHandler.get().getProvider(Config.HOLOGRAM_PLUGIN));
                HoloHandler.get().getCurrentProvider().setup();
                this.loadHologramElements();
            } else
                this.getLogger().severe("'" + Config.HOLOGRAM_PLUGIN + "' was either not found, or there is no SkJade provider for it. Hologram elements will not be enabled.");
        } else
            this.getLogger().warning("Holographic elements not enabled in the config. Skipping...");

        this.registerListeners(this);
        this.getServer().getPluginCommand("skjade").setExecutor(new SkJadeCmd());

        final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            String version = "INVALID";
            try {
                final WebRequest request = new WebRequest("https://api.github.com/repos/Ankoki/SkJade/releases/latest", WebRequest.RequestType.GET);
                final Optional<String> optional = request.execute();
                if (optional.isPresent()) {
                    try {
                        final JSON json = new JSON(optional.get());
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
            final Version latest = Version.of(version);
            this.latest = latest.isNewerThan(this.version);
            if (!this.latest)
                this.getLogger().warning("You are due an update for SkJade. The latest version is v" + version + "! " +
                    "Find it at https://www.github.com/Ankoki/SkJade/releases/latest/");
            return false;
        });

        final long fin = System.currentTimeMillis() - start;
        this.getLogger().info("SkJade v" + version + " has been successfully enabled in " + df.format(fin / 1000.0) + " seconds (" +
                fin + "ms)");
    }

    private void loadNMS() {
        if (MinecraftVersion.CURRENT_VERSION == MinecraftVersion.UNKNOWN || MinecraftVersion.currentIsLegacy()) {
            this.getLogger().warning("Could not find any NMS support for " + version + "! Please note SkJade only supports " +
                    "the latest sub-version of each version above 1.13.");
            this.getLogger().warning("There is also a chance you are using a version I haven't implemented support for yet.");
            this.getLogger().warning("SkJade will remain enabled, however anything using NMS will not be enabled!");
        } else
            nmsEnabled = true;
    }

    private void loadMiscElements() {
        if (Utils.getMinecraftMinor() > 12)
            NonLegacyClassInfo.register();
        try {
            addon.loadClasses("com.ankoki.skjade.elements",
                    "expressions",
                    "effects",
                    "events",
                    "conditions");
            RealTimeEvent.register();
        } catch (IOException ex) {
            this.getLogger().severe("Something went wrong loading the misc elements.");
            ex.printStackTrace();
        }
    }

    private void loadBinflopElements() {
        try {
            addon.loadClasses("com.ankoki.skjade.elements", "binflop");
        } catch (IOException ex) {
            this.getLogger().severe("Something went wrong loading the binflop elements.");
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
            this.getLogger().severe("Something went wrong loading the laser elements.");
            ex.printStackTrace();
        }
    }

    private void loadHologramElements() {
        Classes.registerClass(new ClassInfo<>(SKJHolo.class, "skjholo")
                .user("skjholo%s%")
                .name("Hologram")
                .description("An SkJade hologram.")
                .since("2.0")
                .parser(new Parser<>() {
                    @Override
                    public String toString(SKJHolo holo, int i) {
                        return "SkJadeHologram-" + holo.getKey();
                    }

                    @Override
                    public String toVariableNameString(SKJHolo holo) {
                        return "SkJadeHologram-" + holo.getKey();
                    }

                    @Override
                    public @Nullable SKJHolo parse(String unparsed, ParseContext context) {
                        return HoloHandler.get().getHolo(unparsed);
                    }
                }));
        Classes.registerClass(new ClassInfo<>(SKJHoloLine.class, "skjhololine")
                .user("skjhololine%s%")
                .name("Hologram Line")
                .description("An SkJade hologram line.")
                .since("2.0"));
        try {
            addon.loadClasses("com.ankoki.skjade.hooks.holograms");
        } catch (IOException ex) {
            this.getLogger().severe("Something went wrong loading the holographic elements.");
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

    public Version getVersion() {
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

    public boolean copyTests() {
        File file = new File(Skript.getInstance().getDataFolder() + File.separator + "scripts", "skjade-tests.sk");
        if (!file.exists()) {
            try {
                InputStream in = this.getClassLoader().getResourceAsStream("/skjade-tests.sk");
                if (in == null)
                    throw new IllegalAccessException("Required resource 'skjade-tests.sk' does not exist! Please report this.");
                Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return true;
            } catch (IllegalAccessException | IOException ex) {
                ex.printStackTrace();
                return false;
            }
        } else {
            this.getLogger().severe("Command '/skjade copy-tests' failed as 'skjade-tests.sk' already exists in your scripts folder!");
            return false;
        }
    }

    public Config getOwnConfig() {
        return config;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Config.VERSION_ALERTS && player.hasPermission("skjade.notify") && !SkJade.getInstance().isLatest()) {
            player.sendMessage("§fSk§aJade §f| §7§oYou are running an outdated version of §f§oSk§a§oJade§7§o!");
            TextComponent github = new TextComponent("§fSk§aJade §f| §7§oClick me to download the latest version!");
            github.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("Click me to go to the latest release!")
                            .color(ChatColor.GRAY)
                            .italic(true)
                            .create()));
            github.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                    "https://www.github.com/Ankoki/SkJade/releases/latest"));
            player.spigot().sendMessage(github);
        }
    }
}