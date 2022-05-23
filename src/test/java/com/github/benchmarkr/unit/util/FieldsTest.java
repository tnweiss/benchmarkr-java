package com.github.benchmarkr.unit.util;

import java.util.Arrays;

import com.github.benchmarkr.console.datatable.annotations.Column;
import com.github.benchmarkr.console.datatable.annotations.ColumnName;
import com.github.benchmarkr.util.Fields;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FieldsTest {
  @Column
  @ColumnName("Column A")
  public int columnA;

  public int columnB;

  @Test
  public void testHavingAnnotation() {
    long count = Arrays.stream(this.getClass().getFields()).filter(Fields.havingAnnotation(Column.class)).count();
    Assertions.assertEquals(count, 1);
  }

  @Test
  public void testColumnName() throws Exception {
    String colA = Fields.columnName(this.getClass().getField("columnA"));
    Assertions.assertEquals(colA, "Column A");

    String colB = Fields.columnName(this.getClass().getField("columnB"));
    Assertions.assertEquals(colB, "columnB");
  }
}
