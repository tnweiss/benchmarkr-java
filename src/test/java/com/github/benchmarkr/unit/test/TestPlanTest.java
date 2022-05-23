package com.github.benchmarkr.unit.test;

import com.github.benchmarkr.dto.Results;
import com.github.benchmarkr.validscenario.SetupTeardown;
import com.github.benchmarkr.test.plan.TestPlan;
import com.github.benchmarkr.test.plan.TestPlanParameters;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPlanTest {
  @Test
  public void testConstruct() {
    SetupTeardown.reset();
    TestPlan.construct(TestPlanParameters.builder()
        .packageName("com.github.benchmarkr.validscenario")
        .build());

    assertFalse(SetupTeardown.afterAll);
    assertFalse(SetupTeardown.beforeAll);
  }

  @Test
  public void testExecute() {
    SetupTeardown.reset();

    Results results = TestPlan.construct(TestPlanParameters.builder()
            .packageName("com.github.benchmarkr.validscenario")
            .build())
        .execute();

    assertFalse(results.getResults().isEmpty());
    assertTrue(SetupTeardown.afterAll);
    assertTrue(SetupTeardown.beforeAll);
  }
}
