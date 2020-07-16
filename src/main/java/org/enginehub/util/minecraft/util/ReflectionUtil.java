package org.enginehub.util.minecraft.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

    public static Object getField(Object obj, Class<?> clazz, String name) {
        try {
            Field f = clazz.getDeclaredField(name);
            f.setAccessible(true);
            return f.get(obj);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invokeMethod(Object obj, Class<?> clazz, String name, Class<?>[] paramClasses, Object[] params) {
        try {
            Method m = clazz.getDeclaredMethod(name, paramClasses);
            m.setAccessible(true);
            return m.invoke(obj, params);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
