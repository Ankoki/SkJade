package com.ankoki.skjade.elements.pastebinapi;

import com.ankoki.pastebinapi.api.PasteBuilder;
import com.ankoki.pastebinapi.enums.PasteExpiry;
import com.ankoki.pastebinapi.enums.PasteVisibility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PasteManager {
    private static final Map<String, PasteBuilder> pasteBuilders = new HashMap<>();

    public static void createPasteBuilder(String key) {
        PasteBuilder pasteBuilder = new PasteBuilder();
        pasteBuilders.put(key, pasteBuilder);
    }

    public static PasteBuilder getFromID(String key) {
        if (pasteBuilders.containsKey(key)) return pasteBuilders.get(key);
        return null;
    }

    public static void setExpires(PasteBuilder builder, PasteExpiry expire) {
        builder.setExpiry(expire);
        for (Map.Entry<String, PasteBuilder> entry : pasteBuilders.entrySet()) {
            if (builder == entry.getValue()) {
                pasteBuilders.remove(entry.getKey());
                pasteBuilders.put(entry.getKey(), builder);
            }
        }
    }

    public static void deletePaste(PasteBuilder... builders) {
        Arrays.stream(builders).forEach(builder -> {
            for (Map.Entry<String, PasteBuilder> entry : pasteBuilders.entrySet()) {
                if (builder == entry.getValue()) {
                    pasteBuilders.remove(entry.getKey());
                }
            }
        });
    }

    public static void resetPaste(PasteBuilder... builders) {
        Arrays.stream(builders).forEach(builder -> {
            for (Map.Entry<String, PasteBuilder> entry : pasteBuilders.entrySet()) {
                if (builder == entry.getValue()) {
                    pasteBuilders.remove(entry.getKey());
                    pasteBuilders.put(entry.getKey(), new PasteBuilder());
                }
            }
        });
    }

    public static void setVisibility(PasteBuilder builder, PasteVisibility visiblity) {
        builder.setVisibility(visiblity);
        for (Map.Entry<String, PasteBuilder> entry : pasteBuilders.entrySet()) {
            if (builder == entry.getValue()) {
                pasteBuilders.remove(entry.getKey());
                pasteBuilders.put(entry.getKey(), builder);
            }
        }
    }

    public static void setTitle(PasteBuilder builder, String title) {
        builder.setTitle(title);
        for (Map.Entry<String, PasteBuilder> entry : pasteBuilders.entrySet()) {
            if (builder == entry.getValue()) {
                pasteBuilders.remove(entry.getKey());
                pasteBuilders.put(entry.getKey(), builder);
            }
        }
    }

    public static void setText(PasteBuilder builder, String title) {
        builder.setText(title);
        for (Map.Entry<String, PasteBuilder> entry : pasteBuilders.entrySet()) {
            if (builder == entry.getValue()) {
                pasteBuilders.remove(entry.getKey());
                pasteBuilders.put(entry.getKey(), builder);
            }
        }
    }

    public static void setFormat(PasteBuilder builder, String format) {
        builder.setFormat(format);
        for (Map.Entry<String, PasteBuilder> entry : pasteBuilders.entrySet()) {
            if (builder == entry.getValue()) {
                pasteBuilders.remove(entry.getKey());
                pasteBuilders.put(entry.getKey(), builder);
            }
        }
    }
}
