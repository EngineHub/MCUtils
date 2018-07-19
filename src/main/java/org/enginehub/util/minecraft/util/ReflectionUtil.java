package org.enginehub.util.minecraft.util;

import java.lang.reflect.Field;

public class ReflectionUtil {

    public static Object getField(Object obj, Class<?> clazz, String name, String obfName) {
        try {
            Field f;
            try {
                f = clazz.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                f = clazz.getDeclaredField(obfName);
            }
            if (f == null) return null;
            f.setAccessible(true);
            return f.get(obj);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {
        }
        return null;
    }
}
