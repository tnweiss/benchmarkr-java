package com.github.benchmarkr.validscenario;

import java.util.concurrent.ThreadLocalRandom;

import com.github.benchmarkr.annotation.AfterClass;
import com.github.benchmarkr.annotation.BeforeClass;
import com.github.benchmarkr.annotation.Benchmark;
import com.github.benchmarkr.annotation.CustomProperty;
import com.github.benchmarkr.annotation.Description;
import com.github.benchmarkr.annotation.LowerBound;
import com.github.benchmarkr.annotation.TestName;
import com.github.benchmarkr.annotation.UpperBound;

public class GroupsBenchmark {
  private static String var;

  @BeforeClass
  public static void setUpClass() {
    var = "Hello World";
  }

  @AfterClass
  public static void tearDownClass() {
    var = "";
  }

  @LowerBound(40)
  @UpperBound(60)
  @Description("Add a group to the database")
  @Benchmark
  public void addGroup() throws InterruptedException {
    if (!var.equals("Hello World")) {
      throw new IllegalStateException("Class not properly setup");
    }
    Thread.sleep(ThreadLocalRandom.current().nextInt(20, 40));
  }

  @LowerBound(40)
  @UpperBound(250)
  @CustomProperty(key = "category", value = "Group")
  @CustomProperty(key = "cache", value = "false")
  @TestName("Update Group")
  @Benchmark
  public void updateGroup() throws InterruptedException {
    Thread.sleep(ThreadLocalRandom.current().nextInt(0, 50));
    throw new IllegalStateException("Unable to connect to the database");
  }
}
