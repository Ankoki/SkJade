package com.ankoki.skjade.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {

    private static final String NEW_PACKAGE = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName()
            .replace(".", ",").split(",")[3];
    public static Method getHandle;
    public static Field playerConnection;
    public static Method sendPacket;

    static {
        try {
            getHandle = Class.forName(Bukkit.getServer().getClass().getPackage().getName() + ".entity.CraftPlayer")
                    .getDeclaredMethod("getHandle");
            playerConnection = getNMSClass("server.level", "EntityPlayer")
                    .getDeclaredField(Utils.getMinecraftMinor() < 17 ? "playerConnection" : "b");
            sendPacket = getNMSClass("server.network", "PlayerConnection")
                    .getMethod(Utils.getMinecraftMinor() < 18 ? "sendPacket" : "a", getNMSClass("network.protocol", "Packet"));
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets a player's craft handle.
     *
     * @param player the player.
     * @return the handle.
     */
    @Nullable
    public static Object getHandle(Player player) {
        try {
            return getHandle.invoke(player);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Sends packets to a given player.
     *
     * @param player the player to send the packets too.
     * @param packets the packets to send.
     */
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

    /**
     * Gets a method from its name.
     *
     * @param clazz the class to look in.
     * @param name the name to look for.
     * @return the method, or null.
     */
    @Nullable
    public static Method getMethod(Class<?> clazz, String name) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(name)) return m;
        }
        return null;
    }

    /**
     * Gets a field value from an instance and given name.
     *
     * @param clazz the class to look in.
     * @param name the name to look for.
     * @param instance the instance to search in, null if static.
     * @return the field value.
     * @throws ReflectiveOperationException if not found.
     */
    public static Object getField(Class<?> clazz, String name, Object instance) throws ReflectiveOperationException {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(instance);
    }

    /**
     * Gets a field value from a given name and instance.
     *
     * @param instance the instance to look in, null if static.
     * @param name the name to look for.
     * @return the field value.
     * @throws ReflectiveOperationException if not found.
     */
    public static Object getField(Object instance, String name) throws ReflectiveOperationException {
        Field field = instance.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(instance);
    }

    /**
     * Sets the field with the given name with the given value.
     *
     * @param instance the instance to set in, null if static.
     * @param name the name of the field.
     * @param value the value to set it too.
     */
    public static void setField(Object instance, String name, Object value) {
        try {
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the NMS class, including version dependencies.
     *
     * @param package17 the package on 1.17 and above.
     * @param className the name of the class.
     * @return the class instance, or null if not found.
     */
    public static Class<?> getNMSClass(String package17, String className) {
        try {
            return Class.forName((Utils.getMinecraftMinor() < 17 ?
                    NEW_PACKAGE : "net.minecraft." + package17) + "." + className);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
