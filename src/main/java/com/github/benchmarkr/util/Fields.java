package com.github.benchmarkr.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.function.Predicate;

import com.github.benchmarkr.console.datatable.annotations.ColumnName;

/**
 * Static utility methods for fields.
 */
public class Fields {
  public static <T extends Annotation> Predicate<Field> havingAnnotation(Class<T> annotation) {
    return f -> f.getAnnotation(annotation) != null;
  }

  /**
   * Get the name of a column using the ColumnName annotation.
   *
   * @param field the field to get the annotation from
   * @return a string representation of the field
   */
  public static String columnName(Field field) {
    ColumnName annotation = field.getAnnotation(ColumnName.class);

    if (annotation != null) {
      return annotation.value();
    }

    return field.getName();
  }
}
