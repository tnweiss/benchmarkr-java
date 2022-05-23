package com.github.benchmarkr.util;

/**
 * Static methods pertaining to directories.
 */
public class Directories {
  private static final String WINDOWS_BM_DIR = "C:\\ProgramData\\Benchmarkr";
  private static final String LINUX_BM_DIR = "/etc/benchmarkr";

  /**
   * Get the benchmark dir for the given OS.
   *
   * @return the benchmark dir for the os
   */
  public static String benchmarkrDir() {
    if (System.getProperty("os.name").contains("Windows")) {
      return WINDOWS_BM_DIR;
    }
    return LINUX_BM_DIR;
  }
}
