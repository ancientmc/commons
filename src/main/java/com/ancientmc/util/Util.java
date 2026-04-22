package com.ancientmc.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Miscellaneous utility class. Contains helper methods for safe type getting, collections, and OS parsing, among others.
 *
 * @author moist-mason
 */
public final class Util {

    /**
     * Gets the URL of a provided String path.
     * Easier way to create a URL directly from string, since the old URL constructor has been deprecated.
     */
    public static final Function<String, URL> URL = (path) -> {
        try {
            return URI.create(path).toURL();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    /**
     * Safe getter for a non-null value. Returns the value if it exists, and throws an exception if it doesn't.
     *
     * @param value The value.
     * @return The same value, if it exists. Throws an exception if not.
     * @param <T> The type.
     */
    public static <T> T get(final @NonNull T value) {
        return Optional.of(value).orElseThrow();
    }

    /**
     * Safe getter for a conditional value. Returns the value if the condition is met, and throws an exception if the condition
     * is not met.
     *
     * <p> This is similar to {@link Util#get(Object)}, but it is used for conditions *other* than whether
     * the value exists, such as if the value is or is not a given data type, or (for numbers) if it's a certain size, etc. </p>
     *
     * @param condition The boolean condition.
     * @param value The value.
     * @param exception The exception thrown.
     * @return The same value, if the condition is met. Throws an exception if not.
     * @param <T> The return type.
     * @param <X> The exception type.
     */
    public static <T, X extends Throwable> T get(final boolean condition, final @NonNull T value, final X exception) {
        return condition ? value : elseThrow(exception);
    }

    /**
     * Type-specific method that throws an exception. Useful for ternaries that throw if the correct conditions are not met.
     *
     * @param exception The exception.
     * @return Nothing.
     * @param <T> The type.
     * @param <X> The exception type.
     */
    public static <T, X extends Throwable> T elseThrow(final X exception) {
        throw new RuntimeException(exception);
    }

    /**
     * Converts the provided collection to a list.
     *
     * @param collection The collection.
     * @return The list.
     * @param <T> The list type.
     */
    public static <T> List<T> list(final Collection<T> collection) {
        return Lists.newArrayList(collection);
    }

    /**
     * Converts the provided iterator to a list.
     *
     * @param iterator The iterator.
     * @return The list.
     * @param <T> The list type.
     */
    public static <T> List<T> list(final Iterator<T> iterator) {
        return Lists.newArrayList(iterator);
    }

    /**
     * Converts the provided enumeration to a list.
     *
     * @param enumeration The enumeration.
     * @return The list.
     * @param <T> The list type.
     */
    public static <T> List<T> list(final Enumeration<T> enumeration) {
        return Collections.list(enumeration);
    }

    /**
     * Creates a filtered stream from a collection based on the given predicate.
     * This is an intermediate method for other functions, similar to regular {@link Stream} functions.
     *
     * @param collection The collection.
     * @param predicate The predicate.
     * @return The stream.
     * @param <T> The collection type.
     */
    public static <T> Stream<T> filteredStream(final Collection<T> collection, final Predicate<T> predicate) {
        return collection.stream().filter(predicate);
    }

    /**
     * Filters a list based on the given predicate.
     *
     * @param list The list.
     * @param predicate The predicate.
     * @return The filtered list.
     * @param <T> The list type.
     */
    public static <T> List<T> filteredList(final List<T> list, final Predicate<T> predicate) {
        return filteredStream(list, predicate).toList();
    }

    /**
     * Finds an object in a list based on the provided predicate.
     *
     * @param list The list.
     * @param predicate The predicate.
     * @return The expected object. Throws an exception if the object is not present.
     * @param <T> The list type.
     */
    public static <T> T findAny(final Collection<T> list, final Predicate<T> predicate) {
        return filteredStream(list, predicate).findAny().orElseThrow();
    }

    /**
     * Checks if any element in the collection matches the given predicate.
     *
     * @param collection The collection.
     * @param predicate The predicate.
     * @return {@code true} if any match in the collection is found.
     * @param <T> The collection type.
     */
    public static <T> boolean anyMatch(final Collection<T> collection, final Predicate<T> predicate) {
        return collection.stream().anyMatch(predicate);
    }

    /**
     * Checks if any element in the array matches the given predicate.
     *
     * @param array The list.
     * @param predicate The predicate.
     * @return {@code true} if any match in the array is found.
     * @param <T> The array type.
     */
    public static <T> boolean anyMatch(final T[] array, final Predicate<T> predicate) {
        return Arrays.stream(array).anyMatch(predicate);
    }

    /**
     * Checks if no elements in the collection match the given predicate.
     *
     * @param collection The collection.
     * @param predicate The predicate.
     * @return {@code true} if no matches in the collection are found.
     * @param <T> The collection type.
     */
    public static <T> boolean noneMatch(final Collection<T> collection, final Predicate<T> predicate) {
        return collection.stream().noneMatch(predicate);
    }

    /**
     * Checks if no elements in the array match the given predicate.
     *
     * @param array The array.
     * @param predicate The predicate.
     * @return {@code true} if no matches in the array are found.
     * @param <T> The array type.
     */
    public static <T> boolean noneMatch(final T[] array, final Predicate<T> predicate) {
        return Arrays.stream(array).noneMatch(predicate);
    }

    /**
     * Checks if the collection has duplicate elements. This is done by comparing the size of the original collection to
     * the size the collection with the {@link Stream#distinct()} stream function applied. A difference in size means the
     * stream filtered out any duplicate elements.
     *
     * @param collection The collection.
     * @return {@code true} if the original collection and filtered collection are different sizes.
     * @param <T> The collection type.
     */
    public static <T> boolean hasDuplicates(final Collection<T> collection) {
        return collection.stream().distinct().count() != collection.size();
    }

    /**
     * Gets a list of the keys in a map.
     *
     * @param map The map.
     * @return The list of keys.
     * @param <K> The key type.
     * @param <V> The value type.
     */
    public static <K, V> List<K> keys(final Map<K, V> map) {
        return list(map.keySet());
    }

    /**
     * Gets a list of the values in a map.
     *
     * @param map The map.
     * @return The list of values.
     * @param <K> The key type.
     * @param <V> The value type.
     */
    public static <K, V> List<V> values(final Map<K, V> map) {
        return list(map.values());
    }

    /**
     * Creates a map based on a key list and a value list.
     *
     * <p> To lessen the risk of map-parsing bugs, both lists should be instances of a {@link LinkedList} so that the intended
     * orders for both lists are preserved and their elements are matched properly. This is not always required, however.
     * They should also be equal in size; if they aren't, an {@link IllegalArgumentException} will be thrown. </p>
     *
     * @param keys The key list.
     * @param values The value list.
     * @return The map. An exception is thrown if the key and value lists are not the same size.
     * @param <K> The key type.
     * @param <V> The value type.
     */
    public static <K, V> Map<K, V> map(final List<K> keys, final List<V> values) {
        final Map<K, V>  map = new HashMap<>();

        if (keys.size() != values.size()) {
            throw new IllegalArgumentException("Key size -> " + keys.size() + "is not equal to value size -> " + values.size());
        }

        for (int i = 0; i < keys.size(); i++) {
            map.put(keys.get(i), values.get(i));
        }

        return map;
    }

    /**
     * Creates a {@link BiMap} from the provided key set and value set.
     *
     * <p> To lessen the risk of map-parsing bugs, both lists should be instances of a {@link LinkedList} so that the intended
     * orders for both lists are preserved and their elements are matched properly. This is not always required, however.
     * They should also be equal in size; if they aren't, an {@link IllegalArgumentException} will be thrown.
     * Lastly, since this is a BiMap, both the lists and the values must consist of unique elements.
     * Otherwise, another {@link IllegalArgumentException} will be thrown. </p>
     *
     * @param keys The key list.
     * @param values The value list.
     * @return The BiMap.
     * @param <K> The key type.
     * @param <V> The value type.
     */
    public static <K, V> BiMap<K, V> biMap(final List<K> keys, final List<V> values) {
        if (hasDuplicates(keys)) {
            throw new IllegalArgumentException("Duplicate keys found.");
        } else if (hasDuplicates(values)) {
            throw new IllegalArgumentException("Duplicate values found.");
        }

        return HashBiMap.create(map(keys, values));
    }

    /**
     * Inverts a map. Keys become values and vice versa. This is a {@link BiMap} because the map must consist of both unique
     * keys and unique values.
     *
     * @param original The original map.
     * @return The inverted map.
     * @param <K> The key type.
     * @param <V> The value type.
     */
    public static <K, V> BiMap<V, K> invertedMap(final BiMap<K, V> original) {
        return original.inverse();
    }

    /**
     * Gets the key in a map from the provided value. This requires a {@link BiMap} because the map must consist of both unique
     * keys and unique values.
     *
     * @param map The map.
     * @param value The value.
     * @return The key. An exception is thrown if the value is not present.
     * @param <K> The key type.
     * @param <V> The value type.
     */
    public static <K, V> K keyFromValue(final BiMap<K, V> map, final V value) {
        final K key = invertedMap(map).get(value);
        return Util.get(key);
    }

    /**
     * Creates an exception of type {@code X} with the provided message.
     *
     * @param factory The exception factory.
     * @param message The message.
     * @return The exception.
     * @param <X> The exception type.
     */
    public static <X extends Exception> X exception(final ExceptionFactory<X> factory, final String message) {
        return factory.create(message);
    }

    /**
     * Creates an exception of type {@code X} with the provided formatted message.
     *
     * @param factory The exception factory.
     * @param template The message format template.
     * @param args Arguments passed into the template for formatting.
     * @return The exception.
     * @param <X> The exception type.
     */
    public static <X extends Exception> X exception(final ExceptionFactory<X> factory, final String template, final Object... args) {
        String message = String.format(template, args);
        return exception(factory, message);
    }

    /**
     * Creates an {@link IOException} and formats the provided message template with arguments.
     *
     * @param template The message format template.
     * @param args Arguments passed into the template for formatting.
     * @return The exception.
     */
    public static IOException ioException(final String template, final Object... args) {
        return exception(IOException::new, template, args);
    }

    /**
     * Creates an {@link IllegalArgumentException} and formats the provided message template with arguments.
     *
     * @param template The message format template.
     * @param args Arguments passed into the template for formatting.
     * @return The exception.
     */
    public static IllegalArgumentException illegalArgException(final String template, final Object... args) {
        return exception(IllegalArgumentException::new, template, args);
    }

    /** @return The currently-installed OS. */
    public static Os os() {
        return Os.current();
    }

    /** Enum representation of the current OS. */
    public enum Os {
        LINUX("linux"),
        MACOS("osx"),
        WINDOWS("windows"),
        UNKNOWN("unknown");

        /** The name of the OS as represented in the Minecraft version JSON. */
        private final String name;

        Os(final String name) {
            this.name = name;
        }

        /** @return The currently-installed OS. */
        public static Os current() {
            final String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
            final List<String> commonDistros = List.of("debian", "ubuntu", "mint", "arch", "fedora", "zorin");

            // similar to NeoFormRuntime, OsType.java
            // in turn similar to Apache Commons Lang 3, SystemUtils.java
            if (osName.contains("linux") || anyMatch(commonDistros, osName::contains)) {
                return LINUX;
            } else if (osName.contains("mac") || osName.contains("osx") || osName.contains("os x")) {
                return MACOS;
            } else if (osName.contains("win")) {
                return WINDOWS;
            } else {
                return UNKNOWN;
            }
        }

        public String getName() {
            return name;
        }
    }

    /**
     * Factory for exception creation.
     *
     * @param <X> The exception type.
     */
    @FunctionalInterface
    public interface ExceptionFactory<X extends Exception> {
        X create(final String message);
    }
}