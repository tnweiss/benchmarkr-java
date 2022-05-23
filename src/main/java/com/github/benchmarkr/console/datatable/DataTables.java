package com.github.benchmarkr.console.datatable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.benchmarkr.console.Console;
import com.github.benchmarkr.console.Consoles;
import com.github.benchmarkr.console.datatable.tables.ResultDataTable;

import org.codehaus.plexus.util.StringUtils;

/**
 * Class with static utility methods for data tables.
 */
public class DataTables {
  public static final ResultDataTable RESULT_DATA_TABLE = new ResultDataTable();

  /**
   * Using the provided console, print the header + key / value pairs.
   *
   * @param console the console used ot print the text
   * @param header  the data table header
   * @param data    the data to print
   */
  public static void print(Console console, String header, Map<String, String> data) {
    String border = StringUtils.repeat("#", Consoles.CONSOLE_WIDTH);
    String headerSpacing = StringUtils.repeat(" ", (Consoles.CONSOLE_WIDTH / 2) - (header.length() / 2));

    console.print("\n\n" + border + "\n");
    console.print(headerSpacing + header + "\n");
    console.print(border + "\n");

    int keySpacing = data.keySet()
        .stream()
        .map(String::length)
        .max(Integer::compareTo)
        .orElse(0);

    String content = data.entrySet().stream()
        .map(e -> e.getKey()
            + StringUtils.repeat(" ", keySpacing - e.getKey().length())
            + " : "
            + e.getValue()
        )
        .collect(Collectors.joining("\n"));
    console.print(content);
    console.print("\n");
  }

  /**
   * Using the provided console, print the header + list of key / value pairs.
   *
   * @param console console used to print text
   * @param header  header text for the data
   * @param data    data to print
   */
  public static void print(Console console, String header, List<Map<String, String>> data) {
    String border = StringUtils.repeat("#", Consoles.CONSOLE_WIDTH);
    String headerSpacing = StringUtils.repeat(" ", (Consoles.CONSOLE_WIDTH / 2) - (header.length() / 2));

    console.print("\n\n" + border + "\n");
    console.print(headerSpacing + header + "\n");
    console.print(border + "\n");

    int keySpacing = data.stream()
        .flatMap(m -> m.keySet().stream())
        .map(String::length)
        .max(Integer::compareTo)
        .orElse(0);

    String content = data.stream()
        .map(d -> d.entrySet().stream()
            .map(e -> e.getKey()
                + StringUtils.repeat(" ", keySpacing - e.getKey().length())
                + " : "
                + e.getValue()
            )
            .collect(Collectors.joining("\n"))
        ).collect(Collectors.joining("\n\n"));
    console.print(content + "\n\n");
  }
}
