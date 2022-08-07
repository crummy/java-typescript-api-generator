package com.malcolmcrum.typescriptapigenerator.typescriptgenerator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// Converts a Java type to a TypeScript equivalent. Sample input -> output:
// int, "prefix" -> number
// Integer, "prefix" -> number
// Object, "prefix" -> any
// com.foo.Bar, "prefix" -> prefix.Bar
// List<com.foo.Bar", -> List<prefix.Bar>
public class TypeScriptConverter {

    private static final Map<Class<?>, String> overrides = new HashMap<>();

    static {
        overrides.put(String.class, "string");
        overrides.put(int.class, "number");
        overrides.put(Integer.class, "number");
        overrides.put(float.class, "number");
        overrides.put(Float.class, "number");
        overrides.put(double.class, "number");
        overrides.put(Double.class, "number");
        overrides.put(long.class, "number");
        overrides.put(Long.class, "number");
        overrides.put(Object.class, "any");
    }

    public static String getTsType(Type type, String prefix) {
        if (type instanceof Class) {
            //noinspection rawtypes
            return getTsType((Class) type, prefix);
        }

        if (type instanceof ParameterizedType pt) {
            StringBuilder typeName = new StringBuilder(getTsType(pt.getRawType(), prefix));

            Type[] specificTypes = pt.getActualTypeArguments();
            if (null != specificTypes && 0 < specificTypes.length) {
                typeName.append("<");
                for (int j = 0; j < specificTypes.length; ++j) {
                    if (j > 0) {
                        typeName.append(",");
                    }

                    typeName.append(getTsType(specificTypes[j], prefix));
                }

                typeName.append(">");
            }

            return typeName.toString();
        }

        // or it could be a Something<?>
        if (type instanceof WildcardType) {
            return "any";
        }
        throw new RuntimeException("Couldn't figure out type for " + type);
    }

    private static String getTsType(@SuppressWarnings("rawtypes") Class clazz, String prefix) {
        if (clazz == null) {
            return "";
        }

        String override = overrides.get(clazz);
        if (override != null) {
            return overrides.get(clazz);
        }

        if (clazz.isPrimitive()) {
            return clazz.getSimpleName();
        }
        if (clazz.getName().equals("java.lang.Void")) {
            return "void";
        }

        if (clazz.isArray()) {
            return "Array<" + getTsType(clazz.getComponentType(), prefix) + ">";
        }

        if (Collection.class.isAssignableFrom(clazz)) {
            return "Array" + getTsType(clazz.getComponentType(), prefix);
        } else if (Map.class.isAssignableFrom(clazz)) {
            return "Record" + getTsType(clazz.getComponentType(), prefix);
        }
        return prefix + "." + clazz.getSimpleName();
    }
}
