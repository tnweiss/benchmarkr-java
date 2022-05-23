package com.github.benchmarkr.console.datatable.tables;

import com.github.benchmarkr.console.datatable.DataTable;
import com.github.benchmarkr.console.datatable.annotations.Column;
import com.github.benchmarkr.console.datatable.annotations.ColumnName;
import com.github.benchmarkr.console.datatable.annotations.ColumnSize;
import com.github.benchmarkr.console.datatable.annotations.PackageName;
import com.github.benchmarkr.console.datatable.annotations.Success;
import com.github.benchmarkr.dto.Result;

/**
 * Prints result data in the console.
 */
@SuppressWarnings("unused")
public class ResultDataTable extends DataTable<Result> {
  @Column
  @PackageName
  @ColumnSize(.4)
  @ColumnName("Test Name")
  public static String getTestName;

  @Column
  @ColumnSize(.2)
  @ColumnName("Lower Bound")
  public static String getLowerBound;

  @Column
  @ColumnSize(.2)
  @ColumnName("Upper Bound")
  public static String getUpperBound;

  @Column
  @Success(lessThanMethod = "getUpperBound")
  @ColumnSize(.2)
  @ColumnName("Duration")
  public static String getDuration;
}
