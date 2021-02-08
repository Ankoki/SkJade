package com.ankoki.skjade.elements.pastebinapi;

import com.ankoki.skjade.SkJade;
import com.besaba.revonline.pastebinapi.paste.PasteBuilder;
import com.besaba.revonline.pastebinapi.paste.PasteExpire;
import com.besaba.revonline.pastebinapi.paste.PasteVisiblity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PasteManager {
    private static final Map<String, PasteBuilder> pasteBuilders = new HashMap<>();

    public static void createPasteBuilder(String key) {
        PasteBuilder pasteBuilder = SkJade.getFactory().createPaste();
        pasteBuilders.put(key, pasteBuilder);
    }

    public static PasteBuilder getFromID(String key) {
        if (pasteBuilders.containsKey(key)) return pasteBuilders.get(key);
        return null;
    }

    public static void setExpires(PasteBuilder builder, PasteExpire expire) {
        builder.setExpire(expire);
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
                    pasteBuilders.put(entry.getKey(), SkJade.getFactory().createPaste());
                }
            }
        });
    }

    public static void setVisibility(PasteBuilder builder, PasteVisiblity visiblity) {
        builder.setVisiblity(visiblity);
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
        builder.setRaw(title);
        for (Map.Entry<String, PasteBuilder> entry : pasteBuilders.entrySet()) {
            if (builder == entry.getValue()) {
                pasteBuilders.remove(entry.getKey());
                pasteBuilders.put(entry.getKey(), builder);
            }
        }
    }
}
