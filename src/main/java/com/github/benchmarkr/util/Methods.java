package com.github.benchmarkr.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;

/**
 * Static utility methods for Methods.
 */
@Log4j2
public class Methods {
  public static final Function<List<Method>, String> methodsToString = methods ->
      methods.stream().map(Method::getName).collect(Collectors.joining(", "));
  private static final BiFunction<List<Method>, Class<?>, String> error = (methods, annotation) ->
      String.format("More than 1 Registered methods (%s) with annotation '%s'",
          annotation.getName(), methodsToString.apply(methods));

  /**
   * Invoke the provided method on the provided object.
   *
   * @param object the object to invoke the method on.
   * @param method the method to invoke.
   */
  public static void invoke(Object object, Method method) {
    try {
      method.invoke(object);
    } catch (Exception ex) {
      log.error("Unable to invoke {} in class {}", method.getName(), object.getClass().getName(), ex);
      throw new IllegalStateException("Unable to invoke " + method.getName(), ex);
    }
  }

  /**
   * Invoke the provided static method.
   *
   * @param method the method to invoke.
   */
  public static void invoke(Method method) {
    try {
      method.invoke(null);
    } catch (Exception ex) {
      log.error("Unable to invoke static method '{}' in '{}'", method.getName(),
          method.getDeclaringClass().getName(), ex);
      throw new IllegalStateException("Unable to invoke static method " + method.getName(), ex);
    }
  }

  /**
   * Invoke a method and return the return value of the method.
   *
   * @param method the method to invoke
   * @param clazz  the return type
   * @param <R>    the type of return value
   * @return the return value of the method
   */
  @SuppressWarnings("unchecked")
  public static <R> R invoke(Method method, Class<R> clazz) {
    try {
      Object returnValue = method.invoke(null);
      if (returnValue.getClass().equals(clazz)) {
        return (R) returnValue;
      }

      throw new IllegalStateException("Return value type ( " + returnValue.getClass()
          + " ) from method ( " + method.getName() + " ) cannot be cast to ( " + clazz + " ) ");
    } catch (Exception ex) {
      log.error("Unable to invoke static method {}", method.getName(), ex);
      throw new IllegalStateException("Unable to invoke static method " + method.getName(), ex);
    }
  }

  /**
   * Invoke the method and return the value produced by the method.
   *
   * @param o      the object to invoke the method on
   * @param method the method to invoke
   * @param clazz  the return type
   * @param <T>    the type of object to return
   * @return the return value of the method
   */
  @SuppressWarnings("unchecked")
  public static <T> T invoke(Object o, String method, Class<T> clazz) {
    try {
      Object returnValue = o.getClass().getMethod(method).invoke(o);

      if (returnValue == null) {
        return null;
      }

      if (clazz.isAssignableFrom(returnValue.getClass())) {
        return (T) returnValue;
      }

      throw new IllegalStateException("Return value type ( " + returnValue.getClass()
          + " ) from method ( " + method + " ) cannot be cast to ( " + clazz + " ) ");
    } catch (Exception ex) {
      log.error("Unable to invoke {} in class {}", method, o.getClass().getName(), ex);
      throw new IllegalStateException("Unable to invoke " + method, ex);
    }
  }

  /**
   * Get a predicate that selects methods with the provided annotation.
   *
   * @param annotation the annotation provided methods should have
   * @param <T>        the type of annotation
   * @return a predicate that filters methods with the provided annotation.
   */
  public static <T extends Annotation> Predicate<Method> annotatedWith(Class<T> annotation) {
    return m -> m.getAnnotation(annotation) != null;
  }

  /**
   * Get 1 or 0 Runnables from methods in classes with the provided annotation.
   *
   * @param classes    the classes that may contain an annotated method.
   * @param annotation the provided annotation of interest
   * @param <T>        the type of annotation
   * @return an optional Runnable
   */
  public static <T extends Annotation> Optional<Runnable> runnableAnnotatedWith(
      List<Class<?>> classes, Class<T> annotation) {

    // Collect all registered before all static methods
    List<Method> methodsWithAnnotation = classes.stream()
        .filter(Classes.withMethodsHavingAnnotation(annotation))
        .flatMap(Classes.methodsHavingAnnotation(annotation))
        .collect(Collectors.toList());

    if (methodsWithAnnotation.size() > 1) {
      // if more than 1 result is returned, something went wrong
      throw new IllegalStateException(error.apply(methodsWithAnnotation, annotation));
    } else if (methodsWithAnnotation.size() == 0) {
      // if none present, return empty
      return Optional.empty();
    } else {
      // if 1 present, return supplier
      return Optional.of(() -> Methods.invoke(methodsWithAnnotation.get(0)));
    }
  }

  /**
   * Get 1 or 0 Runnables from methods in classes with the provided annotation.
   *
   * @param classes    the classes that may contain an annotated method.
   * @param annotation the provided annotation of interest
   * @param <T>        the type of annotation
   * @return an optional Runnable
   */
  public static <T extends Annotation, R> Optional<Supplier<R>> runnableAnnotatedWith(
      List<Class<?>> classes, Class<T> annotation,
      Class<R> retVal) {

    // Collect all registered before all static methods
    List<Method> methodsWithAnnotation = classes.stream()
        .filter(Classes.withMethodsHavingAnnotation(annotation))
        .flatMap(Classes.methodsHavingAnnotation(annotation))
        .collect(Collectors.toList());

    if (methodsWithAnnotation.size() > 1) {
      // if more than 1 result is returned, something went wrong
      throw new IllegalStateException(error.apply(methodsWithAnnotation, annotation));
    } else if (methodsWithAnnotation.size() == 0) {
      // if none present, return empty
      return Optional.empty();
    } else {
      // if 1 present, return supplier
      return Optional.of(() -> Methods.invoke(methodsWithAnnotation.get(0), retVal));
    }
  }

}
