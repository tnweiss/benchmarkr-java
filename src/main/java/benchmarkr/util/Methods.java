package benchmarkr;

import lombok.extern.log4j.Log4j2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class Methods {
  public static <T extends Annotation> Function<ImmutablePair<Class<?>, List<Method>>,
      ImmutablePair<Class<?>, List<Method>>> annotatedWith(Class<T> annotation) {
    return p -> new ImmutablePair<>(
        p.getKey(),
        p.getValue().stream().filter(m -> m.getAnnotation(annotation) != null).collect(Collectors.toList())
    );
  }

  public static void invokeEach(ImmutablePair<?, List<Method>> methods) {
    for (Method method : methods.getValue()) {
      try {
        method.invoke(methods.getKey());
      } catch (Exception ex) {
        log.error("Unable to invoke {} in class {}", method.getName(), methods.getKey().toString(), ex);
      }
    }
  }

  public static <T> Stream<Method> methodsInClass(Class<T> clazz) {
    return Arrays.stream(clazz.getDeclaredMethods());
  }
}
