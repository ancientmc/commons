package com.ancientmc.commons.codec;

import com.ancientmc.commons.Util;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function5;
import com.mojang.datafixers.util.Function6;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Helper methods and fields for codecs. A {@link Codec} is an object defined in Mojang's DataFixerUpper library that
 * helps serialize data between Java and JSON/NBT formats.
 *
 * @author moist-mason
 */
public final class Codecs {

    /** A codec for a URL. */
    public static final Codec<URL> URL = Codec.STRING.comapFlatMap(Codecs::checkUrl, java.net.URL::toString);

    /** A codec for a path. */
    public static final Codec<Path> PATH = Codec.STRING.comapFlatMap(Codecs::checkPath, Path::toString);

    /**
     * Creates a codec for an object with one parameter in its constructor.
     *
     * @param function A method reference to an object's constructor (Object::new).
     * @param field1 The field codec entry for the first parameter.
     * @return The codec.
     * @param <F1> The first field type.
     * @param <R> The object type.
     */
    public static <F1, R> Codec<R> of(final Function<F1, R> function, final RecordCodecBuilder<R, F1> field1) {
        return RecordCodecBuilder.create(i -> i.group(
                field1
        ).apply(i, function));
    }

    /**
     * Creates a codec for an object with two parameters in its constructor.
     *
     * @param function A method reference to an object's constructor (Object::new).
     * @param field1 The field codec entry for the first parameter.
     * @param field2 The field codec entry for the second parameter.
     * @return The codec.
     * @param <F1> The first parameter type.
     * @param <F2> The second parameter type.
     * @param <R> The object type.
     */
    public static <F1, F2, R> Codec<R> of(
            final BiFunction<F1, F2, R> function,
            final RecordCodecBuilder<R, F1> field1,
            final RecordCodecBuilder<R, F2> field2
    ) {
        return RecordCodecBuilder.create(i -> i.group(
                field1,
                field2
        ).apply(i, function));
    }

    /**
     * Creates a codec for an object with three parameters in its constructor.
     *
     * @param function A method reference to an object's constructor (Object::new).
     * @param field1 The field codec entry for the first parameter.
     * @param field2 The field codec entry for the second parameter.
     * @param field3 The field codec entry for the third parameter.
     * @return The codec.
     * @param <F1> The first parameter type.
     * @param <F2> The second parameter type.
     * @param <F3> The third parameter type.
     * @param <R> The object type.
     */
    public static <F1, F2, F3, R> Codec<R> of(
            final Function3<F1, F2, F3, R> function,
            final RecordCodecBuilder<R, F1> field1,
            final RecordCodecBuilder<R, F2> field2,
            final RecordCodecBuilder<R, F3> field3
    ) {
        return RecordCodecBuilder.create(i -> i.group(
                field1,
                field2,
                field3
        ).apply(i, function));
    }

    /**
     * Creates a codec for an object with four parameters in its constructor.
     *
     * @param function A method reference to an object's constructor (Object::new).
     * @param field1 The field codec entry for the first parameter.
     * @param field2 The field codec entry for the second parameter.
     * @param field3 The field codec entry for the third parameter.
     * @param field4 The field codec entry for the fourth parameter.
     * @return The codec.
     * @param <F1> The first parameter type.
     * @param <F2> The second parameter type.
     * @param <F3> The third parameter type.
     * @param <F4> The fourth parameter type.
     * @param <R> The object type.
     */
    public static <F1, F2, F3, F4, R> Codec<R> of(
            final Function4<F1, F2, F3, F4, R> function,
            final RecordCodecBuilder<R, F1> field1,
            final RecordCodecBuilder<R, F2> field2,
            final RecordCodecBuilder<R, F3> field3,
            final RecordCodecBuilder<R, F4> field4
    ) {
        return RecordCodecBuilder.create(i -> i.group(
                field1,
                field2,
                field3,
                field4
        ).apply(i, function));
    }

    /**
     * Creates a codec for an object with five parameters in its constructor.
     *
     * @param function A method reference to an object's constructor (Object::new).
     * @param field1 The field codec entry for the first parameter.
     * @param field2 The field codec entry for the second parameter.
     * @param field3 The field codec entry for the third parameter.
     * @param field4 The field codec entry for the fourth parameter.
     * @param field5 The field codec entry for the fifth parameter.
     * @return The codec.
     * @param <F1> The first parameter type.
     * @param <F2> The second parameter type.
     * @param <F3> The third parameter type.
     * @param <F4> The fourth parameter type.
     * @param <F5> The fifth parameter type.
     * @param <R> The object type.
     */
    public static <F1, F2, F3, F4, F5, R> Codec<R> of(
            final Function5<F1, F2, F3, F4, F5, R> function,
            final RecordCodecBuilder<R, F1> field1,
            final RecordCodecBuilder<R, F2> field2,
            final RecordCodecBuilder<R, F3> field3,
            final RecordCodecBuilder<R, F4> field4,
            final RecordCodecBuilder<R, F5> field5
    ) {
        return RecordCodecBuilder.create(i -> i.group(
                field1,
                field2,
                field3,
                field4,
                field5
        ).apply(i, function));
    }

    /**
     * Creates a codec for an object with six parameters in its constructor.
     *
     * @param function A method reference to an object's constructor (Object::new).
     * @param field1 The field codec entry for the first parameter.
     * @param field2 The field codec entry for the second parameter.
     * @param field3 The field codec entry for the third parameter.
     * @param field4 The field codec entry for the fourth parameter.
     * @param field5 The field codec entry for the fifth parameter.
     * @param field6 The field codec entry for the sixth parameter.
     * @return The codec.
     * @param <F1> The first parameter type.
     * @param <F2> The second parameter type.
     * @param <F3> The third parameter type.
     * @param <F4> The fourth parameter type.
     * @param <F5> The fifth parameter type.
     * @param <F6> The sixth parameter type.
     * @param <R> The object type.
     */
    public static <F1, F2, F3, F4, F5, F6, R> Codec<R> of(
            final Function6<F1, F2, F3, F4, F5, F6, R> function,
            final RecordCodecBuilder<R, F1> field1,
            final RecordCodecBuilder<R, F2> field2,
            final RecordCodecBuilder<R, F3> field3,
            final RecordCodecBuilder<R, F4> field4,
            final RecordCodecBuilder<R, F5> field5,
            final RecordCodecBuilder<R, F6> field6
    ) {
        return RecordCodecBuilder.create(i -> i.group(
                field1,
                field2,
                field3,
                field4,
                field5,
                field6
        ).apply(i, function));
    }

    /**
     * Checks if the input string is a legitimate URL address.
     * @param address The address.
     * @return A successful {@link DataResult} if the address is legitimate, and a {@link DataResult} error if it isn't.
     */
    private static DataResult<URL> checkUrl(final String address) {
        try {
            return DataResult.success(Util.URL.apply(address));
        } catch (final NullPointerException e) {
            return DataResult.error(() -> "Invalid URL address: " + address);
        }
    }

    /**
     * Checks if the input string is a legitimate path address.
     * @param address The address.
     * @return A successful {@link DataResult} if the address is legitimate, and a {@link DataResult} error if it isn't.
     */
    private static DataResult<Path> checkPath(final String address) {
        try {
            return DataResult.success(Path.of(address));
        } catch (final InvalidPathException e) {
            return DataResult.error(() -> "Invalid Path address: " + address);
        }
    }
}
