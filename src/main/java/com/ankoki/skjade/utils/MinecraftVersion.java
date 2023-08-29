package com.ankoki.skjade.utils;

import com.ankoki.skjade.SkJade;

public enum MinecraftVersion {

    v1_7_R1,
    v1_7_R2,
    v1_7_R3,
    v1_7_R4,
    v1_8_R1,
    v1_9_R1,
    v1_10_R1,
    v1_11_R1,
    v1_12_R1,
    v1_12_R2,
    v1_13_R1,
    v1_14_R1,
    v1_15_R1,
    v1_16_R1,
    v1_16_R2,
    v1_16_R3,
    v1_16_R4,
    v1_17_R1,
    v1_18_R1,
    v1_18_R2,
    v1_19_R1,
    v1_19_R2,
    v1_19_R3,
    v1_20_R1,
    v1_20_R2,
    UNKNOWN;


    // Can't be final.
    public static MinecraftVersion CURRENT_VERSION;

    static {
        String packageName = SkJade.getInstance().getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        try {
            CURRENT_VERSION = MinecraftVersion.valueOf(version);
        } catch (IllegalArgumentException ex) {
            CURRENT_VERSION = UNKNOWN;
            SkJade.getInstance().getLogger().severe("You are using an unknown version (" + version + "). You could be using a version before 1.7, or a newer version I haven't supported.");
        }
    }

    /**
     * Checks if the current version is a legacy version.
     *
     * @return true if legacy.
     */
    public static boolean currentIsLegacy() {
        return CURRENT_VERSION.isLegacy();
    }

    private final boolean legacy = this.ordinal() < 10;

    /**
     * Checks if the version is legacy.
     *
     * @return true if legacy.
     */
    public boolean isLegacy() {
        return legacy;
    }

    /**
     * Checks if the version is newer than the current.
     *
     * @return true if newer.
     */
    public boolean isNewer() {
        return this.ordinal() > CURRENT_VERSION.ordinal();
    }

    /**
     * Checks if the version is older than the current.
     *
     * @return true if older.
     */
    public boolean isOlder() {
        return this.ordinal() < CURRENT_VERSION.ordinal();
    }

}
