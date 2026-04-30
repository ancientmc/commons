package com.ancientmc.commons.type;

/**
 * Basic implementation of an immutable pair object.
 *
 * @author moist-mason
 *
 * @param left The left value.
 * @param right The right value.
 * @param <L> The left type.
 * @param <R> The right type.
 */
public record Pair<L, R>(L left, R right) { }
