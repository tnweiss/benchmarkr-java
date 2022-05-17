package benchmarkr.integration;

import benchmarkr.annotation.Benchmark;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TestABenchmark {
  @Benchmark
  public void testA() {
    log.info("Hello from A");
    System.out.println("Hellllooo");
  }
}
