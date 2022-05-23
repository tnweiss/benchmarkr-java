package com.github.benchmarkr.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to set a custom property on a test.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(CustomProperties.class)
public @interface CustomProperty {
  /**
   * The property key.
   *
   * @return the property key
   */
  String key();

  /**
   * The property value.
   *
   * @return the property value
   */
  String value();
}
