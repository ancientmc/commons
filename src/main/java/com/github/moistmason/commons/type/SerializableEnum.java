package com.github.moistmason.commons.type;

import com.github.moistmason.commons.Util;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import org.jspecify.annotations.Nullable;

import java.util.Locale;
import java.util.function.Function;

/**
 * Helps to serialize an enum constant as a string. Simplified implementation of {@code net.minecraft.util.StringRepresentable}.
 *
 * <p> Usage: Implement this interface into an enum declaration and return {@link SerializableEnum#getSerializedName()}
 * with a string value of your choice. This string will represent the serialized form of an enum constant.
 * If {@link SerializableEnum#getSerializedName()} returns {@code null}, the enum constant will be serialized as its {@link Enum#name()} in lowercase. </p>
 *
 * @author moist-mason
 */
public interface SerializableEnum {

    /**
     * The name of an enum constant as it appears in a serialized format (ex. JSON). This value is nullable.
     *
     * @return The name.
     */
    @Nullable String getSerializedName();

    /**
     * Creates a codec based on the provided values of an enum class.
     *
     * @param <E> The enum type.
     * @param values The values. of the enum.
     * @return The codec.
     */
    static <E extends Enum<E> & SerializableEnum> EnumCodec<E> codec(final E[] values) {
        return new EnumCodec<>(enumResolver(values, nameResolver()));
    }

    /**
     * Converts the enum constant into the value returned in {@link SerializableEnum#getSerializedName()}. If that value is null,
     * the function returns {@link Enum#name()}.
     *
     * @param <E> The enum type.
     * @return The conversion function.
     */
    static <E extends Enum<E> & SerializableEnum> Function<E, String> nameResolver() {
        return e -> Util.get(e.getSerializedName(), e.name().toLowerCase(Locale.ROOT));
    }

    /**
     * Converts the name into its enum constant equivalent.
     *
     * @param <E> The enum type.
     * @param values The values of the enum.
     * @param nameResolver The resolver function for converting the enum into a string.
     * @return The conversion function.
     */
    static <E extends Enum<E> & SerializableEnum> Function<String, E> enumResolver(final E[] values, final Function<E, String> nameResolver) {
        return id -> {
            for (E value : values) {
                if (nameResolver.apply(value).equals(id)) {
                    return value;
                }
            }

            return null;
        };
    }

    /**
     * A codec representing enum constants, serialized as a string.
     *
     * @param <E> The enum type.
     */
    class EnumCodec<E extends Enum<E> & SerializableEnum> implements Codec<E> {

        /** The internal codec type used for serializing/deserializing. */
        private final Codec<E> codec;

        /** @param enumResolver The enum resolver function, as defined by {@link SerializableEnum#enumResolver(Enum[], Function)}. */
        public EnumCodec(final Function<String, E> enumResolver) {
            this.codec = Codec.stringResolver(SerializableEnum.nameResolver(), enumResolver);
        }

        @Override
        public <T> DataResult<Pair<E, T>> decode(final DynamicOps<T> ops, final T input) {
            return codec.decode(ops, input);
        }

        @Override
        public <T> DataResult<T> encode(final E input, final DynamicOps<T> ops, T prefix) {
            return codec.encode(input, ops, prefix);
        }
    }
}
