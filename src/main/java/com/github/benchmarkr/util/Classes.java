package com.github.benchmarkr.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.reflect.ClassPath;

import lombok.extern.log4j.Log4j2;

/**
 * provides static utility methods for classes.
 */
@Log4j2
public class Classes {
  /**
   * Discover all classes in the classpath with the provided package regex.
   *
   * @param packageName package name regex, formatted as "benchmarkr.".
   * @return return a stream of class infos.
   */
  public static List<Class<?>> inPackage(ClassLoader classLoader, String packageName) {
    try {
      if (packageName == null) {
        throw new IllegalStateException("Package name can not be null");
      }

      return ClassPath.from(classLoader)
          .getTopLevelClassesRecursive(packageName)
          .stream()
          .map(ClassPath.ClassInfo::load)
          .collect(Collectors.toList());
    } catch (Exception ex) {
      log.error("Unable to locate classes in {}", packageName, ex);
      throw new IllegalStateException(ex);
    }
  }

  /**
   * Returns a predicate that looks for classes containing methods with a particular annotation.
   *
   * @param annotation the annotation to search for.
   * @param <T>        the type of annotation.
   * @return a predicate to test for matches.
   */
  public static <T extends Annotation> Predicate<Class<?>> withMethodsHavingAnnotation(Class<T> annotation) {
    return c -> Arrays.stream(c.getDeclaredMethods()).anyMatch(a -> a.getAnnotation(annotation) != null);
  }

  /**
   * Filter out classes not having a method with the provided name.
   *
   * @param methodName the name of the method.
   * @return a predicate used to test
   */
  public static Predicate<Class<?>> withMethod(String methodName) {
    if (methodName == null || methodName.isBlank()) {
      return c -> true;
    } else {
      return c -> Arrays.stream(c.getDeclaredMethods()).anyMatch(m -> m.getName().equals(methodName));
    }
  }

  /**
   * Filter out classes with the provided name.
   *
   * @param className the name of the class
   * @return a predicate used to test
   */
  public static Predicate<Class<?>> withClassname(String className) {
    if (className == null || className.isBlank()) {
      return c -> true;
    } else {
      return c -> c.getSimpleName().equals(className);
    }
  }

  /**
   * Creates a function to map classes to a list of methods having a given annotation.
   *
   * @param annotation the target annotation.
   * @param <T>        the type of annotation
   * @return a function used to extract methods with a given annotation
   */
  public static <T extends Annotation> Function<Class<?>, Stream<Method>> methodsHavingAnnotation(
      Class<T> annotation) {
    return c -> Arrays.stream(c.getDeclaredMethods()).filter(a -> a.getAnnotation(annotation) != null);
  }

  /**
   * Instantiate the provided class using the default constructor.
   *
   * @param clazz the class to instantiate.
   * @return the provided class instantiated
   */
  public static Object instantiate(Class<?> clazz) {
    try {
      return clazz.getDeclaredConstructor().newInstance();
    } catch (NoSuchMethodException ex) {
      log.error("Unable to instantiate {} default constructor not found", clazz.getName(), ex);
      throw new IllegalStateException(ex);
    } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
      log.error("Unable to instantiate {} constructor must be public and require no parameters",
          clazz.getName(), ex);
      throw new IllegalStateException(ex);
    }
  }

}
