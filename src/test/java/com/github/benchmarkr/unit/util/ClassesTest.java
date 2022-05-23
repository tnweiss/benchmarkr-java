package com.github.benchmarkr.unit.util;

import java.util.List;

import com.github.benchmarkr.annotation.Benchmark;
import com.github.benchmarkr.util.Classes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClassesTest {
  @Benchmark
  public void testMethodThatIsUniqueToThisClass() {

  }

  @Test
  public void testInPackage() {
    List<Class<?>> classes = Classes.inPackage(this.getClass().getClassLoader(), "com.github.benchmarkr.validscenario");
    Assertions.assertFalse(classes.isEmpty());

    Assertions.assertThrows(IllegalStateException.class, () -> Classes.inPackage(
        this.getClass().getClassLoader(), null));
  }

  @Test
  public void testWithMethodsHavingAnnotation() {
    long count = Classes.inPackage(this.getClass().getClassLoader(), "com.github.benchmarkr.validscenario")
        .stream()
        .filter(Classes.withMethodsHavingAnnotation(Benchmark.class))
        .count();
    Assertions.assertTrue(count > 0);
  }

  @Test
  public void testWithMethod() {
    long count = Classes.inPackage(this.getClass().getClassLoader(), "com.github.benchmarkr.unit.util")
        .stream()
        .filter(Classes.withMethod("testMethodThatIsUniqueToThisClass"))
        .count();
    Assertions.assertEquals(count, 1);
  }

  @Test
  public void testWithClassName() {
    long count = Classes.inPackage(this.getClass().getClassLoader(), "com.github.benchmarkr.unit.util")
        .stream()
        .filter(Classes.withClassname("ClassesTest"))
        .count();
    Assertions.assertEquals(count, 1);
  }

  @Test
  public void testMethodsHavingAnnotation() {
    long count = Classes.inPackage(this.getClass().getClassLoader(), "com.github.benchmarkr.unit.util")
        .stream()
        .flatMap(Classes.methodsHavingAnnotation(Benchmark.class))
        .count();
    Assertions.assertEquals(count, 2);
  }

  @Test
  public void testInstantiate() {
    Object object = Classes.instantiate(this.getClass());
    Assertions.assertNotNull(object);

    Assertions.assertThrows(IllegalStateException.class, () -> Classes.instantiate(PrivateConstructor.class));
    Assertions.assertThrows(IllegalStateException.class, () -> Classes.instantiate(NoDefaultConstructor.class));
  }

  public static class PrivateConstructor {
    private PrivateConstructor() {

    }
  }

  public static class NoDefaultConstructor {
    private NoDefaultConstructor(String val) {

    }
  }
}
