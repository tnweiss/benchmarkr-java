package benchmarkr;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.common.reflect.ClassPath;

import lombok.extern.log4j.Log4j2;

/**
 * provides static utility methods for classes.
 */
@Log4j2
public class Classes {
  private static final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

  /**
   * Discover all classes in the classpath with the provided package regex.
   *
   * @param packageName package name regex, formatted as "benchmarkr.".
   * @return return a stream of class infos.
   */
  public static Stream<ClassPath.ClassInfo> inPackage(String packageName) {
    try {
      return ClassPath.from(classLoader)
          .getAllClasses()
          .stream()
          .filter(c -> c.getName().startsWith(packageName));
    } catch (Exception ex) {
      log.error("Unable to locate classes in {}", packageName, ex);
      throw new IllegalStateException(ex);
    }
  }

  /**
   * Filters ClassInfo based on the provided regex.
   *
   * @param regex the regular expression.
   * @return a Predicate used to filter class info.
   */
  public static Predicate<ClassPath.ClassInfo> matchingSimpleName(String regex) {
    return c -> c.getSimpleName().matches(regex);
  }

  /**
   * return a pair with the key being a class and the value being a list of methods.
   *
   * @param classInfo the information about a class
   * @return a pair with class info tied to a list of methods
   */
  public static ImmutablePair<Class<?>, List<Method>> withMethods(ClassPath.ClassInfo classInfo) {
    Class<?> clazz = classInfo.load();
    return new ImmutablePair<>(clazz, Arrays.asList(clazz.getDeclaredMethods()));
  }

  /**
   * Returns a predicate that looks for classes containing methods with a particular annotation.
   *
   * @param annotation the annotation to search for.
   * @param <T> the type of annotation.
   * @return a predicate to test for matches.
   */
  public static <T extends Annotation> Predicate<ClassPath.ClassInfo> withMethodsHavingAnnotation(Class<T> annotation) {
    return c -> Arrays.stream(c.load().getDeclaredMethods()).anyMatch(a -> a.getAnnotation(annotation) != null);
  }

  public static <T extends Annotation> Function<ClassPath.ClassInfo, Stream<Method>> methodsHavingAnnotation(Class<T> annotation) {
    return c -> Arrays.stream(c.load().getDeclaredMethods()).filter(a -> a.getAnnotation(annotation) != null);
  }

  /**
   * Instantiates the class in a Class / Methods immutable pair.
   *
   * @param clazz the class
   * @return an immutable pair with the key being an object and the value being its declared methods.
   */
  public static ImmutablePair<?, List<Method>> instantiate(ImmutablePair<Class<?>, List<Method>> clazz) {
    try {
      return new ImmutablePair<>(clazz.getKey().getDeclaredConstructor().newInstance(), clazz.getValue());
    } catch (NoSuchMethodException ex) {
      log.error("Unable to instantiate {} default constructor not found", clazz.getKey().getName(), ex);
      throw new IllegalStateException(ex);
    } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
      log.error("Unable to instantiate {} constructor must be public", clazz.getKey().getName(), ex);
      throw new IllegalStateException(ex);
    }
  }

  /**
   * Instantiates the class in a Class / Methods immutable pair.
   *
   * @param classInfo the class
   * @return an immutable pair with the key being an object and the value being its declared methods.
   */
  public static Object instantiate(ClassPath.ClassInfo classInfo) {
    try {
      return classInfo.load().getDeclaredConstructor().newInstance();
    } catch (NoSuchMethodException ex) {
      log.error("Unable to instantiate {} default constructor not found", classInfo.getName(), ex);
      throw new IllegalStateException(ex);
    } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
      log.error("Unable to instantiate {} constructor must be public", classInfo.getName(), ex);
      throw new IllegalStateException(ex);
    }
  }
}
