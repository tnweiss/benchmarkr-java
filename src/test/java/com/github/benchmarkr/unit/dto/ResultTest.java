package com.github.benchmarkr.unit.dto;

import com.github.benchmarkr.dto.Result;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ResultTest {
  @Test
  public void testResultToString() {
    final String EXPECTED = "{\"Timestamp\":1000000,\"TestID\":\"1234\",\"TestName\":\"Test\",\"CustomProperties\":" +
        "null,\"LowerBound\":10,\"UpperBound\":10,\"PerformanceDelta\":0.44,\"Duration\":10,\"Success\":false," +
        "\"SignificantSuccess\":null}";

    final Result result = Result.builder()
        .testName("Test")
        .testId("1234")
        .duration(10L)
        .lowerBound(10L)
        .upperBound(10L)
        .performanceDelta(.44)
        .timestamp(1000000L)
        .success(false)
        .build();

    assertEquals(EXPECTED, result.toString(), "Result toString is broken");
  }

}
