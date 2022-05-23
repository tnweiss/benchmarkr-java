package com.github.benchmarkr;

import java.util.concurrent.Callable;

import com.github.benchmarkr.console.Consoles;
import com.github.benchmarkr.dto.ResultSummary;
import com.github.benchmarkr.dto.Results;
import com.github.benchmarkr.test.plan.TestPlan;
import com.github.benchmarkr.test.plan.TestPlanParameters;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Inception point of tests.
 */
@Log4j2
@Command(name = "benchmark", version = "0.0.1",
    description = "Runs benchmark tests")
@AllArgsConstructor
@NoArgsConstructor
public class BenchmarkrCore implements Callable<Integer> {
  public static final String PACKAGE_PROPERTY = "benchmarkr.config.packageName";
  public static final String PACKAGE_PROPERTY_DEFAULT = "";
  private static final String PACKAGE_PICO_CLI_DV = "${" + PACKAGE_PROPERTY + ":-" + PACKAGE_PROPERTY_DEFAULT + "}";

  public static final String CLASS_PROPERTY = "benchmarkr.config.class";
  public static final String CLASS_PROPERTY_DEFAULT = "";
  private static final String CLASS_PICO_CLI_DV = "${" + CLASS_PROPERTY + ":-" + CLASS_PROPERTY_DEFAULT + "}";

  public static final String METHOD_PROPERTY = "benchmarkr.config.method";
  public static final String METHOD_PROPERTY_DEFAULT = "";
  private static final String METHOD_PICO_CLI_DV = "${" + METHOD_PROPERTY + ":-" + METHOD_PROPERTY_DEFAULT + "}";

  public static final String ITERATIONS_PROPERTY = "benchmarkr.config.iterations";
  public static final String ITERATIONS_PROPERTY_DEFAULT = "1";
  private static final String ITERATIONS_PICO_CLI_DV = "${" + ITERATIONS_PROPERTY + ":-"
      + ITERATIONS_PROPERTY_DEFAULT + "}";

  public static final String CONSOLE_PROPERTY = "benchmarkr.config.console";
  public static final String CONSOLE_PROPERTY_DEFAULT = "System";
  private static final String CONSOLE_PICO_CLI_DV = "${" + CONSOLE_PROPERTY + ":-"
      + CONSOLE_PROPERTY_DEFAULT + "}";

  public static final String RECORD_PROPERTY = "benchmarkr.config.record";
  public static final String RECORD_PROPERTY_DEFAULT = "true";
  private static final String RECORD_PICO_CLI_DV = "${" + RECORD_PROPERTY + ":-"
      + RECORD_PROPERTY_DEFAULT + "}";

  public static final String IGNORE_FAILURES_PROPERTY = "benchmarkr.config.ignoreFailures";
  public static final String IGNORE_FAILURES_PROPERTY_DEFAULT = "false";
  private static final String IGNORE_FAILURES_PICO_CLI_DV = "${" + IGNORE_FAILURES_PROPERTY + ":-"
      + IGNORE_FAILURES_PROPERTY_DEFAULT + "}";

  @CommandLine.Option(names = {"-p", "--package-name"}, defaultValue = PACKAGE_PICO_CLI_DV,
      description = "Top level package where the tests reside eg 'com.example'.")
  private String packageName;

  @CommandLine.Option(names = {"-l", "--class"}, defaultValue = CLASS_PICO_CLI_DV,
      description = "A specific class to search for benchmarking tests in.")
  private String className;

  @CommandLine.Option(names = {"-b", "--benchmark"}, defaultValue = METHOD_PICO_CLI_DV,
      description = "The specific benchmarking test to run.")
  private String methodName;

  @CommandLine.Option(names = {"-i", "--iterations"}, defaultValue = ITERATIONS_PICO_CLI_DV,
      description = "The number of times to execute the selected tests.")
  private Integer iterations;

  @CommandLine.Option(names = {"-c", "--console"}, defaultValue = CONSOLE_PICO_CLI_DV,
      description = "The console type ('System', 'Silent')")
  private String console;

  @CommandLine.Option(names = {"-r", "--record"}, defaultValue = RECORD_PICO_CLI_DV,
      description = "Whether or not to record the result")
  private Boolean record;

  @CommandLine.Option(names = {"-f", "--ignore-failures"}, defaultValue = IGNORE_FAILURES_PICO_CLI_DV,
      description = "Stop execution on benchmarking failure")
  private Boolean fail;

  @Override
  public Integer call() {
    return main(TestPlanParameters.builder()
        .packageName(packageName)
        .className(className)
        .methodName(methodName)
        .console(Consoles.from(console))
        .iterations(iterations)
        .recordResults(record)
        .classLoader(this.getClass().getClassLoader())
        .ignoreFailure(fail)
        .build());
  }

  /**
   * Application entry point.
   *
   * @param testPlanParameters test parameters.
   * @return an exit code
   */
  public static int main(TestPlanParameters testPlanParameters) {
    // disable logging for the module
    LoggerContext context = (LoggerContext) LogManager.getContext(false);
    Configuration config = context.getConfiguration();
    LoggerConfig loggerConfig = config.getLoggerConfig("com.github.benchmarkr");
    loggerConfig.setLevel(Level.OFF);
    context.updateLoggers();

    // construct the test plan and execute
    Results results = TestPlan.construct(testPlanParameters).execute();

    // print any failures
    results.printFailedTests(testPlanParameters.getConsole());

    // generate result summary
    ResultSummary resultsSummary = ResultSummary.construct(results);

    // print the summary
    resultsSummary.printSummary(testPlanParameters.getConsole());

    // write to file if directed to
    if (testPlanParameters.isRecordResults()) {
      results.write();
    }

    return !testPlanParameters.isIgnoreFailure() || resultsSummary.getFailures() == 0 ? 0 : -1;
  }

  public static void main(String[] args) {
    System.exit(new CommandLine(new BenchmarkrCore()).execute(args));
  }
}
