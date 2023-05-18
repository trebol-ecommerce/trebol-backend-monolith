package org.trebol.common;

import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Utils {

  /**
   * Looks up a Map's entries whose keys match a given prefix.
   * Then it makes a copy of every entry and removes said prefix from its keys.
   *
   * @param sourceMap The original Map with String keys
   * @param prefix The prefix that will be removed from each matching Map entry
   * @return A copy of the original map, with transformations as explained above
   * @param <V> sourceMap's entries value type parameter
   */
  public static <V> Map<String, V> copyMapWithUnprefixedEntries(Map<String, V> sourceMap, String prefix) {
    return sourceMap.entrySet().stream()
      .filter(entry -> entry.getKey().startsWith(prefix))
      .map(entry -> Map.entry(
        entry.getKey().replace(prefix, ""),
        entry.getValue()))
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
