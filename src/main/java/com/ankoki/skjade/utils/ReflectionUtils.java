package com.ankoki.skjade.utils;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static Method getHandle;
    public static Field playerConnection;
    public static Method sendPacket;

    static {
        try {
            getHandle = Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".entity.CraftPlayer")
                    .getDeclaredMethod("getHandle");
            playerConnection = getNMSClass("server.level", "EntityPlayer")
                    .getDeclaredField(Utils.getServerMajorVersion() < 17 ? "playerConnection" : "b");
            sendPacket = getNMSClass("server.network", "PlayerConnection")
                    .getMethod("sendPacket", getNMSClass("network.protocol", "Packet"));
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }

    public static void sendPacket(Player player, Object... packets) {
        try {
            Object connection = playerConnection.get(getHandle.invoke(player));
            for (Object packet : packets) {
                sendPacket.invoke(connection, packet);
            }
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }

    public static Method getMethod(Class<?> clazz, String name) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(name)) return m;
        }
        return null;
    }

    public static void setField(Object instance, String name, Object value) throws ReflectiveOperationException {
        Validate.notNull(instance);
        Field field = instance.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(instance, value);
    }

    public static Object getField(Class<?> clazz, String name, Object instance) throws ReflectiveOperationException {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(instance);
    }

    public static Object getField(Object instance, String name) throws ReflectiveOperationException {
        Field field = instance.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(instance);
    }

    public static Class<?> getNMSClass(String package17, String className) {
        String npack = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName()
                .replace(".", ",").split(",")[3];
        try {
            return Class.forName((Utils.getServerMajorVersion() < 17 ?
                    npack : "net.minecraft." + package17) + "." + className);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
