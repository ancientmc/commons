package com.github.moistmason.commons.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Provides helper methods for creating codecs of a given type.
 *
 * @author moist-mason
 * @param <A> The codec type.
 */
public interface CodecProvider<A> {

    /**
     * The actual codec for this type. See {@link CodecProviders} for example implementations.
     *
     * @return The codec.
     */
    Codec<A> codec();

    /**
     * Provides a field component builder for this codec type. Used in building record-like codecs.
     *
     * @param name The name of the field.
     * @param getter The method reference to a getter associated with this field.
     * @return The field component.
     * @param <O> The type of the parent object of this codec.
     */
    default <O> RecordCodecBuilder<O, A> field(final String name, final Function<O, A> getter) {
        return Codecs.field(codec(), name, getter);
    }

    /**
     * Provides an optional field component builder for this codec type. Used in building record-like codecs.
     *
     * @param name The name of the field.
     * @param getter The method reference to a getter associated with this field.
     * @return The field component.
     * @param <O> The type of the parent object of this codec.
     */
    default <O> RecordCodecBuilder<O, Optional<A>> optionalField(final String name, final Function<O, Optional<A>> getter) {
        return Codecs.optionalField(codec(), name, getter);
    }

    /**
     * Provides a field list component builder for this codec type. Used in building record-like codecs.
     *
     * @param name The name of the field.
     * @param getter The method reference to a getter associated with this field.
     * @return The field component.
     * @param <O> The type of the parent object of this codec.
     */
    default <O> RecordCodecBuilder<O, List<A>> listField(final String name, final Function<O, List<A>> getter) {
        return Codecs.listField(codec(), name, getter);
    }

    /**
     * Provides an optional field list component builder for this codec type. Used in building record-like codecs.
     *
     * @param name The name of the field.
     * @param getter The method reference to a getter associated with this field.
     * @return The field component.
     * @param <O> The type of the parent object of this codec.
     */
    default <O> RecordCodecBuilder<O, Optional<List<A>>> optionalListField(final String name, final Function<O, Optional<List<A>>> getter) {
        return Codecs.optionalListField(codec(), name, getter);
    }
}
