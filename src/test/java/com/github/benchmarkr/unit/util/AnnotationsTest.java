package com.github.benchmarkr.unit.util;

import com.github.benchmarkr.annotation.After;
import com.github.benchmarkr.annotation.AfterAll;
import com.github.benchmarkr.annotation.Before;
import com.github.benchmarkr.annotation.BeforeAll;
import com.github.benchmarkr.annotation.BeforeClass;
import com.github.benchmarkr.annotation.Benchmark;
import com.github.benchmarkr.annotation.CustomProperty;
import com.github.benchmarkr.annotation.Description;
import com.github.benchmarkr.annotation.LowerBound;
import com.github.benchmarkr.annotation.TestName;
import com.github.benchmarkr.annotation.UpperBound;
import com.github.benchmarkr.util.Annotations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnnotationsTest {
  public void noAnnotation() {

  }

  @TestName("test")
  @Description("desc")
  @UpperBound(3)
  @LowerBound(2)
  public void withAnnotation() {

  }

  @CustomProperty(key = "key", value = "value")
  public void customAnnotation() {

  }

  @CustomProperty(key = "keya", value = "value")
  @CustomProperty(key = "keyb", value = "value")
  public void customAnnotations() {

  }

  @BeforeAll
  public static void staticRunnable() {

  }

  @BeforeClass
  public static void beforeClass1() {

  }

  @BeforeClass
  public static void beforeClass2() {

  }

  @Before
  public void before() {

  }

  @After
  public void after1() {

  }

  @After
  public void after2() {

  }

  @Test
  public void testLowerBound() throws Exception {
    Assertions.assertTrue(Annotations.lowerBound(this.getClass().getMethod("noAnnotation")).isEmpty());
    Assertions.assertEquals(Annotations.lowerBound(this.getClass().getMethod("withAnnotation"))
        .get(), 2L);
  }

  @Test
  public void testUpperBound() throws Exception {
    Assertions.assertTrue(Annotations.upperBound(this.getClass().getMethod("noAnnotation")).isEmpty());
    Assertions.assertEquals(Annotations.upperBound(this.getClass().getMethod("withAnnotation"))
        .get(), 3L);
  }

  @Test
  public void testDescription() throws Exception {
    Assertions.assertTrue(Annotations.description(this.getClass().getMethod("noAnnotation")).isEmpty());
    Assertions.assertEquals(Annotations.description(this.getClass().getMethod("withAnnotation"))
        .get(), "desc");
  }

  @Test
  public void testTestName() throws Exception {
    Assertions.assertTrue(Annotations.testName(this.getClass().getMethod("noAnnotation")).isEmpty());
    Assertions.assertEquals(Annotations.testName(this.getClass().getMethod("withAnnotation"))
        .get(), "test");
  }

  @Test
  public void testCustomProperties() throws Exception {
    Assertions.assertTrue(Annotations.customProperties(this.getClass().getMethod("noAnnotation")).isEmpty());
    Assertions.assertEquals(Annotations.customProperties(this.getClass().getMethod("customAnnotation"))
        .get().size(), 1);
    Assertions.assertEquals(Annotations.customProperties(this.getClass().getMethod("customAnnotations"))
        .get().size(), 2);
  }

  @Test
  public void testStaticRunnable() {
    Assertions.assertTrue(Annotations.staticRunnable(this.getClass(), AfterAll.class).isEmpty());
    Assertions.assertFalse(Annotations.staticRunnable(this.getClass(), BeforeAll.class).isEmpty());

    Assertions.assertThrows(IllegalStateException.class,
        () -> Annotations.staticRunnable(this.getClass(), BeforeClass.class));
  }

  @Test
  public void testRunnable() {
    Assertions.assertTrue(Annotations.runnable(this.getClass(), Benchmark.class).isEmpty());
    Assertions.assertFalse(Annotations.runnable(this.getClass(), Before.class).isEmpty());

    Assertions.assertThrows(IllegalStateException.class,
        () -> Annotations.staticRunnable(this.getClass(), After.class));
  }
}
