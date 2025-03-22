package reghzy.witt.utils;

import net.minecraft.src.ModLoader;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {
    public static Method getMd(String clazz, String md, Class<?>... params) {
        try {
            Class<?> klass = Class.forName(clazz, false, ModLoader.class.getClassLoader());
            for (Class<?> theClass = klass; theClass != null; theClass = theClass.getSuperclass()) {
                try {
                    Method m = theClass.getDeclaredMethod(md, params);
                    m.setAccessible(true);
                    return m;
                }
                catch (NoSuchMethodException ignored) { }
            }
        }
        catch (Throwable ignored) {}

        return null;
    }

    public static Field findField(String clazz, String field) {
        try {
            Class<?> klass = Class.forName(clazz, false, ModLoader.class.getClassLoader());
            for (Class<?> theClass = klass; theClass != null; theClass = theClass.getSuperclass()) {
                try {
                    Field f = theClass.getDeclaredField(field);
                    f.setAccessible(true);
                    return f;
                }
                catch (NoSuchFieldException ignored) {}
            }
        }
        catch (Throwable ignored) { }

        return null;
    }

    public static Object invokeMd(Method method, Object instance, Object... params) {
        try {
            return method.invoke(instance, params);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getFieldObj(Field field, Object instance) {
        try {
            return field.get(instance);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
