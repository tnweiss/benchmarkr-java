package com.github.benchmarkr.console.datatable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.benchmarkr.console.Console;
import com.github.benchmarkr.console.Consoles;
import com.github.benchmarkr.console.datatable.annotations.Column;
import com.github.benchmarkr.console.datatable.annotations.ColumnSize;
import com.github.benchmarkr.console.datatable.annotations.PackageName;
import com.github.benchmarkr.console.datatable.annotations.Success;
import com.github.benchmarkr.dto.ImmutablePair;
import com.github.benchmarkr.util.Fields;
import com.github.benchmarkr.util.Methods;

import lombok.extern.log4j.Log4j2;
import org.codehaus.plexus.util.StringUtils;

/**
 * Abstract class used to display objects in the console.
 *
 * @param <T> the type of object
 */
@Log4j2
public abstract class DataTable<T> {
  // the amount of space each row consumes
  private final int[] columnDistribution;
  // the name of each column
  private final String[] columnNames;
  // the methods used to extract values from the object
  private final String[] methodNames;
  // function used to modify output values
  private final Function<ImmutablePair<Object, String>, String>[] modifiers;

  /**
   * DataTable default constructor.
   */
  @SuppressWarnings("unchecked")
  public DataTable() {
    methodNames = fields()
        .map(Field::getName)
        .toArray(String[]::new);
    log.debug(() -> Arrays.toString(methodNames));

    columnNames = fields()
        .map(Fields::columnName)
        .toArray(String[]::new);
    log.debug(() -> Arrays.toString(columnNames));

    modifiers = fields()
        .map(DataTable::modifiers)
        .toArray(Function[]::new);

    // extract any annotated columns
    List<Double> columnSizes = fields()
        .map(f -> f.getAnnotation(ColumnSize.class) == null ? null : f.getAnnotation(ColumnSize.class).value())
        .collect(Collectors.toList());

    // get the amount of allocated proportion
    double allocatedDist = columnSizes.stream()
        .filter(Objects::nonNull)
        .reduce(0.0, Double::sum);

    if (allocatedDist > 1.0) {
      throw new IllegalStateException("Invalid column distribution, total proportions can not exceed 1.0");
    }

    // get the number of null columns
    long nullCollSizes = columnSizes.stream()
        .filter(Objects::isNull)
        .count();

    // calculate the proportion for any non annotated columns
    double tempRemainingProportions = 0.0;
    if (nullCollSizes > 0) {
      tempRemainingProportions = ((1.0 - allocatedDist) / Long.valueOf(nullCollSizes).doubleValue());
    }
    final double remainingProportions = tempRemainingProportions;

    // calculate the row size
    columnDistribution = columnSizes.stream()
        .mapToDouble(f -> f == null ? remainingProportions * Consoles.CONSOLE_WIDTH : f * Consoles.CONSOLE_WIDTH)
        .mapToInt(d -> Double.valueOf(d).intValue())
        .toArray();
    log.debug(() -> Arrays.toString(columnDistribution));
  }

  /**
   * Get the columns for this table.
   *
   * @return a stream of fields.
   */
  private Stream<Field> fields() {
    return Arrays.stream(getClass().getFields())
        .filter(Fields.havingAnnotation(Column.class));
  }

  /**
   * print the table header.
   *
   * @param console the consumer to send the data to.
   */
  public void printHeader(Console console) {
    List<String> borderBuilder = new LinkedList<>();
    List<String> labelBuilder = new LinkedList<>();

    for (int i = 0; i < columnNames.length; i++) {
      borderBuilder.add(StringUtils.repeat("#", columnDistribution[i] - 1));
      labelBuilder.add(columnNames[i] + StringUtils.repeat(" ", columnDistribution[i]
          - columnNames[i].length() - 1));
    }

    String border = String.join(" ", borderBuilder);
    String label = String.join(" ", labelBuilder);

    console.print(border + "\n");
    console.print(label + "\n");
    console.print(border + "\n");
  }

  /**
   * Print the object as a row.
   *
   * @param console the consumer to send data to
   * @param object  the object to print
   */
  public void print(Console console, T object) {
    List<String> columns = Arrays.stream(methodNames)
        .map(mn -> {
          try {
            Object retVal = object.getClass()
                .getMethod(mn)
                .invoke(object);
            return retVal == null ? "" : retVal.toString();
          } catch (Exception ex) {
            throw new IllegalStateException(ex);
          }
        })
        .collect(Collectors.toList());

    for (int i = 0; i < columnDistribution.length; i++) {
      String data = modifiers[i].apply(new ImmutablePair<>(object, columns.get(i)));
      data = data.length() > columnDistribution[i]
          ? data.substring(0, columnDistribution[i] - 1) : data;
      String padding = StringUtils.repeat(" ", Math.max(columnDistribution[i]
          - data.length() - 1, 0));

      columns.set(i, data + padding);
    }

    console.print(String.join(" ", columns) + "\n");
  }

  private static Function<ImmutablePair<Object, String>, String> modifiers(Field field) {
    return i -> success(field).apply(packageName(field).apply(i)).getValue();
  }

  private static Function<ImmutablePair<Object, String>, ImmutablePair<Object, String>> packageName(Field field) {
    if (field.getAnnotation(PackageName.class) == null) {
      return s -> s;
    } else {
      return s -> {
        if (!s.getValue().contains(".")) {
          return s;
        } else {
          String[] breakdown = s.getValue().split("\\.");

          return new ImmutablePair<>(s.getKey(), Arrays.stream(breakdown)
              .limit(breakdown.length - 1)
              .map(p -> p.substring(0, 1)).collect(Collectors.joining("."))
              + "." + breakdown[breakdown.length - 1]);
        }
      };
    }
  }

  private static Function<ImmutablePair<Object, String>, ImmutablePair<Object, String>> success(Field field) {
    Success annotation = field.getAnnotation(Success.class);

    if (annotation == null) {
      return s -> s;
    }

    List<Predicate<ImmutablePair<Object, String>>> tests = new LinkedList<>();

    if (annotation.greaterThan() != Double.MAX_VALUE) {
      tests.add(p ->
          !p.getValue().isEmpty()
              &&
              Double.parseDouble(p.getValue()) > annotation.greaterThan());
    } else if (annotation.lessThan() != Double.MIN_VALUE) {
      tests.add(p ->
          !p.getValue().isEmpty()
              &&
              Double.parseDouble(p.getValue()) < annotation.lessThan());
    } else if (!annotation.greaterThanMethod().isBlank()) {
      tests.add(s ->
          !s.getValue().isEmpty()
              &&
              invokeForDouble(s.getKey(), annotation.greaterThanMethod()).orElse(Double.MIN_VALUE)
                  < Double.parseDouble(s.getValue()));
    } else if (!annotation.lessThanMethod().isBlank()) {
      tests.add(s ->
          !s.getValue().isEmpty()
              &&
              invokeForDouble(s.getKey(), annotation.lessThanMethod()).orElse(Double.MAX_VALUE)
                  > Double.parseDouble(s.getValue()));
    } else {
      tests.add(s -> true);
    }

    Predicate<ImmutablePair<Object, String>> test = p -> tests.stream()
        .allMatch(t -> t.test(p));

    return p -> test.test(p)
        ? new ImmutablePair<>(p.getKey(), Consoles.ANSI_GREEN + p.getValue() + Consoles.ANSI_RESET) :
        new ImmutablePair<>(p.getKey(), Consoles.ANSI_RED + p.getValue() + Consoles.ANSI_RESET);
  }

  /**
   * Invoke the method and return a double.
   *
   * @param o      the object to invoke the method on
   * @param method the method to invoke
   * @return an optional double
   */
  public static Optional<Double> invokeForDouble(Object o, String method) {
    Object returnO = Methods.invoke(o, method, Object.class);

    if (returnO == null) {
      return Optional.empty();
    } else {
      return Optional.of(Double.parseDouble(returnO.toString()));
    }
  }
}
