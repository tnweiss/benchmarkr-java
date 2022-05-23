package com.github.benchmarkr.unit.util;

import com.github.benchmarkr.util.Poms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PomsTest {
  @Test
  public void testPomVersion() {
    Assertions.assertFalse(Poms.pomVersion().isBlank());

    Assertions.assertThrows(IllegalStateException.class, () -> Poms.pomVersion("doesNotExist"));
    Assertions.assertThrows(IllegalStateException.class, () -> Poms.pomVersion("README.md"));
  }
}
