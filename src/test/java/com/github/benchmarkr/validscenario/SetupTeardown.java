package com.github.benchmarkr.validscenario;

import com.github.benchmarkr.annotation.AfterAll;
import com.github.benchmarkr.annotation.BeforeAll;
import com.github.benchmarkr.annotation.TestContext;

public class SetupTeardown {
  public static boolean beforeAll = false;
  public static boolean afterAll = false;

  public static void reset() {
    beforeAll = false;
    afterAll = false;
  }

  @BeforeAll
  public static void beforeAll() {
    beforeAll = true;
  }

  @AfterAll
  public static void afterAll() {
    afterAll = true;
  }

  @TestContext
  public static com.github.benchmarkr.dto.TestContext testContext() {
    return com.github.benchmarkr.dto.TestContext
        .builder()
        .softwareVersion("1.2.3-SNAPSHOT")
        .build();
  }
}
