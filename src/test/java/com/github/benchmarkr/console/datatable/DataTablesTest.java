package com.github.benchmarkr.console.datatable;

import java.util.List;
import java.util.Map;

import com.github.benchmarkr.console.Consoles;
import com.google.common.collect.ImmutableMap;

import org.junit.jupiter.api.Test;

public class DataTablesTest {
  @Test
  public void testPrintDataTable() {
    Map<String, String> data = ImmutableMap.<String, String>builder()
        .put("Row 1", "Value 1")
        .put("Row 2", "Value 2")
        .put("Row 3", "Value 3")
        .build();
    DataTables.print(Consoles.silent(), "Test", data);
  }

  @Test
  public void testPrintDataTableList() {
    List<Map<String, String>> data = List.of(
        ImmutableMap.<String, String>builder()
          .put("Row 1", "Value 1")
          .put("Row 2", "Value 2")
          .put("Row 3", "Value 3")
          .build(),
        ImmutableMap.<String, String>builder()
            .put("Row 1", "Value 1")
            .put("Row 2", "Value 2")
            .put("Row 3", "Value 3")
            .build()
    );
    DataTables.print(Consoles.silent(), "Test", data);
  }
}
