package com.github.moistmason.commons.math;

import com.github.moistmason.commons.Util;
import com.github.moistmason.commons.codec.CodecBuilders;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.NoSuchElementException;
import java.util.function.DoublePredicate;
import java.util.stream.DoubleStream;

import static com.github.moistmason.commons.codec.CodecProviders.*;

/**
 * A double range with defined, inclusive bounds and a specified increment value.
 *
 * @author moist-mason
 *
 * @param min The minimum value. Inclusive in a stream.
 * @param max The maximum value. Inclusive in a stream.
 * @param increment How much each value in the range increases by in a {@link DoubleStream}.
 */
public record DoubleRange(double min, double max, double increment) {

    public static final Codec<DoubleRange> CODEC = CodecBuilders.of(DoubleRange::new,
            DOUBLE.field("min", DoubleRange::min),
            DOUBLE.field("max", DoubleRange::max),
            DOUBLE.field("increment", DoubleRange::increment)
    ).validate(
            range -> range.min < range.max
                    ? DataResult.error(() -> "Minimum must be less than maximum. Range + " + range)
                    : DataResult.success(range)
    );

    /**
     * Creates a double range whose values are separated by one.
     *
     * @param min The minimum value. Inclusive in a stream.
     * @param max The maximum value. Inclusive in a stream.
     * @return The range.
     */
    public static DoubleRange of(final double min, final double max) {
        return new DoubleRange(min, max, 1);
    }

    /**
     * Creates a double range whose values are separated by the given increment.
     *
     * @param min The minimum value. Inclusive in a stream.
     * @param max The maximum value. Inclusive in a stream.
     * @param increment How much each value in the range increases by in a {@link DoubleStream}.
     * @return The range.
     */
    public static DoubleRange of(final double min, final double max, final double increment) {
        return new DoubleRange(min, max, increment);
    }

    /**
     * Creates an {@link DoubleStream} of this range that increments by the provided value.
     *
     * <p> Example: A range of {@code [0, 10]} with an increment of {@code 0.5}
     * Will create a stream consisting of the values {@code [0, 0.5, 1.0, 1.5, 2.0...10]}.</p>
     *
     * @return The stream.
     */
    public DoubleStream stream() {
        return DoubleStream.iterate(min, n -> n <= max, n -> n + increment);
    }

    /**
     * Gets a value from this range's stream that matches the given {@link DoublePredicate}.
     *
     * <p> WARNING: This method calls {@code findFirst()} when searching in the stream and doesn't offer too much precision
     * in getting particular a particular value. This method's use cases are really narrow as a result. </p>
     *
     * <p> Most of the time it'll be easier to call {@link DoubleRange#getAll(DoublePredicate)} and sort through that method's
     * returned array to get the value you want. </p>
     *
     * @param predicate The predicate.
     * @return The value. A {@link NoSuchElementException} is thrown if no value in the stream matches the given predicate.
     */
    public double get(final DoublePredicate predicate) {
        return stream().filter(predicate).findFirst().orElseThrow(() ->
                Util.noSuchElementException("No values in the range %s match the given predicate %s", this, predicate));
    }

    /**
     * Gets all the values that match the given {@link DoublePredicate}.
     *
     * @param predicate The predicate.
     * @return The values. A {@link NoSuchElementException} is thrown if no value in the stream matches the given predicate.
     */
    public double[] getAll(final DoublePredicate predicate) {
        final double[] array = stream().filter(predicate).toArray();
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
    public double getRandom() {
        return stream().findAny().orElseThrow();
    }
}
