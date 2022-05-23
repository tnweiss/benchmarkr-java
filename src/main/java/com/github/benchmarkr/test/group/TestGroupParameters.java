package com.github.benchmarkr.test.group;

import com.github.benchmarkr.console.Console;
import com.github.benchmarkr.console.Consoles;
import com.github.benchmarkr.test.plan.TestPlanParameters;

import lombok.Builder;
import lombok.Getter;

/**
 * Parameters for a test group.
 */
@Builder
@Getter
public class TestGroupParameters {
  @Builder.Default
  private final Console console = Consoles.system();

  @Builder.Default
  private final Integer iterations = 1;

  /**
   * Construct TestGroupParameters from TestPlanParameters.
   *
   * @param testPlanParameters test plan parameters
   * @return TestGroupParameters
   */
  public static TestGroupParameters from(TestPlanParameters testPlanParameters) {
    return TestGroupParameters.builder()
        .console(testPlanParameters.getConsole())
        .iterations(testPlanParameters.getIterations())
        .build();
  }
}
