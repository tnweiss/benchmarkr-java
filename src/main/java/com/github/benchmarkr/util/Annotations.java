package com.github.benchmarkr.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.github.benchmarkr.annotation.CustomProperties;
import com.github.benchmarkr.annotation.CustomProperty;
import com.github.benchmarkr.annotation.Description;
import com.github.benchmarkr.annotation.LowerBound;
import com.github.benchmarkr.annotation.TestName;
import com.github.benchmarkr.annotation.UpperBound;
import com.github.benchmarkr.dto.TestContext;
import com.google.common.collect.ImmutableMap;

/**
 * Static annotation methods.
 */
public class Annotations {
  /**
   * Given a method, extract the lower bound annotation value if present.
   *
   * @param method the method to extract the annotation from.
   * @return the lower bound, if annotated
   */
  public static Optional<Long> lowerBound(Method method) {
    LowerBound annotation = method.getAnnotation(LowerBound.class);
    if (annotation != null) {
      return Optional.of(annotation.value());
    }
    return Optional.empty();
  }

  /**
   * Given a method, extract the upper bound annotation value if present.
   *
   * @param method the method to extract the annotation from.
   * @return the upper bound, if annotated
   */
  public static Optional<Long> upperBound(Method method) {
    UpperBound annotation = method.getAnnotation(UpperBound.class);
    if (annotation != null) {
      return Optional.of(annotation.value());
    }
    return Optional.empty();
  }

  /**
   * Given a method, extract the description value if present.
   *
   * @param method the method to extract the annotation from.
   * @return the description, if annotated
   */
  public static Optional<String> description(Method method) {
    Description annotation = method.getAnnotation(Description.class);
    if (annotation != null) {
      return Optional.of(annotation.value());
    }
    return Optional.empty();
  }

  /**
   * Given a method, extract the test name value if present.
   *
   * @param method the method to extract the annotation from.
   * @return the test name, if annotated
   */
  public static Optional<String> testName(Method method) {
    TestName annotation = method.getAnnotation(TestName.class);
    if (annotation != null) {
      return Optional.of(annotation.value());
    }
    return Optional.empty();
  }

  /**
   * Given a method, extract the custom properties if present.
   *
   * @param method the method to extract the annotation from.
   * @return the custom properties, if annotated
   */
  public static Optional<Map<String, String>> customProperties(Method method) {
    CustomProperties annotations = method.getAnnotation(CustomProperties.class);
    if (annotations != null) {
      final ImmutableMap.Builder<String, String> propertiesBuilder = ImmutableMap.builder();

      for (CustomProperty customProperty : annotations.value()) {
        propertiesBuilder.put(customProperty.key(), customProperty.value());
      }

      return Optional.of(propertiesBuilder.build());
    }

    CustomProperty annotation = method.getAnnotation(CustomProperty.class);
    if (annotation != null) {
      return Optional.of(
          ImmutableMap.<String, String>builder()
              .put(annotation.key(), annotation.value())
              .build()
      );
    }

    return Optional.empty();
  }

  /**
   * Provided a class, get a single runnable.
   *
   * @param clazz      the class
   * @param annotation the annotation identifying the runnable
   * @param <T>        the type of annotation
   * @return a runnable, if provided
   */
  public static <T extends Annotation> Optional<Runnable> staticRunnable(Class<?> clazz,
                                                                         Class<T> annotation) {
    List<Method> methods = Arrays.stream(clazz.getMethods())
        .filter(Methods.annotatedWith(annotation))
        .collect(Collectors.toList());

    if (methods.size() == 1) {
      return Optional.of(() -> Methods.invoke(methods.get(0)));
    } else if (methods.isEmpty()) {
      return Optional.empty();
    } else {
      throw new IllegalStateException("Multiple static methods ("
          + methods.stream().map(Method::getName).collect(Collectors.joining(", "))
          + ") in class "
          + clazz.getName()
          + "  annotated with "
          + annotation.getName());
    }
  }

  /**
   * Get a runnable from a method in a class with a specific annotation.
   *
   * @param clazz      The class to search for methods with annotation
   * @param annotation the annotation marking the runnable
   * @param <T>        the type of annotation
   * @return an optional consumer
   */
  public static <T extends Annotation> Optional<Consumer<Object>> runnable(Class<?> clazz,
                                                                           Class<T> annotation) {
    List<Method> methods = Arrays.stream(clazz.getMethods())
        .filter(Methods.annotatedWith(annotation))
        .collect(Collectors.toList());

    if (methods.size() == 1) {
      return Optional.of(o -> Methods.invoke(o, methods.get(0)));
    } else if (methods.isEmpty()) {
      return Optional.empty();
    } else {
      throw new IllegalStateException("Multiple static methods ("
          + methods.stream().map(Method::getName).collect(Collectors.joining(", "))
          + ") in class "
          + clazz.getName()
          + "  annotated with "
          + annotation.getName());
    }
  }

  /**
   * If present, get a test context otherwise return the default.
   *
   * @param classes the classes containing TestContext
   * @return a test context
   */
  public static TestContext testContext(List<Class<?>> classes) {
    List<Method> methods = classes.stream()
        .filter(Classes.withMethodsHavingAnnotation(com.github.benchmarkr.annotation.TestContext.class))
        .flatMap(Classes.methodsHavingAnnotation(com.github.benchmarkr.annotation.TestContext.class))
        .collect(Collectors.toList());

    if (methods.isEmpty()) {
      return TestContext.builder().build();
    } else if (methods.size() == 1) {
      return Methods.invoke(methods.get(0), TestContext.class);
    } else {
      throw new IllegalStateException("Discovered more than two methods with TestContext Annotation"
          + Methods.methodsToString.apply(methods));
    }
  }
}
