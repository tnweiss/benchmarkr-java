package com.github.benchmarkr;

import java.util.stream.Collectors;

import com.github.benchmarkr.util.Classes;
import com.google.common.reflect.ClassPath;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

public class BenchmarkrCoreTest {
  @Test
  public void testCallWithArgs() {
    String[] args = {"-p", "benchmarkr.integration.validscenario"};
    Assertions.assertEquals(new CommandLine(new BenchmarkrCore()).execute(args), 0);
  }

  @Test
  public void testCallWithProperties() {
    String[] args = {};
    System.setProperty("benchmarkr.config.packageName", "benchmarkr.integration.validscenario");
    Assertions.assertEquals(new CommandLine(new BenchmarkrCore()).execute(args), 0);
  }
}
