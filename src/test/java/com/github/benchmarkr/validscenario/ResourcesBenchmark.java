package com.github.benchmarkr.validscenario;

import java.util.concurrent.ThreadLocalRandom;

import com.github.benchmarkr.annotation.After;
import com.github.benchmarkr.annotation.Before;
import com.github.benchmarkr.annotation.Benchmark;
import com.github.benchmarkr.annotation.Description;
import com.github.benchmarkr.annotation.LowerBound;
import com.github.benchmarkr.annotation.TestName;
import com.github.benchmarkr.annotation.UpperBound;

public class ResourcesBenchmark {
  private String var;

  @Before
  public void setUp() {
    var = "Hello World";
  }

  @After
  public void tearDown() {
    var = "";
  }


  @UpperBound(100)
  @Description("Delete a resource")
  @Benchmark
  public void deleteResource() throws InterruptedException {
    if (!var.equals("Hello World")) {
      throw new IllegalStateException("class not properly set up");
    }
    Thread.sleep(ThreadLocalRandom.current().nextInt(70, 105));
  }

  @LowerBound(10)
  @UpperBound(20)
  @TestName("GetResource")
  @Benchmark
  public void getResource() throws InterruptedException {
    Thread.sleep(ThreadLocalRandom.current().nextInt(20, 40));
  }
}
