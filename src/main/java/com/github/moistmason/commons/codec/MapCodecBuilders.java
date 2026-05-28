package com.github.moistmason.commons.codec;

import com.mojang.datafixers.util.*;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Builder methods for map codecs, mainly record-like map codecs using {@link RecordCodecBuilder}.
 *
 * @author moist-mason
 */
public class MapCodecBuilders {

    /**
     * Creates a map codec for an object with one parameter in its constructor.
     *
     * @param <F1> The first field type.
     * @param <R> The object type.
     * @param function A method reference to an object's constructor (Object::new), or a custom function to build the object.
     * @param field1 The field codec entry for the first parameter.
     * @return The map codec.
     */
    public static <F1, R> MapCodec<R> of(final Function<F1, R> function, final RecordCodecBuilder<R, F1> field1) {
        return RecordCodecBuilder.mapCodec(i -> i.group(
                field1
        ).apply(i, function));
    }

    /**
     * Creates a map codec for an object with two parameters in its constructor.
     *
     * @param <F1> The first parameter type.
     * @param <F2> The second parameter type.
     * @param <R> The object type.
     * @param function A method reference to an object's constructor (Object::new), or a custom function to build the object.
     * @param field1 The field codec entry for the first parameter.
     * @param field2 The field codec entry for the second parameter.
     * @return The map codec.
     */
    public static <F1, F2, R> MapCodec<R> of(
            final BiFunction<F1, F2, R> function,
            final RecordCodecBuilder<R, F1> field1,
            final RecordCodecBuilder<R, F2> field2
    ) {
        return RecordCodecBuilder.mapCodec(i -> i.group(
                field1,
                field2
        ).apply(i, function));
    }

    /**
     * Creates a map codec for an object with three parameters in its constructor.
     *
     * @param <F1> The first parameter type.
     * @param <F2> The second parameter type.
     * @param <F3> The third parameter type.
     * @param <R> The object type.
     * @param function A method reference to an object's constructor (Object::new), or a custom function to build the object.
     * @param field1 The field codec entry for the first parameter.
     * @param field2 The field codec entry for the second parameter.
     * @param field3 The field codec entry for the third parameter.
     * @return The map codec.
     */
    public static <F1, F2, F3, R> MapCodec<R> of(
            final Function3<F1, F2, F3, R> function,
            final RecordCodecBuilder<R, F1> field1,
            final RecordCodecBuilder<R, F2> field2,
            final RecordCodecBuilder<R, F3> field3
    ) {
        return RecordCodecBuilder.mapCodec(i -> i.group(
                field1,
                field2,
                field3
        ).apply(i, function));
    }

    /**
     * Creates a map codec for an object with four parameters in its constructor.
     *
     * @param <F1> The first parameter type.
     * @param <F2> The second parameter type.
     * @param <F3> The third parameter type.
     * @param <F4> The fourth parameter type.
     * @param <R> The object type.
     * @param function A method reference to an object's constructor (Object::new), or a custom function to build the object.
     * @param field1 The field codec entry for the first parameter.
     * @param field2 The field codec entry for the second parameter.
     * @param field3 The field codec entry for the third parameter.
     * @param field4 The field codec entry for the fourth parameter.
     * @return The map codec.
     */
    public static <F1, F2, F3, F4, R> MapCodec<R> of(
            final Function4<F1, F2, F3, F4, R> function,
            final RecordCodecBuilder<R, F1> field1,
            final RecordCodecBuilder<R, F2> field2,
            final RecordCodecBuilder<R, F3> field3,
            final RecordCodecBuilder<R, F4> field4
    ) {
        return RecordCodecBuilder.mapCodec(i -> i.group(
                field1,
                field2,
                field3,
                field4
        ).apply(i, function));
    }

    /**
     * Creates a map codec for an object with five parameters in its constructor.
     *
     * @param <F1> The first parameter type.
     * @param <F2> The second parameter type.
     * @param <F3> The third parameter type.
     * @param <F4> The fourth parameter type.
     * @param <F5> The fifth parameter type.
     * @param <R> The object type.
     * @param function A method reference to an object's constructor (Object::new), or a custom function to build the object.
     * @param field1 The field codec entry for the first parameter.
     * @param field2 The field codec entry for the second parameter.
     * @param field3 The field codec entry for the third parameter.
     * @param field4 The field codec entry for the fourth parameter.
     * @param field5 The field codec entry for the fifth parameter.
     * @return The map codec.
     */
    public static <F1, F2, F3, F4, F5, R> MapCodec<R> of(
            final Function5<F1, F2, F3, F4, F5, R> function,
            final RecordCodecBuilder<R, F1> field1,
            final RecordCodecBuilder<R, F2> field2,
            final RecordCodecBuilder<R, F3> field3,
            final RecordCodecBuilder<R, F4> field4,
            final RecordCodecBuilder<R, F5> field5
    ) {
        return RecordCodecBuilder.mapCodec(i -> i.group(
                field1,
                field2,
                field3,
                field4,
                field5
        ).apply(i, function));
    }

    /**
     * Creates a map codec for an object with six parameters in its constructor.
     *
     * @param <F1> The first parameter type.
     * @param <F2> The second parameter type.
     * @param <F3> The third parameter type.
     * @param <F4> The fourth parameter type.
     * @param <F5> The fifth parameter type.
     * @param <F6> The sixth parameter type.
     * @param <R> The object type.
     * @param function A method reference to an object's constructor (Object::new) or a custom function to build the object.
     * @param field1 The field codec entry for the first parameter.
     * @param field2 The field codec entry for the second parameter.
     * @param field3 The field codec entry for the third parameter.
     * @param field4 The field codec entry for the fourth parameter.
     * @param field5 The field codec entry for the fifth parameter.
     * @param field6 The field codec entry for the sixth parameter.
     * @return The map codec.
     */
    public static <F1, F2, F3, F4, F5, F6, R> MapCodec<R> of(
            final Function6<F1, F2, F3, F4, F5, F6, R> function,
            final RecordCodecBuilder<R, F1> field1,
            final RecordCodecBuilder<R, F2> field2,
            final RecordCodecBuilder<R, F3> field3,
            final RecordCodecBuilder<R, F4> field4,
            final RecordCodecBuilder<R, F5> field5,
            final RecordCodecBuilder<R, F6> field6
    ) {
        return RecordCodecBuilder.mapCodec(i -> i.group(
                field1,
                field2,
                field3,
                field4,
                field5,
                field6
        ).apply(i, function));
    }

    /**
     * Creates a map codec for an object with seven parameters in its constructor.
     *
     * @param <F1> The first parameter type.
     * @param <F2> The second parameter type.
     * @param <F3> The third parameter type.
     * @param <F4> The fourth parameter type.
     * @param <F5> The fifth parameter type.
     * @param <F6> The sixth parameter type.
     * @param <F7> The seventh parameter type.
     * @param <R> The object type.
     * @param function A method reference to an object's constructor (Object::new) or a custom function to build the object.
     * @param field1 The field codec entry for the first parameter.
     * @param field2 The field codec entry for the second parameter.
     * @param field3 The field codec entry for the third parameter.
     * @param field4 The field codec entry for the fourth parameter.
     * @param field5 The field codec entry for the fifth parameter.
     * @param field6 The field codec entry for the sixth parameter.
     * @param field7 The field codec entry for the seventh parameter.
     * @return The map codec.
     */
    public static <F1, F2, F3, F4, F5, F6, F7, R> MapCodec<R> of(
            final Function7<F1, F2, F3, F4, F5, F6, F7, R> function,
            final RecordCodecBuilder<R, F1> field1,
            final RecordCodecBuilder<R, F2> field2,
            final RecordCodecBuilder<R, F3> field3,
            final RecordCodecBuilder<R, F4> field4,
            final RecordCodecBuilder<R, F5> field5,
            final RecordCodecBuilder<R, F6> field6,
            final RecordCodecBuilder<R, F7> field7
    ) {
        return RecordCodecBuilder.mapCodec(i -> i.group(
                field1,
                field2,
                field3,
                field4,
                field5,
                field6,
                field7
        ).apply(i, function));
    }

    /**
     * Creates a map codec for an object with eight parameters in its constructor.
     *
     * @param <F1> The first parameter type.
     * @param <F2> The second parameter type.
     * @param <F3> The third parameter type.
     * @param <F4> The fourth parameter type.
     * @param <F5> The fifth parameter type.
     * @param <F6> The sixth parameter type.
     * @param <F7> The seventh parameter type.
     * @param <F8> The eighth parameter type.
     * @param <R> The object type.
     * @param function A method reference to an object's constructor (Object::new) or a custom function to build the object.
     * @param field1 The field codec entry for the first parameter.
     * @param field2 The field codec entry for the second parameter.
     * @param field3 The field codec entry for the third parameter.
     * @param field4 The field codec entry for the fourth parameter.
     * @param field5 The field codec entry for the fifth parameter.
     * @param field6 The field codec entry for the sixth parameter.
     * @param field7 The field codec entry for the seventh parameter.
     * @param field8 The field codec entry for the eighth parameter.
     * @return The map codec.
     */
    public static <F1, F2, F3, F4, F5, F6, F7, F8, R> MapCodec<R> of(
            final Function8<F1, F2, F3, F4, F5, F6, F7, F8, R> function,
            final RecordCodecBuilder<R, F1> field1,
            final RecordCodecBuilder<R, F2> field2,
            final RecordCodecBuilder<R, F3> field3,
            final RecordCodecBuilder<R, F4> field4,
            final RecordCodecBuilder<R, F5> field5,
            final RecordCodecBuilder<R, F6> field6,
            final RecordCodecBuilder<R, F7> field7,
            final RecordCodecBuilder<R, F8> field8
    ) {
        return RecordCodecBuilder.mapCodec(i -> i.group(
                field1,
                field2,
                field3,
                field4,
                field5,
                field6,
                field7,
                field8
        ).apply(i, function));
    }
}
