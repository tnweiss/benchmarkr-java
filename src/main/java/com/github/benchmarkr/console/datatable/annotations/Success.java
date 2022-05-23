package com.github.benchmarkr.console.datatable.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tests the value and turns it green if it passes
 * and red if it fails.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Success {
  /**
   * Greater than a value.
   *
   * @return value to test.
   */
  double greaterThan() default Double.MAX_VALUE;

  /**
   * Less than a value.
   *
   * @return the value to test
   */
  double lessThan() default Double.MIN_VALUE;

  /**
   * Greater than a value from a method on the object.
   *
   * @return the method used to retrieve a value for testing.
   */
  String greaterThanMethod() default "";

  /**
   * Less than a value from a method on the object.
   *
   * @return the method used to retrieve a value for testing
   */
  String lessThanMethod() default "";
}
