package com.github.benchmarkr.test.test;

import com.github.benchmarkr.console.Console;
import com.github.benchmarkr.console.Consoles;
import com.github.benchmarkr.test.group.TestGroupParameters;

import lombok.Builder;
import lombok.Getter;

/**
 * Parameters used to construct a test.
 */
@Builder
@Getter
public class TestParameters {
  @Builder.Default
  private Console console = Consoles.system();

  /**
   * Construct TestParameters from TestGroupParameters.
   *
   * @param testGroupParameters test group parameters
   * @return an instance of test parameters
   */
  public static TestParameters from(TestGroupParameters testGroupParameters) {
    return TestParameters.builder()
        .console(testGroupParameters.getConsole())
        .build();
  }
}
