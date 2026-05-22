package com.github.moistmason.commons.codec;

import com.github.moistmason.commons.type.Dictionary;
import com.github.moistmason.commons.type.SerializableEnum;
import com.mojang.serialization.Codec;

import java.net.URL;
import java.nio.file.Path;

/**
 * Contains providers for building codec types.
 *
 * @author moist-mason
 */
public final class CodecProviders {

    /** Provides codec builders for integers. */
    public static final CodecProvider<Integer> INT = () -> Codec.INT;

    /** Provides codec builders for doubles. */
    public static final CodecProvider<Double> DOUBLE = () -> Codec.DOUBLE;

    /** Provides codec builders for longs. */
    public static final CodecProvider<Long> LONG = () -> Codec.LONG;

    /** Provides codec builders for floats. */
    public static final CodecProvider<Float> FLOAT = () -> Codec.FLOAT;

    /** Provides codec builders for strings. */
    public static final CodecProvider<String> STRING = () -> Codec.STRING;

    /** Provides codec builders for bytes. */
    public static final CodecProvider<Byte> BYTE = () -> Codec.BYTE;

    /** Provides codec builders for shorts. */
    public static final CodecProvider<Short> SHORT = () -> Codec.SHORT;

    /** Provides codec builders for booleans. */
    public static final CodecProvider<Boolean> BOOL = () -> Codec.BOOL;

    /** Provides codec builders for URLs. */
    public static final CodecProvider<URL> URL = () -> Codecs.URL;

    /** Provides codec builders for paths. */
    public static final CodecProvider<Path> PATH = () -> Codecs.PATH;

    /**
     * Provides codec builders for a {@link Dictionary}.
     *
     * @param valueCodec The codec of the dictionary's value type.
     * @return The provider.
     * @param <T> The dictionary type.
     */
    public static <T> CodecProvider<Dictionary<T>> dictionary(final Codec<T> valueCodec) {
        return () -> Codecs.dictionary(valueCodec);
    }

    /**
     * Provides codec builders for an enum class implemented with {@link SerializableEnum}.
     *
     * @param values The enum constants.
     * @return The provider.
     * @param <E> The enum class type.
     */
    public static <E extends Enum<E> & SerializableEnum> CodecProvider<E> enumCodec(final E[] values) {
        return () -> SerializableEnum.codec(values);
    }
}
