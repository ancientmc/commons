package com.ancientmc.commons.type;

/**
 * Basic implementation of an immutable triple object.
 *
 * @author moist-mason
 *
 * @param left The left value.
 * @param middle The middle value.
 * @param right The right value.
 * @param <L> The left type.
 * @param <M> The middle type.
 * @param <R> The right type.
 */
public record Triple<L, M, R>(L left, M middle, R right) { }
