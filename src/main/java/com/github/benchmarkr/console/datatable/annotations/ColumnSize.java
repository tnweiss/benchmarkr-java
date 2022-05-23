package com.github.benchmarkr.console.datatable.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Overrides the size of the column.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnSize {
  /**
   * The proportion, ranging from 0.0 to 1.0.
   *
   * @return the column proportion
   */
  double value();
}
