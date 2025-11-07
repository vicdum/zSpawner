package fr.maxlego08.spawner.storage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeSafeCache {
    private final Map<Class<?>, List<Object>> cache;

    public TypeSafeCache() {
        this.cache = new ConcurrentHashMap<>();
    }

    public void add(Object object) {
        Class<?> type = object.getClass();
        cache.computeIfAbsent(type, k -> new ArrayList<>()).add(object);
    }

    @SuppressWarnings("unchecked")
    public @NotNull <T> List<T> get(Class<T> type) {
        return (List<T>) this.cache.getOrDefault(type, new ArrayList<>());
    }

    public void clear(Class<?> type) {
        this.cache.put(type, new ArrayList<>());
    }

    public void clearAll() {
        this.cache.clear();
    }

    public boolean remove(Object object) {
        Class<?> type = object.getClass();
        List<Object> list = this.cache.get(type);
        if (list != null) {
            return list.remove(object);
        }
        return false;
    }
}

