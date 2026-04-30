package com.ancientmc.commons.type;

import com.ancientmc.commons.Util;
import org.jspecify.annotations.NonNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A registry, in this library, is a map-like object with string keys.
 * This is useful for sorting through collections of immutable objects.
 * This is a ridiculously simplified version of Minecraft's own registry system that handles objects in the game.
 *
 * <p> See also:
 * <a href="https://docs.neoforged.net/docs/concepts/registries/">Minecraft registry documentation, provided by NeoForged.</a>
 * </p>
 *
 * @author moist-mason
 *
 * @param <T> The registry type.
 */
public class Registry<T> implements Iterable<Registry.Entry<T>> {

    /** The entry set. Since this is a set, all entries in the registry are unique. */
    private final Set<Entry<T>> entries;

    public Registry() {
        this.entries = new HashSet<>();
    }

    /**
     * Adds an entry to the registry.
     *
     * @param key The key.
     * @param value The value.
     * @return The value.
     */
    public T register(final String key, final T value) {
        final Entry<T> entry = new Entry<>(key, value);
        entries.add(entry);
        return value;
    }

    /**
     * Removes an entry from the registry.
     *
     * @param key The key.
     * @return The value.
     */
    public T unregister(final String key) {
        final Entry<T> entry = Util.get(getEntry(key));
        entries.remove(entry);
        return entry.value();
    }

    /**
     * Retrieves the object with the key.
     *
     * @param key The key.
     * @return The object.
     */
    public T get(final String key) {
        final Entry<T> entry = Util.get(getEntry(key));
        return entry.value();
    }

    /**
     * Retrieves the key with the object.
     *
     * @param value The object.
     * @return The key.
     */
    public String getKey(final T value) {
        final Entry<T> entry = Util.get(getEntry(value));
        return entry.key();
    }

    /**
     * Checks if an entry with the given key is in the registry.
     *
     * @param key The key.
     * @return The entry, if present.
     */
    public Entry<T> getEntry(final String key) {
        return Util.findAny(entries, e -> e.key.equals(key));
    }

    /**
     * Checks if an entry with the given value is in the registry.
     *
     * @param value The value.
     * @return The entry, if present.
     */
    public Entry<T> getEntry(final T value) {
        return Util.findAny(entries, e -> e.value().equals(value));
    }

    /**
     * Checks if the registry contains an entry with the given key.
     *
     * @param key The key.
     * @return {@code true} if the registry has an entry with the key.
     */
    public boolean containsKey(final String key) {
        return Util.anyMatch(entries, e -> e.key.equals(key));
    }

    /**
     * Checks if the registry contains an entry with the given value.
     *
     * @param value The value.
     * @return {@code true} if the registry has an entry with the value.
     */
    public boolean containsValue(final T value) {
        return Util.anyMatch(entries, e -> e.value().equals(value));
    }

    /** @return The size of the registry. */
    public int size() {
        return entries.size();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (!(o instanceof Registry<?> registry)) {
            return false;
        } else {
            return entries.equals(registry.entries);
        }
    }

    @NonNull
    @Override
    public Iterator<Entry<T>> iterator() {
        return entries.iterator();
    }

    @Override
    public void forEach(Consumer<? super Entry<T>> action) {
        Iterable.super.forEach(action);
    }

    /**
     * An entry in the registry. Similar to {@link Map.Entry}.
     *
     * @param key The key.
     * @param value The value.
     * @param <T> The value type.
     */
    public record Entry<T>(String key, T value) { }
}
