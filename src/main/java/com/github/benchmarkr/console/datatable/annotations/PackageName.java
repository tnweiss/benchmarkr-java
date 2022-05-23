package com.github.benchmarkr.console.datatable.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Transforms the value of the column.
 * A test name "org.example.Example:test" would
 * turn into "o.e.Example:test"
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PackageName {
}
