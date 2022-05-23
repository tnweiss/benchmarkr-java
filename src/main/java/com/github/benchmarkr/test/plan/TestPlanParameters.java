package com.github.benchmarkr.test.plan;

import com.github.benchmarkr.console.Console;
import com.github.benchmarkr.console.Consoles;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Parameters for test plan.
 */
@Builder
@Getter
@ToString
public class TestPlanParameters {
  // the number of times to run the tests
  @Builder.Default
  private Integer iterations = 1;

  // the console type
  @Builder.Default
  private Console console = Consoles.system();

  // parameters used to identify certain tests
  @Builder.Default
  private final String packageName = "";
  @Builder.Default
  private final String className = "";
  @Builder.Default
  private final String methodName = "";
  @Builder.Default
  private final ClassLoader classLoader = TestPlanParameters.class.getClassLoader();

  // if true, record results
  @Builder.Default
  private boolean recordResults = false;

  @Builder.Default
  private boolean ignoreFailure = false;
}
