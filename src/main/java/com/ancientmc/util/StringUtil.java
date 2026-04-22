package com.ancientmc.util;

import java.util.*;

/**
 * String manipulation methods. Includes {@link Object#toString} helpers.
 *
 * @author moist-mason
 */
public final class StringUtil {

    /**
     * Capitalizes the first character of the string. Throws an {@link IllegalArgumentException} if the first character
     * of the string is not a letter.
     *
     * @param string The string.
     * @return The capitalized string.
     */
    public static String capitalize(final String string) {
        return Util.get(
                Character.isLetter(string.charAt(0)),
                string.substring(0, 1).toUpperCase(Locale.ROOT) + string.substring(1),
                Util.illegalArgException("First character of string %s (%c) is not a letter.", string, string.charAt(0))
        );
    }

    /**
     * Joins the input strings together with spaces in between.
     *
     * @param strings The strings.
     * @return The joined strings.
     */
    public static String spaced(final String... strings) {
        return String.join(" ", strings);
    }

    /**
     * Joins the input strings together with spaced commas in between.
     *
     * @param strings The strings.
     * @return The joined strings.
     */
    public static String commas(final String... strings) {
        return String.join(", ", strings);
    }

    /**
     * Surrounds the input string with square brackets [].
     *
     * @param string The string.
     * @return The bracketed string.
     */
    public static String brackets(final String string) {
        return "[" + brackets(string) + "]";
    }

    /**
     * {@link Object#toString} formatter for normal values.
     * This method shows the type of object, followed by the components normally found in a {@link Object#toString} call.
     *
     * @param value The object value.
     * @param components The components of that value as normally found in a {@link Object#toString} call.
     * @return The formatted string.
     * @param <T> The type.
     */
    public <T> String toString(final T value, final String components) {
        return toStringType(value) + " -> " + components;
    }

    /**
     * {@link Object#toString} formatter for arrays.
     * This method shows that this is an array of the given type. 
     * It then displays the values in the array formatted via {@link Arrays#toString(Object[])}.
     *
     * @param array The array.
     * @return The formatted string.
     * @param <T> The array type.
     */
    public <T> String toString(final T[] array) {
        return toStringType(array) + " -> " + Arrays.toString(array);
    }

    /**
     * {@link Object#toString} formatter for collections.
     * This method shows the subtype of collection (list, set, etc.), and the type iterated within the collection.
     * It then displays the values in the collection formatted via {@link StringUtil#collectionValues(Collection)}.
     *
     * @param collection The collection.
     * @return The formatted string.
     * @param <T> The collection type.
     */
    public static <T> String toString(final Collection<T> collection) {
        return collectionPrefix(collection) + " -> " + collectionValues(collection);
    }

    /**
     * {@link Object#toString} formatter for maps.
     * This method shows the subtype of map (hash map, tree map, etc.), and the types for both keys and values iterated within the map.
     * It then displays the entries in the map formatted via {@link StringUtil#mapEntries(Map)}.
     *
     * @param map The map.
     * @return The formatted string.
     * @param <K> The key type.
     * @param <V> The value type.
     */
    public static <K, V> String toString(final Map<K, V> map) {
        return mapPrefix(map) + " -> " + mapEntries(map);
    }

    /**
     * {@link Object#toString} formatter for enums.
     * This method shows the declaring enum class.
     * It then displays the entries of the map formatted via {@link StringUtil#enumConstants(Enum)}.
     *
     * @param enumeration The enum.
     * @return The formatted string.
     * @param <T> The enum type.
     */
    public static <T extends Enum<T>> String toString(final Enum<T> enumeration) {
        return enumPrefix(enumeration) + " -> " + enumConstants(enumeration);
    }

    /**
     * Formats the values in a collection as an array with {@link Arrays#toString(Object[])}.
     * 
     * @param collection The collection.
     * @return The formatted string.
     * @param <T> The collection type.
     */
    private static <T> String collectionValues(final Collection<T> collection) {
        return Arrays.toString(collection.toArray());
    }

    /**
     * Formats the entries in a map. Each entry is formatted within square brackets as {@code [K.toString: V.toString]}, 
     * and entries are separated by commas.
     *
     * @param map The map.
     * @return The formatted string.
     * @param <K> The key type.
     * @param <V> The value type.
     */
    private static <K, V> String mapEntries(final Map<K, V> map) {
        String[] entries = map.entrySet().stream()
                .map(e -> spaced(
                        e.getKey().toString() + ":", 
                        e.getValue().toString())
                ).map(StringUtil::brackets)
                .toArray(String[]::new);

        return commas(entries);
    }

    /**
     * Formats the constants in an enum class as an array with {@link Arrays#toString(Object[])}.
     *
     * @param enumeration The enum.
     * @return The formatted string.
     * @param <T> The enum type.
     */
    private static <T extends Enum<T>> String enumConstants(final Enum<T> enumeration) {
        return Arrays.toString(enumeration.getDeclaringClass().getEnumConstants());
    }

    /**
     * Formats the type information for a provided value.
     * 
     * @param value The value.
     * @return The formatted type information.
     * @param <T> The type.
     */
    private static <T> String toStringType(final T value) {
        String prefix = "";

        if (value instanceof Collection<?>) {
            prefix = collectionPrefix((Collection<?>) value);
        } else if (value instanceof Map<?, ?>) {
            prefix = mapPrefix((Map<?,?>) value);
        } else if (value instanceof Enum<?>) {
            prefix = enumPrefix((Enum<?>) value);
        }

        String valueType = Util.get(
                !value.getClass().isAnonymousClass(),
                value.getClass().getSimpleName(),
                Util.illegalArgException(
                        "Class %s of value %s is anonymous and has no simple name.", value.getClass().toString(), value)
        );

        return spaced(prefix, valueType);
    }

    /**
     * Formats the type information for a provided array.
     *
     * @param array The array.
     * @return The formatted type information.
     * @param <T> The array type.
     */
    private static <T> String toStringType(final T[] array) {
        return spaced("Array of type", array.getClass().getComponentType().getTypeName());
    }

    /**
     * Formats the type information of a collection. 
     * Shows what subtype of collection it is (list, set, etc.) and the type iterated within the collection.
     *
     * @param collection The collection.
     * @return The formatted type information.
     * @param <T> The collection type.
     */
    private static <T> String collectionPrefix(final Collection<T> collection) {
        return spaced(
                collection.getClass().getTypeName(),
                "of Type:",
                toStringType(collection.toArray().getClass().getComponentType().getTypeName())
        );
    }

    /**
     * Formats the type information of a map. 
     * Shows what subtype of map it is (hash map, tree map, etc.) and the key and value types.
     *
     * @param map The map.
     * @return The formatted type information.
     * @param <K> The key type.
     * @param <V> The value type.
     */
    private static <K, V, M extends Map<K, V>> String mapPrefix(final M map) {
        return spaced(
                map.getClass().getTypeName(),
                "with Key Type:", collectionPrefix(Util.keys(map)),
                "and Value Type:", collectionPrefix(Util.values(map))
        );
    }

    /**
     * Formats the type information of an enum. 
     * Shows the declaring class for this enum.
     *
     * @param enumeration The map.
     * @return The formatted type information.
     * @param <T> The enum type.
     */
    private static <T extends Enum<T>> String enumPrefix(final Enum<T> enumeration) {
        return "Enum " + enumeration.getDeclaringClass().getSimpleName();
    }
}