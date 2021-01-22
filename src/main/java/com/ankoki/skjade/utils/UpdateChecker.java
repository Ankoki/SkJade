package com.ankoki.skjade.utils;

import com.ankoki.skjade.SkJade;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker {
    private final String user;
    private final String repo;
    private String latestTag;
    private boolean isLatest;

    public UpdateChecker(String user, String repo) {
        this.user = user;
        this.repo = repo;
        this.getLatestTag(true);
    }

    public String getLatestTag(boolean check) {
        if (check || latestTag.isEmpty()) {
            String redirectedURL = "";
            try {
                URLConnection con = new URL("https://github.com/Ankoki-Dev/Elementals/releases/latest").openConnection();
                con.connect();
                InputStream is = con.getInputStream();
                redirectedURL = con.getURL().toString();
                is.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                String tag = redirectedURL.split("releases/tag/")[1];
                isLatest = tag.equalsIgnoreCase(SkJade.getInstance().getDescription().getVersion());
                latestTag = tag;
                return tag;
            } catch (ArrayIndexOutOfBoundsException ex) {
                isLatest = false;
                return "UNKNOWN";
            }
        }
        return latestTag;
    }

    public boolean isOutdated() {
        return isLatest;
    }
}
