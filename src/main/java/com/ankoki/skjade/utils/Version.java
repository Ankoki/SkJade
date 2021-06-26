package com.ankoki.skjade.utils;

public enum Version {
    v1_7_R1(true),
    v1_7_R2(true),
    v1_7_R3(true),
    v1_7_R4(true),
    v1_8_R1(true),
    v1_9_R1(true),
    v1_10_R1(true),
    v1_11_R1(true),
    v1_12_R1(true),
    v1_12_R2(true),
    v1_13_R1(false),
    v1_14_R1(false),
    v1_15_R1(false),
    v1_16_R1(false),
    v1_16_R2(false),
    v1_16_R3(false),
    v1_16_R4(false),
    UNKNOWN(false);

    private final boolean legacy;
    Version(boolean legacy) {
        this.legacy = legacy;
    }

    public boolean isLegacy() {
        return legacy;
    }
}
