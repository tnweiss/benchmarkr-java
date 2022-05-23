package com.github.benchmarkr.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The tests lower bound.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LowerBound {
  /**
   * The lower bound, in ms, for a test.
   *
   * @return the tests lower bound in ms.
   */
  long value();
}
