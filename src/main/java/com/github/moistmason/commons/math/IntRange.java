package com.github.moistmason.commons.math;

import com.github.moistmason.commons.Util;
import com.github.moistmason.commons.codec.CodecBuilders;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.NoSuchElementException;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import static com.github.moistmason.commons.codec.CodecProviders.*;

/**
 * An integer range with defined, inclusive bounds and a specified increment value.
 *
 * @author moist-mason
 *
 * @param min The minimum value. Inclusive in a stream.
 * @param max The maximum value. Inclusive in a stream.
 * @param increment How much each value in the range increases by in an {@link IntStream}.
 */
public record IntRange(int min, int max, int increment) {

    public static final Codec<IntRange> CODEC = CodecBuilders.of(IntRange::new,
            INT.field("min", IntRange::min),
            INT.field("max", IntRange::max),
            INT.field("increment", IntRange::increment)
    ).validate(
            range -> range.min < range.max
                    ? DataResult.error(() -> "Minimum must be less than maximum. Range + " + range)
                    : DataResult.success(range)
    );

    /**
     * Creates an integer range whose values are separated by one.
     *
     * @param min The minimum value. Inclusive in a stream.
     * @param max The maximum value. Inclusive in a stream.
     * @return The range.
     */
    public static IntRange of(final int min, final int max) {
        return new IntRange(min, max, 1);
    }

    /**
     * Creates an integer range whose values are separated by the given increment.
     *
     * @param min The minimum value. Inclusive in a stream.
     * @param max The maximum value. Inclusive in a stream.
     * @param increment How much each value in the range increases by in an {@link IntStream}.
     * @return The range.
     */
    public static IntRange of(final int min, final int max, final int increment) {
        return new IntRange(min, max, increment);
    }

    /**
     * Creates an {@link IntStream} of this range that increments by the provided value.
     *
     * <p> Example: A range of {@code [0, 50]} with an increment of {@code 2}
     * Will create a stream consisting of the values {@code [0, 2, 4, 6, 8...50]}.</p>
     *
     * @return The stream.
     */
    public IntStream stream() {
        return IntStream.iterate(min, n -> n <= max, n -> n + increment);
    }

    /**
     * Gets a value from this range's stream that matches the given {@link IntPredicate}.
     *
     * <p> WARNING: This method calls {@code findFirst()} when searching in the stream and doesn't offer too much precision
     * in getting particular a particular value. This method's use cases are really narrow as a result. </p>
     *
     * <p> Most of the time it'll be easier to call {@link IntRange#getAll(IntPredicate)} and sort through that method's
     * returned array to get the value you want. </p>
     *
     * @param predicate The predicate.
     * @return The value. A {@link NoSuchElementException} is thrown if no value in the stream matches the given predicate.
     */
    public int get(final IntPredicate predicate) {
        return stream().filter(predicate).findFirst().orElseThrow(() ->
                Util.noSuchElementException("No values in the range %s match the given predicate %s", this, predicate));
    }

    /**
     * Gets all the values that match the given {@link IntPredicate}.
     *
     * @param predicate The predicate.
     * @return The values. A {@link NoSuchElementException} is thrown if no value in the stream matches the given predicate.
     */
    public int[] getAll(final IntPredicate predicate) {
        final int[] array = stream().filter(predicate).toArray();
        return Util.get(
                array.length > 0,
                array, Util.noSuchElementException("No values in the range %s match the given predicate %s", this, predicate)
        );
    }

    /**
     * Gets a random value within the range. Done by calling {@code findAny()} in the stream.
     *
     * @return The value.
     */
    public int getRandom() {
        return stream().findAny().orElseThrow();
    }
}
