package com.github.benchmarkr.unit.test;

import com.github.benchmarkr.annotation.Benchmark;
import com.github.benchmarkr.annotation.CustomProperty;
import com.github.benchmarkr.annotation.Description;
import com.github.benchmarkr.annotation.LowerBound;
import com.github.benchmarkr.annotation.TestName;
import com.github.benchmarkr.annotation.UpperBound;
import com.github.benchmarkr.dto.Result;
import com.github.benchmarkr.test.test.TestParameters;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestTest {

  @Benchmark
  @LowerBound(20)
  @UpperBound(30)
  @TestName("TestNameOverride")
  @Description("Method Description")
  @CustomProperty(key = "testKey", value = "testValue")
  public void validMethod() throws Exception {
    Thread.sleep(1);
  }

  @Test
  public void testConstructFromMethod() throws Exception {
    com.github.benchmarkr.test.test.Test test = com.github.benchmarkr.test.test.Test.construct(
        this, this.getClass().getMethod("validMethod"), TestParameters.builder().build());

    assertEquals(test.getTestName(), "TestNameOverride");
    assertEquals(test.getLowerBound(), 20);
    assertEquals(test.getUpperBound(), 30);
    assertEquals(test.getDescription(), "Method Description");
    assertEquals(test.getCustomProperties().get("testKey"), "testValue");
  }

  @Benchmark
  public void validMethodNotAnnotated() {

  }

  @Test
  public void testConstructFromNotAnnotatedMethod() throws Exception {
    com.github.benchmarkr.test.test.Test test = com.github.benchmarkr.test.test.Test.construct(
        this, this.getClass().getMethod("validMethodNotAnnotated"), TestParameters.builder().build());

    assertEquals(test.getTestName(), "com.github.benchmarkr.unit.test.TestTest:validMethodNotAnnotated");
    assertNull(test.getLowerBound());
    assertNull(test.getUpperBound());
    assertNull(test.getDescription());
    assertNull(test.getCustomProperties());
  }

  public void invalidMethodNotAnnotated() {

  }

  @Test
  public void testConstructFromInvalidMethodNotAnnotated() {
    assertThrows(IllegalArgumentException.class, () -> com.github.benchmarkr.test.test.Test.construct(
        this, this.getClass().getMethod("invalidMethodNotAnnotated"),
        TestParameters.builder().build()));
  }

  @Test
  public void testConstructNullClass() {
    assertThrows(IllegalArgumentException.class, () -> com.github.benchmarkr.test.test.Test.construct(
        null, this.getClass().getMethod("validMethodNotAnnotated"),
        TestParameters.builder().build()));
  }

  @Test
  public void testConstructNullMethod() {
    assertThrows(IllegalArgumentException.class, () -> com.github.benchmarkr.test.test.Test.construct(
        this, null, TestParameters.builder().build()));
  }

  @Test
  public void testConstructNullTestParameters() {
    assertThrows(IllegalArgumentException.class, () -> com.github.benchmarkr.test.test.Test.construct(
        this, this.getClass().getMethod("validMethodNotAnnotated"), null));
  }

  @Test
  public void testExecute() throws Exception {
    com.github.benchmarkr.test.test.Test test = com.github.benchmarkr.test.test.Test.construct(
        this, this.getClass().getMethod("validMethod"),
        TestParameters.builder().build());

    Result result = test.execute();

    assertTrue(result.getSignificantSuccess());
    assertTrue(result.getSuccess());
    assertNotNull(result.getTimestamp());
    assertTrue(result.getTestId().length() > 0);
    assertEquals(result.getTestName(), "TestNameOverride");
    assertEquals(result.getLowerBound(), 20);
    assertEquals(result.getUpperBound(), 30);
    assertTrue(result.getDuration() > 0);
    assertEquals(result.getCustomProperties().get("testKey"), "testValue");
  }
}
