package com.ankoki.skjade.hooks.holograms;

import com.ankoki.skjade.hooks.holograms.api.HoloProvider;
import com.ankoki.skjade.hooks.holograms.api.SKJHolo;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HoloManager {

    private static final HoloManager HANDLER = new HoloManager();

    public static HoloManager get() {
        return HANDLER;
    }

    private final Map<String, SKJHolo> storage = new ConcurrentHashMap<>();
    private final List<HoloProvider> providers = new ArrayList<>();

    private HoloProvider provider;

    /**
     * Gets a hologram by key.
     * @param key the key.
     * @return the hologram under the given key, null if not found.
     */
    public @Nullable SKJHolo getHolo(String key) {
        return storage.getOrDefault(key, null);
    }

    /**
     * Registers a hologram under a given key.
     * @param key the key to register under. Must not already be registered.
     * @param holo the hologram to register.
     * @return if registering was successful.
     */
    public boolean registerHolo(String key, SKJHolo holo) {
        if (storage.containsKey(key)) return false;
        storage.put(key, holo);
        return true;
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
}
