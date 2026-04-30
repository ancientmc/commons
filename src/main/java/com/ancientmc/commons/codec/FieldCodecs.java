package com.ancientmc.commons.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Contains helper methods for building fields in codecs.
 *
 * @author moist-mason
 */
public class FieldCodecs {

    /**
     * Creates a field component as part of a {@link RecordCodecBuilder}.
     *
     * @param getter The method reference to a getter associated with this field.
     * @param name The name of the field in the data output.
     * @param codec The codec associated with this field.
     * @return The {@link RecordCodecBuilder}.
     * @param <O> The type of this field's parent object.
     * @param <A> The type of this field.
     */
    public static <O, A> RecordCodecBuilder<O, A> field(final Function<O, A> getter, final String name, final Codec<A> codec) {
        return codec.fieldOf(name).forGetter(getter);
    }

    /**
     * Creates an optional field component as part of a {@link RecordCodecBuilder}.
     *
     * @param getter The method reference to a getter associated with this field.
     * @param name The name of the field in the data output.
     * @param codec The codec associated with this field.
     * @return The {@link RecordCodecBuilder}.
     * @param <O> The type of this field's parent object.
     * @param <A> The type of this field.
     */
    public static <O, A> RecordCodecBuilder<O, Optional<A>> optionalField(final Function<O, Optional<A>> getter, final String name, final Codec<A> codec) {
        return codec.optionalFieldOf(name).forGetter(getter);
    }

    /**
     * Creates a field list component as part of a {@link RecordCodecBuilder}.
     *
     * @param getter The method reference to a getter associated with this field.
     * @param name The name of the field in the data output.
     * @param codec The codec associated with this field.
     * @return The {@link RecordCodecBuilder}.
     * @param <O> The type of this field's parent object.
     * @param <A> The type of this field.
     */
    public static <O, A> RecordCodecBuilder<O, List<A>> listField(final Function<O, List<A>> getter, final String name, final Codec<A> codec) {
        return codec.listOf().fieldOf(name).forGetter(getter);
    }

    /**
     * Creates an optional field list component as part of a {@link RecordCodecBuilder}.
     *
     * @param getter The method reference to a getter associated with this field.
     * @param name The name of the field in the data output.
     * @param codec The codec associated with this field.
     * @return The {@link RecordCodecBuilder}.
     * @param <O> The type of this field's parent object.
     * @param <A> The type of this field.
     */
    public static <O, A> RecordCodecBuilder<O, Optional<List<A>>> optionalListField(final Function<O, Optional<List<A>>> getter, final String name, final Codec<A> codec) {
        return codec.listOf().optionalFieldOf(name).forGetter(getter);
    }

    /**
     * Creates a map field component as part of a {@link RecordCodecBuilder}.
     *
     * @param getter The method reference to a getter associated with this field.
     * @param name The name of the field in the data output.
     * @param keyCodec The codec associated with this field's key.
     * @param valueCodec The codec associated with this field's value.
     * @return The {@link RecordCodecBuilder}.
     * @param <O> The type of this field's parent object.
     * @param <K> The type of this field's key.
     * @param <V> The type of this field's value.
     */
    public static <O, K, V> RecordCodecBuilder<O, Map<K, V>> mapField(final Function<O, Map<K, V>> getter, final String name, final Codec<K> keyCodec, final Codec<V> valueCodec) {
        return Codec.unboundedMap(keyCodec, valueCodec).fieldOf(name).forGetter(getter);
    }

    /**
     * Creates a string field component as part of a {@link RecordCodecBuilder}.
     *
     * @param getter The method reference to a getter associated with this field.
     * @param name The name of the field in the data output.
     * @return The {@link RecordCodecBuilder}.
     * @param <O> The type of this field's parent object.
     */
    public static <O> RecordCodecBuilder<O, String> stringField(final Function<O, String> getter, final String name) {
        return field(getter, name, Codec.STRING);
    }

    /**
     * Creates an optional string field component as part of a {@link RecordCodecBuilder}.
     *
     * @param getter The method reference to a getter associated with this field.
     * @param name The name of the field in the data output.
     * @return The {@link RecordCodecBuilder}.
     * @param <O> The type of this field's parent object.
     */
    public static <O> RecordCodecBuilder<O, Optional<String>> optionalStringField(final Function<O, Optional<String>> getter, final String name) {
        return optionalField(getter, name, Codec.STRING);
    }

    /**
     * Creates a string list field component as part of a {@link RecordCodecBuilder}.
     *
     * @param getter The method reference to a getter associated with this field.
     * @param name The name of the field in the data output.
     * @return The {@link RecordCodecBuilder}.
     * @param <O> The type of this field's parent object.
     */
    public static <O> RecordCodecBuilder<O, List<String>> stringListField(final Function<O, List<String>> getter, final String name) {
        return listField(getter, name, Codec.STRING);
    }

    /**
     * Creates an optional string list field component as part of a {@link RecordCodecBuilder}.
     *
     * @param getter The method reference to a getter associated with this field.
     * @param name The name of the field in the data output.
     * @return The {@link RecordCodecBuilder}.
     * @param <O> The type of this field's parent object.
     */
    public static <O> RecordCodecBuilder<O, Optional<List<String>>> optionalStringListField(final Function<O, Optional<List<String>>> getter, final String name) {
        return optionalListField(getter, name, Codec.STRING);
    }

    /**
     * Creates a URL field component as part of a {@link RecordCodecBuilder}.
     *
     * @param getter The method reference to a getter associated with this field.
     * @param name The name of the field in the data output.
     * @return The {@link RecordCodecBuilder}.
     * @param <O> The type of this field's parent object.
     */
    public static <O> RecordCodecBuilder<O, URL> urlField(final Function<O, URL> getter, final String name) {
        return Codecs.URL.fieldOf(name).forGetter(getter);
    }

    /**
     * Creates an integer field component as part of a {@link RecordCodecBuilder}.
     *
     * @param getter The method reference to a getter associated with this field.
     * @param name The name of the field in the data output.
     * @return The {@link RecordCodecBuilder}.
     * @param <O> The type of this field's parent object.
     */
    public static <O> RecordCodecBuilder<O, Integer> intField(final Function<O, Integer> getter, final String name) {
        return field(getter, name, Codec.INT);
    }

    /**
     * Creates an optional integer field component as part of a {@link RecordCodecBuilder}.
     *
     * @param getter The method reference to a getter associated with this field.
     * @param name The name of the field in the data output.
     * @return The {@link RecordCodecBuilder}.
     * @param <O> The type of this field's parent object.
     */
    public static <O> RecordCodecBuilder<O, Optional<Integer>> optionalIntField(final Function<O, Optional<Integer>> getter, final String name) {
        return optionalField(getter, name, Codec.INT);
    }
}
