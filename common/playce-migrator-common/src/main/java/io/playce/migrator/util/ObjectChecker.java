package io.playce.migrator.util;

public abstract class ObjectChecker {

    public static void requireNonNull(Object obj, RuntimeException e) {
        if (obj == null) {
            throw e;
        }
    }

    public static <T> void requireSameClass(Object obj, Class<T> clazz, RuntimeException e) {
        if (obj.getClass() != clazz) {
            throw e;
        }
    }

    public static <T> void requireNotSameClass(Object obj, Class<T> clazz, RuntimeException e) {
        if (obj.getClass() == clazz) {
            throw e;
        }
    }

    public static <T> void requireTrue(boolean contains,RuntimeException e) {
        if (!contains) {
            throw e;
        }
    }
}
