package com.github.benchmarkr.test.test;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

import com.github.benchmarkr.annotation.Benchmark;
import com.github.benchmarkr.console.Console;
import com.github.benchmarkr.console.datatable.DataTables;
import com.github.benchmarkr.dto.Result;
import com.github.benchmarkr.util.Annotations;
import com.github.benchmarkr.util.Methods;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Describes attributes of a test.
 */
@Builder
@Getter
@Log4j2
public class Test {
  private final Long lowerBound;
  private final Long upperBound;
  private final Map<String, String> customProperties;
  private final String testName;

  @Getter(AccessLevel.NONE)
  private final Console console;

  @Getter(AccessLevel.NONE)
  private final Object object;

  @Getter(AccessLevel.NONE)
  private final Method method;

  private final String description;

  /**
   * Construct a test given an object, method, and test parameters.
   *
   * @param object         the object to run the method on.
   * @param method         the method to run
   * @param testParameters test parameters
   * @return an instance of a test
   */
  public static Test construct(Object object, Method method, TestParameters testParameters) {
    if (object == null) {
      throw new IllegalArgumentException("Object can not be null");
    }

    if (method == null) {
      throw new IllegalArgumentException("Method can not be null");
    }

    if (testParameters == null) {
      throw new IllegalArgumentException("TestParameters can not be null");
    }

    if (method.getAnnotation(Benchmark.class) == null) {
      throw new IllegalArgumentException("The provided method was not marked as a benchmark test");
    }

    return Test.builder()
        .object(object)
        .method(method)
        .lowerBound(Annotations.lowerBound(method).orElse(null))
        .upperBound(Annotations.upperBound(method).orElse(null))
        .description(Annotations.description(method).orElse(null))
        .customProperties(Annotations.customProperties(method).orElse(null))
        .testName(Annotations.testName(method).orElse(method.getDeclaringClass().getName() + ":"
            + method.getName()))
        .console(testParameters.getConsole())
        .build();
  }

  /**
   * Execute the test and return the result representing the outcome of the test.
   *
   * @return a result represnting the outcome
   */
  public Result execute() {
    boolean success = false;
    Boolean significantSuccess = null;
    Long duration = null;
    final long timestamp = System.currentTimeMillis();
    String outcome = null;

    try {
      long start = System.currentTimeMillis();
      Methods.invoke(object, method);
      long end = System.currentTimeMillis();

      duration = end - start;

      if (upperBound != null) {
        success = duration < upperBound;

        if (!success) {
          outcome = String.format("Test Duration exceeded UpperBound. %s < %s", upperBound, duration);
        }
      } else {
        success = true;
      }

      if (lowerBound != null) {
        significantSuccess = duration < lowerBound;
      }
    } catch (Exception ex) {
      log.error("Benchmark Test Failure on {}", testName, ex);
      outcome = ex.getMessage();
    }

    Result result = Result.builder()
        .lowerBound(lowerBound)
        .upperBound(upperBound)
        .customProperties(customProperties)
        .duration(duration)
        .testId(UUID.randomUUID().toString())
        .testName(testName)
        .timestamp(timestamp)
        .success(success)
        .significantSuccess(significantSuccess)
        .description(description)
        .outcome(outcome)
        .build();

    DataTables.RESULT_DATA_TABLE.print(console, result);

    return result;
  }
}
