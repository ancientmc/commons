package com.github.moistmason.commons.type;

import com.github.moistmason.commons.StringUtil;
import org.jspecify.annotations.NonNull;

/**
 * Basic implementation of an immutable triple object.
 *
 * @author moist-mason
 *
 * @param <L> The left type.
 * @param <M> The middle type.
 * @param <R> The right type.
 * @param left The left value.
 * @param middle The middle value.
 * @param right The right value.
 */
public record Triple<L, M, R>(L left, M middle, R right) {

    @Override
    public @NonNull String toString() {
        final String prefix = StringUtil.spaced("Triple",
                "with Left Type:", StringUtil.typeName(left),
                "and Middle Type", StringUtil.typeName(middle),
                "and Right Type",  StringUtil.typeName(right)
        );

        final String values = StringUtil.commas(left.toString(), middle.toString(), right.toString());
        return prefix + " -> " + StringUtil.brackets(values);
    }
}
