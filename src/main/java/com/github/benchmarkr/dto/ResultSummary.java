package com.github.benchmarkr.dto;

import java.util.Map;

import com.github.benchmarkr.console.Console;
import com.github.benchmarkr.console.Consoles;
import com.github.benchmarkr.console.datatable.DataTables;
import com.google.common.collect.ImmutableMap;

import lombok.Builder;
import lombok.Getter;

/**
 * Contains result summary.
 */
@Builder
@Getter
public class ResultSummary {
  private long total;
  private long successes;
  private long failures;
  private long significantSuccesses;

  /**
   * Construct a result summary from the provided results.
   *
   * @param results the test results
   * @return test summary
   */
  public static ResultSummary construct(Results results) {
    long successCount = results.getResults().stream()
        .filter(r -> r.getSuccess() != null && r.getSuccess()).count();
    long significantSuccessCount = results.getResults().stream()
        .filter(r -> r.getSignificantSuccess() != null && r.getSignificantSuccess()).count();
    long failedCount = results.getResults().stream()
        .filter(r -> r.getSuccess() != null && !r.getSuccess()).count();

    return ResultSummary.builder()
        .successes(successCount)
        .total(results.getResults().size())
        .failures(failedCount)
        .significantSuccesses(significantSuccessCount)
        .build();
  }

  /**
   * Print the summary of the test results.
   *
   * @param console the consumer used to print text
   */
  public void printSummary(Console console) {
    String resultText = failures > 0 ? Consoles.red("FAILED") : Consoles.green("SUCCESS");

    Map<String, String> data = ImmutableMap.<String, String>builder()
        .put("Successful Tests", successes + "/" + total)
        .put("Significantly Successful Tests", significantSuccesses + "/" + total)
        .put("Failed Tests", failures + "/" + total)
        .put("Result", resultText)
        .build();
    DataTables.print(console, "Test Summary", data);

    console.print("\n\n");
  }
}
