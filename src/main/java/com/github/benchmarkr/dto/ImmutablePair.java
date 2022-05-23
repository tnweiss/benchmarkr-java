package com.github.benchmarkr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Key value pair.
 *
 * @param <K> key type
 * @param <V> value type
 */
@Builder
@AllArgsConstructor
@Getter
@ToString
public class ImmutablePair<K, V> {
  private final K key;
  private final V value;
}
