package com.github.benchmarkr.unit.dto;

import java.util.List;
import java.util.Map;

import com.github.benchmarkr.dto.Result;
import com.github.benchmarkr.dto.Results;
import com.github.benchmarkr.util.Annotations;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResultsTest {
  @Test
  public void resultsToStringTest() throws Exception {
    Result result = Result.builder()
        .testName("Test")
        .testId("1234")
        .duration(10L)
        .lowerBound(10L)
        .upperBound(10L)
        .performanceDelta(.44)
        .timestamp(1000000L)
        .build();
    List<Result> resultList = List.of(result);

    Results results = Results.builder()
        .results(resultList)
        .testContext(Annotations.testContext(List.of()))
        .build();

    Map<String, Object> outResults = new ObjectMapper()
        .readValue(results.toString(), Map.class);

    assertEquals(outResults.get("ID"), results.getId());
  }
}
