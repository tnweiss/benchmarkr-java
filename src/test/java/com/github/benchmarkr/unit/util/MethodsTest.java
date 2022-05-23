package com.github.benchmarkr.unit.util;

import java.util.Arrays;
import java.util.List;

import com.github.benchmarkr.annotation.AfterAll;
import com.github.benchmarkr.annotation.Benchmark;
import com.github.benchmarkr.console.datatable.annotations.Column;
import com.github.benchmarkr.util.Methods;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MethodsTest {
  private static boolean VAR = false;

  public void method() {
    VAR = true;
  }

  public void methodWithParam(String param) {

  }

  @AfterAll
  public static void staticMethod() {
    VAR = true;
  }

  @AfterAll()
  @Benchmark
  public static String getStatic() {
    return "Hello Static";
  }

  public static String getStatic(String uhoh) {
    return "Hello Static";
  }

  public String get() {
    return "Hello Static";
  }

  public String get(String uhoh) {
    return "Hello Static";
  }

  @Test
  public void testInvokeMethodOnObject() throws Exception {
    VAR = false;
    Methods.invoke(this, this.getClass().getMethod("method"));
    Assertions.assertTrue(VAR);

    Assertions.assertThrows(IllegalStateException.class,
        () -> Methods.invoke(this, this.getClass().getMethod("methodWithParam", String.class)));
  }

  @Test
  public void testInvokeStaticMethod() throws Exception {
    VAR = false;
    Methods.invoke(this.getClass().getMethod("staticMethod"));
    Assertions.assertTrue(VAR);

    Assertions.assertThrows(IllegalStateException.class,
        () -> Methods.invoke(this.getClass().getMethod("method")));
  }

  @Test
  public void testInvokeStaticMethodWReturn() throws Exception {
    Assertions.assertEquals(Methods.invoke(this.getClass().getMethod("getStatic"), String.class),
        "Hello Static");

    Assertions.assertThrows(IllegalStateException.class,
        () -> Methods.invoke(this.getClass().getMethod("getStatic"), Double.class));

    Assertions.assertThrows(IllegalStateException.class,
        () -> Methods.invoke(this.getClass().getMethod("getStatic", String.class), Double.class));
  }

  @Test
  public void testInvokeMethodWReturn() throws Exception {
    Assertions.assertEquals(Methods.invoke(this, "get", String.class),
        "Hello Static");

    Assertions.assertThrows(IllegalStateException.class,
        () -> Methods.invoke(this, "get", Double.class));

    Assertions.assertThrows(IllegalStateException.class,
        () -> Methods.invoke(this, "doesntExist", String.class));
  }

  @Test
  public void annotatedWith() {
    Assertions.assertEquals(1,
        Arrays.stream(this.getClass().getMethods()).filter(Methods.annotatedWith(Benchmark.class)).count());
  }

  @Test
  public void runnableAnnotatedWith() {
    List<Class<?>> classes = List.of(this.getClass());

    Methods.runnableAnnotatedWith(classes, Benchmark.class).get().run();

    Assertions.assertTrue(Methods.runnableAnnotatedWith(classes, Column.class).isEmpty());

    Assertions.assertThrows(IllegalStateException.class,
        () -> Methods.runnableAnnotatedWith(classes, AfterAll.class));
  }
}
