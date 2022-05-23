package com.github.benchmarkr.console;

import lombok.extern.log4j.Log4j2;

/**
 * Static console.
 */
@Log4j2
public class Consoles {
  public static final int CONSOLE_WIDTH = 120;
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";

  public static final String SYSTEM = "System";
  public static final String SILENT = "Silent";
  public static final String LOG = "Log";

  /**
   * Get a console from a string.
   *
   * @param consoleType console types string
   * @return a console function
   */
  public static Console from(String consoleType) {
    switch (consoleType) {
      case SYSTEM:
        return system();
      case SILENT:
        return silent();
      case LOG:
        return log();
      default:
        throw new IllegalArgumentException("Unknown consoleType '" + consoleType + "'");
    }
  }

  public static Console system() {
    return System.out::printf;
  }

  public static Console silent() {
    return txt -> {
    };
  }

  public static Console log() {
    return log::info;
  }

  public static String red(String text) {
    return ANSI_RED + text + ANSI_RESET;
  }

  public static String green(String text) {
    return ANSI_GREEN + text + ANSI_RESET;
  }
}
