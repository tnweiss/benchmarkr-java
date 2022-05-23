package com.github.benchmarkr.console;

/**
 * Interface for console.
 */
@FunctionalInterface
public interface Console {
  /**
   * Print the given text.
   *
   * @param txt the text to print
   */
  void print(String txt);
}
