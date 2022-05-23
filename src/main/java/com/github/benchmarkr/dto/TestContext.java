package com.github.benchmarkr.dto;

import com.github.benchmarkr.util.Poms;

import lombok.Builder;
import lombok.Getter;

/**
 * Provides context to the test. Things like memory, cpu, etc is provided by the benchmarkr binary.
 */
@Getter
@Builder
public class TestContext {
  private final String benchmarkrVersion = Poms.pomVersion();
  private final String language = "java";
  private final Long timestamp = System.currentTimeMillis();
  private final String softwareVersion;

  public static TestContext defaultTestContext() {
    return TestContext.builder().build();
  }
}
