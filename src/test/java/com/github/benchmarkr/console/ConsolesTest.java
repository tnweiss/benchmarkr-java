package com.github.benchmarkr.console;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConsolesTest {
  @Test
  public void testFrom() {
    Consoles.from(Consoles.LOG).print("Hello World");
    Consoles.from(Consoles.SYSTEM).print("Hello World");
    Consoles.from(Consoles.SILENT).print("Hello World");
    Assertions.assertThrows(IllegalArgumentException.class, () -> Consoles.from("doesNotExist"));
  }
}
