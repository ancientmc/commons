package com.github.moistmason.commons.codec;

import com.github.moistmason.commons.Util;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Contains codec types not found in the base DataFixerUpper library, as well as builders for
 * {@link RecordCodecBuilder} fields.
 *
 * @author moist-mason
 */
public final class Codecs {

    /** A codec for a URL. */
    public static final Codec<URL> URL = Codec.STRING.comapFlatMap(Codecs::checkUrl, java.net.URL::toString);

    /** A codec for a path. */
    public static final Codec<Path> PATH = Codec.STRING.comapFlatMap(Codecs::checkPath, Path::toString);

    /**
     * Checks if the input string is a legitimate URL address.
     *
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
     *
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

    /**
     * Creates a field component as part of a {@link RecordCodecBuilder}.
     *
     * @param <O> The type of this field's parent object.
     * @param <A> The type of this field.
     * @param codec The codec associated with this field.
     * @param name The name of the field in the data output.
     * @param getter The method reference to a getter associated with this field.
     * @return The {@link RecordCodecBuilder}.
     */
    public static <O, A> RecordCodecBuilder<O, A> field(final Codec<A> codec, final String name, final Function<O, A> getter) {
        return codec.fieldOf(name).forGetter(getter);
    }

    /**
     * Creates a field component as part of a {@link RecordCodecBuilder} with a default value.
     *
     * @param <O> The type of this field's parent object.
     * @param <A> The type of this field.
     * @param codec The codec associated with this field.
     * @param name The name of the field in the data output.
     * @param defaultValue The default value for this field if no other value is explicitly defined.
     * @param getter The method reference to a getter associated with this field.
     * @return The {@link RecordCodecBuilder}.
     */
    public static <O, A> RecordCodecBuilder<O, A> defaultableField(
            final Codec<A> codec,
            final String name,
            final A defaultValue,
            final Function<O, A> getter
    ) {
        return codec.optionalFieldOf(name, defaultValue).forGetter(getter);
    }

    /**
     * Creates an optional field component as part of a {@link RecordCodecBuilder}.
     *
     * @param <O> The type of this field's parent object.
     * @param <A> The type of this field.
     * @param codec The codec associated with this field.
     * @param name The name of the field in the data output.
     * @param getter The method reference to a getter associated with this field.
     * @return The {@link RecordCodecBuilder}.
     */
    public static <O, A> RecordCodecBuilder<O, Optional<A>> optionalField(final Codec<A> codec, final String name, final Function<O, Optional<A>> getter) {
        return codec.optionalFieldOf(name).forGetter(getter);
    }

    /**
     * Creates a field list component as part of a {@link RecordCodecBuilder}.
     *
     * @param <O> The type of this field's parent object.
     * @param <A> The type of this field.
     * @param codec The codec associated with this field.
     * @param name The name of the field in the data output.
     * @param getter The method reference to a getter associated with this field.
     * @return The {@link RecordCodecBuilder}.
     */
    public static <O, A> RecordCodecBuilder<O, List<A>> listField(final Codec<A> codec, final String name, final Function<O, List<A>> getter) {
        return codec.listOf().fieldOf(name).forGetter(getter);
    }

    /**
     * Creates a field list component as part of a {@link RecordCodecBuilder} with a default value.
     *
     * @param <O> The type of this field's parent object.
     * @param <A> The type of this field.
     * @param codec The codec associated with this field.
     * @param name The name of the field in the data output.
     * @param defaultValue The default value for this field if no other value is explicitly defined.
     * @param getter The method reference to a getter associated with this field.
     * @return The {@link RecordCodecBuilder}.
     */
    public static <O, A> RecordCodecBuilder<O, List<A>> defaultableListField(
            final Codec<A> codec,
            final String name,
            final List<A> defaultValue,
            final Function<O, List<A>> getter
    ) {
        return codec.listOf().optionalFieldOf(name, defaultValue).forGetter(getter);
    }

    /**
     * Creates an optional field list component as part of a {@link RecordCodecBuilder}.
     *
     * @param <O> The type of this field's parent object.
     * @param <A> The type of this field.
     * @param codec The codec associated with this field.
     * @param name The name of the field in the data output.
     * @param getter The method reference to a getter associated with this field.
     * @return The {@link RecordCodecBuilder}.
     */
    public static <O, A> RecordCodecBuilder<O, Optional<List<A>>> optionalListField(final Codec<A> codec, final String name, final Function<O, Optional<List<A>>> getter) {
        return codec.listOf().optionalFieldOf(name).forGetter(getter);
    }

    /**
     * Creates a map field component as part of a {@link RecordCodecBuilder}.
     * 
     * @param <O> The type of this field's parent object.
     * @param <K> The type of this field's key.
     * @param <V> The type of this field's value.
     * @param keyCodec The codec associated with this field's key.
     * @param valueCodec The codec associated with this field's value.
     * @param name The name of the field in the data output.
     * @param getter The method reference to a getter associated with this field.
     * @return The {@link RecordCodecBuilder}.
     */
    public static <O, K, V> RecordCodecBuilder<O, Map<K, V>> mapField(
            final Codec<K> keyCodec,
            final Codec<V> valueCodec,
            final String name,
            final Function<O, Map<K, V>> getter
    ) {
        return Codec.unboundedMap(keyCodec, valueCodec).fieldOf(name).forGetter(getter);
    }

    /**
     * Creates an optional map field component as part of a {@link RecordCodecBuilder}.
     *
     * @param <O> The type of this field's parent object.
     * @param <K> The type of this field's key.
     * @param <V> The type of this field's value.
     * @param keyCodec The codec associated with this field's key.
     * @param valueCodec The codec associated with this field's value.
     * @param name The name of the field in the data output.
     * @param getter The method reference to a getter associated with this field.
     * @return The {@link RecordCodecBuilder}.
     */
    public static <O, K, V> RecordCodecBuilder<O, Optional<Map<K, V>>> optionalMapField(
            final Codec<K> keyCodec,
            final Codec<V> valueCodec,
            final String name,
            final Function<O, Optional<Map<K, V>>> getter
    ) {
        return Codec.unboundedMap(keyCodec, valueCodec).optionalFieldOf(name).forGetter(getter);
    }
}
