package com.github.benchmarkr.dto;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.benchmarkr.console.Console;
import com.github.benchmarkr.console.Consoles;
import com.github.benchmarkr.console.datatable.DataTables;
import com.github.benchmarkr.util.Directories;
import com.google.common.collect.ImmutableMap;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Collection of test results.
 */
@Builder
@Log4j2
public class Results {
  @JsonIgnore
  private static final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

  @Getter
  @JsonProperty("ID")
  private final String id = UUID.randomUUID().toString().toUpperCase(Locale.ROOT);

  @Getter
  @JsonProperty("LocalTestContext")
  private final TestContext testContext;

  @Getter
  @JsonProperty("Results")
  private final List<Result> results;

  @Override
  public String toString() {
    try {
      return objectWriter.writeValueAsString(this);
    } catch (JsonProcessingException ex) {
      log.error("Unable to parse Result set with id '{}' as json", id, ex);
      throw new IllegalStateException("Unable to parse result set to json", ex);
    }
  }

  /**
   * Write the results to a file.
   */
  public void write() {
    String filename = String.format("%s.%s.json",
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuuMMddhhmmssSSS")),
        this.id);
    log.debug("Writing to file {}", filename);

    try {
      // ensure the results folder for the system exists
      Path resultsPath = Paths.get(Directories.benchmarkrDir(), "results");
      if (!Files.exists(resultsPath)) {
        Files.createDirectories(resultsPath);
      }

      // construct path to the json
      String filepath = Paths.get(resultsPath.toString(), filename).toAbsolutePath().toString();
      log.debug("Writing to path {}", filepath);

      BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
      writer.write(this.toString());
      writer.close();
    } catch (IOException ex) {
      log.error("Error writing results", ex);
      throw new IllegalStateException("Error writing results", ex);
    }
  }

  /**
   * Print failed tests and their reasons for failure.
   *
   * @param console consumer used to print text
   */
  public void printFailedTests(Console console) {
    List<Map<String, String>> data = results.stream()
        .filter(r -> r.getSuccess() != null && !r.getSuccess())
        .map(r -> ImmutableMap.<String, String>builder()
            .put("Test", Objects.toString(r.getTestName(), ""))
            .put("Description", Objects.toString(r.getDescription(), ""))
            .put("Reason", Objects.toString(r.getOutcome(), ""))
            .build()
        )
        .collect(Collectors.toList());

    if (data.isEmpty()) {
      return;
    }

    console.print(Consoles.ANSI_RED);
    DataTables.print(console, "Failures", data);
    console.print(Consoles.ANSI_RESET);
  }
}
