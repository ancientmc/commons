package com.github.moistmason.commons;

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
     * @param input The string.
     * @return The capitalized string.
     */
    public static String capitalize(final String input) {
        return Util.get(
                Character.isLetter(input.charAt(0)),
                input.substring(0, 1).toUpperCase(Locale.ROOT) + input.substring(1),
                Util.illegalArgException("First character of string %s (%c) is not a letter.", input, input.charAt(0))
        );
    }

    /**
     * Joins the input strings together with spaces in between.
     *
     * <p> Example: An input string array of {@code ["first", "second", "third"]}
     * will return {@code "first second third"}. </p>
     *
     * @param inputs The strings.
     * @return The joined strings.
     */
    public static String spaced(final String... inputs) {
        return String.join(" ", inputs);
    }

    /**
     * Converts the input values to a string array and joins them together with spaces in between.
     *
     * <p> Example: An input integer array of {@code [1, 2, 3]}
     * will return {@code "1 2 3"}. </p>
     *
     * @param inputs The values.
     * @return The joined string.
     */
    public static String spaced(final Object... inputs) {
        final String[] array = stringArray(inputs);
        return spaced(array);
    }

    /**
     * Joins the input strings together with spaced commas in between.
     *
     * <p> Example: An input string array of {@code ["first", "second", "third"]}
     * will return {@code "first, second, third"}. </p>
     *
     * @param inputs The strings.
     * @return The joined string.
     */
    public static String commas(final String... inputs) {
        return String.join(", ", inputs);
    }

    /**
     * Converts the input values to a string array and joins them together with spaced commas in between.
     *
     * <p> Example: An input integer array of {@code [1, 2, 3]}
     * will return {@code "1, 2, 3"}. </p>
     *
     * @param inputs The values.
     * @return The joined string.
     */
    public static String commas(final Object... inputs) {
        final String[] array = stringArray(inputs);
        return commas(array);
    }

    /**
     * Joins the input strings together with new line breaks in between.
     *
     * <p> Example: An input array of {@code ["first", "second", "third"]}
     * will return {@code "first\nsecond\nthird"}. </p>
     *
     * @param inputs The strings.
     * @return The joined string.
     */
    public static String newLines(final String... inputs) {
        return String.join("\n", inputs);
    }

    /**
     * Converts the input values to a string array and joins them together with new line breaks in between.
     *
     * <p> Example: An input array of {@code [1, 2, 3]}
     * will return {@code "first\nsecond\nthird"}. </p>
     *
     * @param inputs The values.
     * @return The joined string.
     */
    public static String newLines(final Object... inputs) {
        final String[] array = stringArray(inputs);
        return newLines(array);
    }

    /**
     * Joins the two input strings together with an arrow symbol {@code ->} separating them.
     *
     * <p> Example: inputting the values {@code "foo", "bar"} will return the joined string {@code "foo -> bar"}. </p>
     *
     * @param first The first input string.
     * @param second The second input string.
     * @return The joined strings.
     */
    public static String arrowSeparated(final String first, final String second) {
        return spaced(first, "->", second);
    }

    /**
     * Converts the two input values into  with an arrow symbol {@code ->} separating them.
     *
     * <p> Example: inputting the values {@code 3, 4} will return the joined string {@code "3 -> 4"}. </p>
     *
     * @param first The first input string.
     * @param second The second input string.
     * @return The joined strings.
     */
    public static String arrowSeparated(final Object first, final Object second) {
        return spaced(first, "->", second);
    }

    /**
     * Surrounds the input value's string representation with square brackets [].
     *
     * <p> Example: inputting the string {@code "foo"} will return the string {@code "[foo]"}. </p>
     *
     * @param input The input value.
     * @return The bracketed string.
     */
    public static String brackets(final Object input) {
        return "[" + input + "]";
    }

    /**
     * Converts the input values into a string array. Useful as an intermediate step for string concatenation.
     *
     * @param inputs The input values.
     * @return The string array.
     */
    public static String[] stringArray(final Object... inputs) {
        return Arrays.stream(inputs)
                .map(String::valueOf)
                .toArray(String[]::new);
    }

    /**
     * Checks if the input string partially contains any element of the array.
     *
     * @param array The array.
     * @param input The input string.
     * @return {@code true} if a partial match is found in the array.
     */
    public static boolean containsAny(final String[] array, final String input) {
        return Util.anyMatch(array, input::contains);
    }

    /**
     * Checks if the input string partially contains any element of the list.
     *
     * @param list The list.
     * @param input The input string.
     * @return {@code true} if a partial match is found in the list.
     */
    public static boolean containsAny(final List<String> list, final String input) {
        return Util.anyMatch(list, input::contains);
    }

    /**
     * Checks if the input string is equal to any element of the array.
     *
     * @param array The array.
     * @param input The input string.
     * @return {@code true} if an exact match is found in the array.
     */
    public static boolean equalsAny(final String[] array, final String input) {
        return Util.anyMatch(array, input::equals);
    }

    /**
     * Checks if the input string is equal to any element of the list.
     *
     * @param list The list.
     * @param input The input string.
     * @return {@code true} if an exact match is found in the list.
     */
    public static boolean equalsAny(final List<String> list, final String input) {
        return Util.anyMatch(list, input::equals);
    }

    /**
     * {@link Object#toString} formatter for arrays.
     * This method shows that this is an array of the given type. 
     * It then displays the values in the array formatted via {@link Arrays#toString(Object[])}.
     *
     * @param <T> The array type.
     * @param array The array.
     * @return The formatted string.
     */
    public static <T> String toString(final T[] array) {
        final String prefix = spaced("Array of Type:", typeName(array.getClass().getComponentType()));
        return arrowSeparated(prefix, Arrays.toString(array));
    }

    /**
     * {@link Object#toString} formatter for collections.
     * This method shows the subtype of collection (list, set, etc.), and the type iterated within the collection.
     * It then displays the values in the collection formatted via {@link Arrays#toString(Object[])}.
     *
     * @param <T> The collection type.
     * @param collection The collection.
     * @return The formatted string.
     */
    public static <T> String toString(final Collection<T> collection) {
        final String prefix = spaced(simpleName(collection), "of Type:", typeName(collection));
        return arrowSeparated(prefix, Arrays.toString(collection.toArray()));
    }

    /**
     * {@link Object#toString} formatter for maps.
     * This method shows the subtype of map (hash map, tree map, etc.), and the types for both keys and values iterated within the map.
     * It then displays the entries in the map formatted via {@link StringUtil#mapEntries(Map)}.
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param map The map.
     * @return The formatted string.
     */
    public static <K, V> String toString(final Map<K, V> map) {
        final String prefix = spaced(
                simpleName(map),
                "with Key Type:", typeName(Util.list(map.keySet()).getFirst()),
                "and Value Type:", typeName(Util.list(map.values()).getFirst())
        );
        return arrowSeparated(prefix, mapEntries(map));
    }

    /**
     * {@link Object#toString} formatter for enum classes.
     * This method shows the declaring enum class.
     * It then displays the constants formatted via {@link Class#getEnumConstants()}.
     *
     * @param <T> The enum type.
     * @param enumClass The enum class.
     * @return The formatted string.
     */
    public static <T extends Enum<T>> String toString(final Class<T> enumClass) {
        return arrowSeparated("Enum " + typeName(enumClass), Arrays.toString(enumClass.getEnumConstants()));
    }

    /**
     * Formats the entries in a map. Each entry is formatted within square brackets as {@code [K.toString: V.toString]}, 
     * and entries are separated by commas.
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param map The map.
     * @return The formatted string.
     */
    private static <K, V> String mapEntries(final Map<K, V> map) {
        String[] entries = map.entrySet().stream()
                .map(e -> spaced(
                        e.getKey().toString() + ":", 
                        e.getValue().toString()))
                .map(StringUtil::brackets)
                .toArray(String[]::new);
        return commas(entries);
    }

    /**
     * Gets the simple (package-exclusive) class name of this value's type. Used for collections, maps, and other hierarchies.
     *
     * @param <T> The type.
     * @param value The value.
     * @return The simple class name.
     */
    public static <T> String simpleName(final T value) {
        return value.getClass().getSimpleName();
    }

    /**
     * Gets the type name of this value.
     *
     * @param <T> The type.
     * @param value The value.
     * @return The type name.
     */
    public static <T> String typeName(final T value) {
        return value.getClass().getTypeName();
    }

    /**
     * Gets the type name of this collection, by checking the type of the first value.
     *
     * @param <T> The type.
     * @param collection The collection.
     * @return The type name.
     */
    public static <T> String typeName(final Collection<T> collection) {
        return collection.stream().findFirst().map(StringUtil::typeName).orElseThrow();
    }

    /**
     * Gets the name of this class. Used for types that call another class during type retrieval, like enums and arrays.
     *
     * @param <T> The type.
     * @param value The value.
     * @return The class name.
     */
    public static <T> String typeName(final Class<T> value) {
        return value.getTypeName();
    }
}