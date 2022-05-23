package com.github.benchmarkr.test.group;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.github.benchmarkr.annotation.After;
import com.github.benchmarkr.annotation.AfterClass;
import com.github.benchmarkr.annotation.Before;
import com.github.benchmarkr.annotation.BeforeClass;
import com.github.benchmarkr.dto.Result;
import com.github.benchmarkr.test.test.Test;
import com.github.benchmarkr.test.test.TestParameters;
import com.github.benchmarkr.util.Annotations;
import com.github.benchmarkr.util.Classes;

import lombok.Builder;
import lombok.Getter;

/**
 * Collection of test related to one and other.
 */
@Builder
public class TestGroup {
  @Getter
  private final Object object;
  private final Runnable beforeGroup;
  private final Consumer<Object> beforeTest;
  private final List<Test> tests;
  private final Consumer<Object> afterTest;
  private final Runnable afterGroup;

  private final Integer iterations;

  /**
   * Construct a test group provided a set of test methods and the class the methods belong to.
   *
   * @param clazz               the class containing the methods.
   * @param methods             the test methods
   * @param testGroupParameters the test parameters
   * @return an instance of TestGroup
   */
  public static TestGroup construct(Class<?> clazz, List<Method> methods, TestGroupParameters testGroupParameters) {
    // instantiate the test class
    Object object = Classes.instantiate(clazz);

    // construct test parameters
    TestParameters testParameters = TestParameters.from(testGroupParameters);

    // construct tests
    List<Test> tests = methods.stream()
        .map(method -> Test.construct(object, method, testParameters))
        .collect(Collectors.toList());

    return TestGroup.builder()
        .object(object)
        .beforeGroup(Annotations.staticRunnable(clazz, BeforeClass.class).orElse(null))
        .beforeTest(Annotations.runnable(clazz, Before.class).orElse(null))
        .tests(tests)
        .afterTest(Annotations.runnable(clazz, After.class).orElse(null))
        .afterGroup(Annotations.staticRunnable(clazz, AfterClass.class).orElse(null))
        .iterations(testGroupParameters.getIterations())
        .build();
  }

  /**
   * Execute the provided test group.
   *
   * @return return a list of results
   */
  public List<Result> execute() {
    // collection of results
    List<Result> results = new LinkedList<>();

    for (int i = 0; i < iterations; i++) {
      // run the class setup
      if (beforeGroup != null) {
        beforeGroup.run();
      }

      // randomize execution order of tests
      Collections.shuffle(tests);

      for (Test test : tests) {
        // run the test setup
        if (beforeTest != null) {
          beforeTest.accept(object);
        }

        // run the test
        results.add(test.execute());

        // run the test teardown
        if (afterTest != null) {
          afterTest.accept(object);
        }
      }

      // run the class teardown
      if (afterGroup != null) {
        afterGroup.run();
      }
    }

    return results;
  }
}
