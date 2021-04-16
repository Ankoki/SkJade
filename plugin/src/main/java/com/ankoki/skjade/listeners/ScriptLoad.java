package com.ankoki.skjade.listeners;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.events.bukkit.PreScriptLoadEvent;
import com.ankoki.skjade.SkJade;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;
import java.util.Map;

public class ScriptLoad implements Listener {

    @EventHandler
    private void getOptions(PreScriptLoadEvent e) {
        try {
            Class<?> clazz = ScriptLoader.class;
            Field field = clazz.getDeclaredField("currentOptions");
            field.setAccessible(true);
            Map<String, String> scriptOptions = (Map<String, String>) field.get(null);
            SkJade.getInstance().allOptions.put(e.getScripts().get(0).getFileName(), scriptOptions);
            /*for (Map.Entry<String, String> entry : scriptOptions.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
