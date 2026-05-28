package com.github.moistmason.commons.type;

import com.github.moistmason.commons.StringUtil;
import com.github.moistmason.commons.Util;
import com.mojang.serialization.Codec;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A dictionary, in this library, is a map-like object with string keys.
 * This is useful for sorting through collections of immutable objects.
 * This was inspired Minecraft's registry system that handles objects in the game.
 *
 * <p> See also:
 * <a href="https://docs.neoforged.net/docs/concepts/registries/">Minecraft registry documentation, provided by NeoForged.</a>
 * </p>
 *
 * @author moist-mason
 *
 * @param <T> The dictionary type.
 */
public class Dictionary<T> implements Iterable<Dictionary.Entry<T>> {

    /**
     * Codec representation of a dictionary.
     *
     * @param <T> The value type.
     * @param valueCodec The codec of the dictionary's value type.
     * @return The codec.
     */
    public static <T> Codec<Dictionary<T>> codec(final Codec<T> valueCodec) {
        return Codec.unboundedMap(Codec.STRING, valueCodec).xmap(Dictionary::fromMap, Dictionary::toMap);
    }

    /** The entry set. Since this is a set, all entries in the dictionary are unique. */
    private final Set<Entry<T>> entries;

    /** The keys in this dictionary. */
    private final Set<String> keys;

    /** The values in this dictionary. */
    private final List<T> values;

    private Dictionary() {
        this.entries = new LinkedHashSet<>();
        this.keys = new LinkedHashSet<>();
        this.values = new LinkedList<>();
    }

    /**
     * Creates an empty dictionary.
     *
     * @param <T> The dictionary value type.
     * @return The dictionary.
     */
    public static <T> Dictionary<T> create() {
        return new Dictionary<>();
    }

    /**
     * Creates a dictionary with predefined values fed into a consumer.
     *
     * @param <T> The dictionary value type.
     * @param consumer The consumer.
     * @return The dictionary.
     */
    public static <T> Dictionary<T> create(final Consumer<? super Dictionary<T>> consumer) {
        final Dictionary<T> dictionary = new Dictionary<>();
        consumer.accept(dictionary);
        return dictionary;
    }

    /**
     * Creates a dictionary from a map.
     *
     * @param <T> The dictionary value type.
     * @param map The map.
     * @return The dictionary.
     */
    public static <T> Dictionary<T> fromMap(final Map<String, T> map) {
        return Dictionary.create(dict -> {
            for (Map.Entry<String, T> entry : map.entrySet()) {
                dict.add(entry.getKey(), entry.getValue());
            }
        });
    }

    /**
     * Adds an entry to the dictionary.
     *
     * @param key The key.
     * @param value The value.
     * @return The value.
     */
    public T add(final String key, final T value) {
        final Entry<T> entry = new Entry<>(key, value);
        entries.add(entry);
        keys.add(key);
        values.add(value);
        return value;
    }

    /**
     * Removes an entry from the dictionary.
     *
     * @param key The key.
     * @return The value.
     */
    public T remove(final String key) {
        final Entry<T> entry = Util.get(getEntry(key));
        entries.remove(entry);
        keys.remove(key);
        values.remove(entry.value());
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
     * Checks if an entry with the given key is in the dictionary.
     *
     * @param key The key.
     * @return The entry, if present.
     */
    public Entry<T> getEntry(final String key) {
        return Util.findAny(entries, e -> e.key.equals(key));
    }

    /**
     * Checks if an entry with the given value is in the dictionary.
     *
     * @param value The value.
     * @return The entry, if present.
     */
    public Entry<T> getEntry(final T value) {
        return Util.findAny(entries, e -> e.value().equals(value));
    }

    /**
     * Checks if the dictionary contains an entry with the given key.
     *
     * @param key The key.
     * @return {@code true} if the dictionary has an entry with the key.
     */
    public boolean containsKey(final String key) {
        return Util.anyMatch(entries, e -> e.key.equals(key));
    }

    /**
     * Checks if the dictionary contains an entry with the given value.
     *
     * @param value The value.
     * @return {@code true} if the dictionary has an entry with the value.
     */
    public boolean containsValue(final T value) {
        return Util.anyMatch(entries, e -> e.value().equals(value));
    }

    /**
     * Gets the keys of the dictionary as a set.
     *
     * @return The key set.
     */
    public Set<String> keys() {
        return keys;
    }

    /**
     * Gets the values of the dictionary as a list.
     *
     * @return The value list.
     */
    public List<T> values() {
        return values;
    }

    /**
     * Gets the index of this value.
     *
     * @param value The value.
     * @return The index.
     */
    public int indexOf(final T value) {
        return values().indexOf(value);
    }

    /** @return The size of the dictionary. */
    public int size() {
        return entries.size();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (!(o instanceof Dictionary<?> dictionary)) {
            return false;
        } else {
            return entries.equals(dictionary.entries);
        }
    }

    @Override
    public @NonNull Iterator<Entry<T>> iterator() {
        return entries.iterator();
    }

    @Override
    public void forEach(Consumer<? super Entry<T>> action) {
        Iterable.super.forEach(action);
    }

    /**
     * Gets a stream of this dictionary's values.
     *
     * @return The stream.
     */
    public Stream<T> stream() {
        return values.stream();
    }

    /**
     * Gets a stream of this dictionary's entries.
     *
     * @return The stream.
     */
    public Stream<Entry<T>> entryStream() {
        return entries.stream();
    }

    /**
     * Converts this dictionary to a regular map.
     *
     * @return The map.
     */
    public Map<String, T> toMap() {
        return entries.stream().collect(Collectors.toMap(Entry::key, Entry::value));
    }

    @Override
    public String toString() {
        final String prefix = "Dictionary of Type: " + StringUtil.typeName(values.getFirst());
        final String[] entriesArray = entries.stream()
                .map(Record::toString)
                .toArray(String[]::new);

        return StringUtil.arrowSeparated(prefix, StringUtil.commas(entriesArray));
    }

    /**
     * An entry in the dictionary. Similar to {@link Map.Entry}.
     *
     * @param <T> The value type.
     * @param key The key.
     * @param value The value.
     */
    public record Entry<T>(String key, T value) { }
}
