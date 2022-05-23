package com.github.benchmarkr.validscenario;

import java.util.concurrent.ThreadLocalRandom;

import com.github.benchmarkr.annotation.Benchmark;
import com.github.benchmarkr.annotation.CustomProperty;
import com.github.benchmarkr.annotation.Description;
import com.github.benchmarkr.annotation.LowerBound;
import com.github.benchmarkr.annotation.TestName;
import com.github.benchmarkr.annotation.UpperBound;

public class UsersBenchmark {
  @LowerBound(40)
  @UpperBound(250)
  @CustomProperty(key = "category", value = "user")
  @Benchmark
  public void addUser() throws InterruptedException {
    Thread.sleep(ThreadLocalRandom.current().nextInt(40, 250));
  }

  @UpperBound(250)
  @CustomProperty(key = "category", value = "user")
  @CustomProperty(key = "cache", value = "true")
  @TestName("GetUser")
  @Description("Test retrieving a user from the database")
  @Benchmark
  public void getUser() throws InterruptedException {
    Thread.sleep(ThreadLocalRandom.current().nextInt(0, 300));
  }

  @Benchmark
  public void updateUser() throws InterruptedException {
    Thread.sleep(ThreadLocalRandom.current().nextInt(0, 300));
  }

  @Description("Delete a user from the database")
  @LowerBound(10)
  @UpperBound(15)
  @Benchmark
  public void deleteUser() throws InterruptedException {
    Thread.sleep(ThreadLocalRandom.current().nextInt(0, 20));
  }
}
