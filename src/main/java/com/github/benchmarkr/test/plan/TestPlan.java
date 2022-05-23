package com.github.benchmarkr.test.plan;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.github.benchmarkr.annotation.AfterAll;
import com.github.benchmarkr.annotation.BeforeAll;
import com.github.benchmarkr.annotation.Benchmark;
import com.github.benchmarkr.console.Console;
import com.github.benchmarkr.console.datatable.DataTables;
import com.github.benchmarkr.dto.Result;
import com.github.benchmarkr.dto.Results;
import com.github.benchmarkr.test.group.TestGroup;
import com.github.benchmarkr.test.group.TestGroupParameters;
import com.github.benchmarkr.util.Classes;
import com.github.benchmarkr.util.Methods;

import lombok.Builder;

/**
 * Test plan describes all tests.
 */
@Builder
public class TestPlan {
  private final Runnable beforeAll;
  private final List<TestGroup> testGroups;
  private final Runnable afterAll;

  private final Console console;

  private final Boolean recordResults;

  private com.github.benchmarkr.dto.TestContext testContext;

  /**
   * Construct a test plan from the provided test plan parameters.
   *
   * @param testPlanParameters parameters for the test plan
   * @return a test plan
   */
  public static TestPlan construct(TestPlanParameters testPlanParameters) {
    // get the global beforeAll and afterAll
    List<Class<?>> classesInPackage = Classes.inPackage(
        testPlanParameters.getClassLoader(), testPlanParameters.getPackageName());

    Optional<Runnable> beforeAll = Methods.runnableAnnotatedWith(classesInPackage, BeforeAll.class);
    Optional<Runnable> afterAll = Methods.runnableAnnotatedWith(classesInPackage, AfterAll.class);
    Supplier<com.github.benchmarkr.dto.TestContext> testContext = Methods.runnableAnnotatedWith(classesInPackage,
            com.github.benchmarkr.annotation.TestContext.class, com.github.benchmarkr.dto.TestContext.class)
        .orElse(com.github.benchmarkr.dto.TestContext::defaultTestContext);

    // construct the test group parameters
    TestGroupParameters testGroupParameters = TestGroupParameters.from(testPlanParameters);

    // get all possible tests for this test context
    List<TestGroup> testGroups = Classes.inPackage(
        testPlanParameters.getClassLoader(), testPlanParameters.getPackageName())
        .stream()
        .filter(Classes.withClassname(testPlanParameters.getClassName()))
        .filter(Classes.withMethod(testPlanParameters.getMethodName()))
        .flatMap(Classes.methodsHavingAnnotation(Benchmark.class))
        .collect(Collectors.groupingBy(Method::getDeclaringClass, Collectors.toList()))
        .entrySet()
        .stream()
        .map(e -> TestGroup.construct(e.getKey(), e.getValue(), testGroupParameters))
        .collect(Collectors.toList());

    return TestPlan.builder()
        .beforeAll(beforeAll.orElse(null))
        .afterAll(afterAll.orElse(null))
        .testGroups(testGroups)
        .console(testPlanParameters.getConsole())
        .recordResults(testPlanParameters.isRecordResults())
        .testContext(testContext.get())
        .build();
  }

  /**
   * Execute the test plan.
   */
  public Results execute() {
    // run test setup
    if (beforeAll != null) {
      beforeAll.run();
    }

    // print test header
    DataTables.RESULT_DATA_TABLE.printHeader(console);

    List<Result> results = new LinkedList<>();

    // run each test group
    for (TestGroup testGroup : testGroups) {
      results.addAll(testGroup.execute());
    }

    // run test teardown
    if (afterAll != null) {
      afterAll.run();
    }

    return Results.builder()
        .testContext(testContext)
        .results(results)
        .build();
  }

}
