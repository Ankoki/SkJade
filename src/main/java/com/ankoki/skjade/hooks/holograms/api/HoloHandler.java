package com.ankoki.skjade.hooks.holograms.api;

import com.ankoki.skjade.SkJade;
import com.ankoki.skjade.hooks.holograms.api.events.HologramInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HoloHandler implements Listener {

    private static final HoloHandler HANDLER = new HoloHandler();

    static {
        Bukkit.getPluginManager().registerEvents(HANDLER, SkJade.getInstance());
    }

    public static HoloHandler get() {
        return HANDLER;
    }

    private final Map<String, SKJHolo> storage = new ConcurrentHashMap<>();
    private final Map<SKJHolo, List<HologramTrigger>> triggers = new ConcurrentHashMap<>();
    private final List<HoloProvider> providers = new ArrayList<>();

    private HoloProvider provider;

    /**
     * Gets a hologram by key.
     * @param key the key.
     * @return the hologram under the given key, null if not found.
     */
    public @Nullable SKJHolo getHolo(String key) {
        return storage.get(key);
    }

    /**
     * Registers a hologram under a given key.
     * @param key the key to register under. Must not already be registered.
     * @param holo the hologram to register.
     * @return if registering was successful.
     */
    public boolean registerHolo(String key, SKJHolo holo) {
        if (key == null || storage.containsKey(key))
            return false;
        storage.put(key, holo);
        return true;
    }

    /**
     * Returns whether a key is already in use.
     * @param key the key to check.
     * @return true if it is in use, else false.
     */
    public boolean keyInUse(String key) {
        return key == null || storage.containsKey(key);
    }

    /**
     * Removes a hologram from the temporary cache.
     * @param key the key to delete.
     */
    public void deleteHolo(String key) {
        storage.remove(key);
    }

    /**
     * Adds an interaction to the given SKJHolo.
     * @param holo the hologram to register an interaction for.
     * @param trigger the trigger to execute.
     */
    public void registerInteraction(SKJHolo holo, HologramTrigger trigger) {
        final List<HologramTrigger> cache = triggers.getOrDefault(holo, new ArrayList<>());
        cache.add(trigger);
        this.triggers.put(holo, cache);
    }

    /**
     * Attempts to execute an interaction for a hologram with certain criteria.
     * @param holo the hologram that has been clicked.
     * @param page the page that has been clicked.
     * @param line the line that has been clicked.
     * @param type the click type.
     * @param event the event to run.
     */
    public void executeInteraction(SKJHolo holo, int page, int line, ClickType type, HologramInteractEvent event) {
        final List<HologramTrigger> cache = this.triggers.getOrDefault(holo, new ArrayList<>());
        for (final HologramTrigger trigger : cache)
            trigger.execute(page, line, type, event);
    }

    /**
     * Check if a hologram has any interactions linked.
     * @param holo the hologram to check.
     * @return true if the hologram has interactions to run.
     */
    public boolean hasInteractions(SKJHolo holo) {
        final List<HologramTrigger> cache = this.triggers.getOrDefault(holo, new ArrayList<>());
        return !triggers.isEmpty();
    }

    /**
     * Returns all holograms in the cache.
     * @return the registered holograms.
     */
    public Collection<SKJHolo> getHolograms() {
        return this.storage.values();
    }

    //<editor-fold desc="Providers" defaultstate="collapsed">
    /**
     * Checks if SkJade has a hologram provider registered under the given id.
     * @param id the id.
     * @return if a provider is registered.
     */
    public boolean hasProvider(String id) {
        for (HoloProvider h : this.providers) {
            if (id.equalsIgnoreCase(h.getId())) return true;
        } return false;
    }

    /**
     * Adds a new hologram provider. Must have a unique id.
     * @param provider the new provider.
     */
    public void addProvider(HoloProvider provider) {
        String id = provider.getId();
        for (HoloProvider h : this.providers) {
            if (id.equalsIgnoreCase(h.getId())) return;
        } this.providers.add(provider);
    }

    /**
     * Gets a provider by id.
     * @param id the id of the wanted provider.
     */
    public HoloProvider getProvider(String id) {
        for (HoloProvider provider : this.providers) {
            if (id.equalsIgnoreCase(provider.getId())) return provider;
        } return null;
    }

    /**
     * Sets the provider SkJade should use.
     * @param provider the provider to use.
     */
    public void setCurrentProvider(HoloProvider provider) {
        this.provider = provider;
    }

    /**
     * Gets the current hologram provider.
     * @return the current provider.
     */
    public HoloProvider getCurrentProvider() {
        return provider;
    }
    //</editor-fold>

    @EventHandler
    private void onHoloInteract(HologramInteractEvent event) {
        if (this.hasInteractions(event.getHologram()))
            this.executeInteraction(event.getHologram(), event.getPage(), event.getLine(), event.getType(), event);
    }
}
