package com.github.benchmarkr.unit.test;

import java.lang.reflect.Method;
import java.util.List;

import com.github.benchmarkr.annotation.After;
import com.github.benchmarkr.annotation.AfterClass;
import com.github.benchmarkr.annotation.Before;
import com.github.benchmarkr.annotation.BeforeClass;
import com.github.benchmarkr.annotation.Benchmark;
import com.github.benchmarkr.dto.Result;
import com.github.benchmarkr.test.group.TestGroup;
import com.github.benchmarkr.test.group.TestGroupParameters;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGroupTest {
  private static boolean beforeGroup = false;
  private boolean before = false;
  private boolean benchmarkA = false;
  private boolean benchmarkB = false;
  private boolean after = false;
  private static boolean afterGroup = false;

  public void reset() {
    beforeGroup = false;
    before = false;
    benchmarkA = false;
    benchmarkB = false;
    after = false;
    afterGroup = false;
  }

  public void assertAll(Object testGroupTestObject, boolean expected) {
    TestGroupTest testGroupTest = (TestGroupTest) testGroupTestObject;

    assertEquals(expected, beforeGroup);
    assertEquals(expected, testGroupTest.before);
    assertEquals(expected, testGroupTest.benchmarkA);
    assertEquals(expected, testGroupTest.benchmarkB);
    assertEquals(expected, testGroupTest.after);
    assertEquals(expected, afterGroup);
  }

  @BeforeClass
  public static void beforeGroup() {
    beforeGroup = true;
  }

  @Before
  public void before() {
    before = true;
  }

  @Benchmark
  public void benchmarkA() {
    benchmarkA = true;
  }

  @Benchmark
  public void benchmarkB() {
    benchmarkB = true;
  }

  @After
  public void after() {
    after = true;
  }

  @AfterClass
  public static void afterGroup() {
    afterGroup = true;
  }

  @Test
  public void testConstruct() throws NoSuchMethodException {
    List<Method> methods = List.of(
        this.getClass().getMethod("benchmarkA"),
        this.getClass().getMethod("benchmarkB")
    );

    reset();
    TestGroup testGroup = TestGroup.construct(this.getClass(), methods, TestGroupParameters.builder().build());
    assertAll(testGroup.getObject(), false);
  }

  @Test
  public void testExecute() throws NoSuchMethodException {
    List<Method> methods = List.of(
        this.getClass().getMethod("benchmarkA"),
        this.getClass().getMethod("benchmarkB")
    );

    reset();
    TestGroup testGroup = TestGroup.construct(this.getClass(), methods, TestGroupParameters.builder().build());
    assertAll(testGroup.getObject(), false);

    List<Result> results = testGroup.execute();
    assertEquals(results.size(), 2);
    assertAll(testGroup.getObject(), true);
  }
}
