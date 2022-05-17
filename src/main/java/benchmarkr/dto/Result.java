package benchmarkr;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

/**
 * Result class.
 */
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Result {
  @JsonIgnore
  private static final ObjectWriter objectWriter = new ObjectMapper().writer();

  @JsonProperty("Timestamp")
  private final Long timestamp;

  @JsonProperty("TestID")
  private final String testId;

  @JsonProperty("TestName")
  private final String testName;

  @JsonProperty("CustomProperties")
  private final Map<String, String> customProperties;

  @JsonProperty("LowerBound")
  private final Long lowerBound;

  @JsonProperty("UpperBound")
  private final Long upperBound;

  @JsonProperty("PerformanceDelta")
  private final Double performanceDelta;

  @JsonProperty("Duration")
  private final Long duration;

  @JsonProperty("Success")
  private final boolean success;

  @JsonProperty("SignificantSuccess")
  private final boolean significantSuccess;

  @Override
  public String toString() {
    try {
      return objectWriter.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
