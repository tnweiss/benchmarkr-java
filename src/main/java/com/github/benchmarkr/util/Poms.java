package com.github.benchmarkr.util;

import java.io.FileReader;
import java.io.IOException;

import lombok.extern.log4j.Log4j2;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 * Pom Utility class.
 */
@Log4j2
public class Poms {
  private static final String POM = "pom.xml";

  /**
   * Get the pom version of the current jar.
   *
   * @return a string representation of the pom.
   */
  public static String pomVersion(String pom) {
    MavenXpp3Reader reader = new MavenXpp3Reader();

    try {
      return reader.read(new FileReader(pom)).getVersion();
    } catch (IOException ex) {
      log.error("Unable to find {}", pom, ex);
      throw new IllegalStateException(ex);
    } catch (XmlPullParserException ex) {
      log.error("Unable to parse {}", pom, ex);
      throw new IllegalStateException(ex);
    }
  }

  /**
   * IF provided, return the pom version for the project.
   *
   * @return the project version
   */
  public static String pomVersion() {
    try {
      return pomVersion(POM);
    } catch (Exception ex) {
      return "NK";
    }
  }
}
