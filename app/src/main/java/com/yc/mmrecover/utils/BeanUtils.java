package com.yc.mmrecover.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class BeanUtils {
    private BeanUtils() {
    }

    public static void setFieldValue(Object obj, String str, Object obj2) {
        Field declaredField = getDeclaredField(obj, str);
        if (declaredField != null) {
            makeAccessible(declaredField);
            try {
                declaredField.set(obj, obj2);
            } catch (IllegalAccessException unused) {
                return;
            }finally {
                return;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Could not find field [");
        stringBuilder.append(str);
        stringBuilder.append("] on target [");
        stringBuilder.append(obj);
        stringBuilder.append("]");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    protected static Field getDeclaredField(Object obj, String str) {
        return getDeclaredField(obj.getClass(), str);
    }

    protected static Field getDeclaredField(Class cls, String str) {
        while (cls != Object.class) {
            try {
                return cls.getDeclaredField(str);
            } catch (NoSuchFieldException unused) {
                cls = cls.getSuperclass();
            }
        }
        return null;
    }

    protected static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

}
