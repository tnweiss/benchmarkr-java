package benchmarkr.integration;

import benchmarkr.annotation.Benchmark;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TestBBenchmark {
  @Benchmark
  public void testB() {
    log.info("Hello from B");
    System.out.println("Hellooo B");
  }
}
