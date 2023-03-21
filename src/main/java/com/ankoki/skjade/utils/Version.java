package com.ankoki.skjade.utils;

import com.ankoki.skjade.SkJade;

public enum Version {

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
    UNKNOWN;


    // Can't be final.
    public static Version CURRENT_VERSION;

    static {
        String packageName = SkJade.getInstance().getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        try {
            CURRENT_VERSION = Version.valueOf(version);
        } catch (IllegalArgumentException ex) {
            CURRENT_VERSION = UNKNOWN;
            System.err.println("You are using an unknown version (" + version + "). You could be using a version before 1.7, or a newer version I haven't supported.");
        }
    }

    public static boolean currentIsLegacy() {
        return CURRENT_VERSION.legacy;
    }

    private final boolean legacy = this.ordinal() < 10;

    public boolean isLegacy() {
        return legacy;
    }

    public boolean isNewer() {
        return this.ordinal() > CURRENT_VERSION.ordinal();
    }

    public boolean isOlder() {
        return this.ordinal() < CURRENT_VERSION.ordinal();
    }

    public boolean equals(Version version) {
        return this.ordinal() == version.ordinal();
    }
}
