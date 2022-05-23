package com.github.benchmarkr.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to note the upper bound of a test.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UpperBound {
  /**
   * The tests upper bound.
   *
   * @return the tests upper bound
   */
  long value();
}
