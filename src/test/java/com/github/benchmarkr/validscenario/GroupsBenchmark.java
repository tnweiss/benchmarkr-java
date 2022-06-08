package com.github.benchmarkr.validscenario;

import java.util.concurrent.ThreadLocalRandom;

import com.github.benchmarkr.annotation.AfterClass;
import com.github.benchmarkr.annotation.BeforeClass;
import com.github.benchmarkr.annotation.Benchmark;
import com.github.benchmarkr.annotation.CustomProperty;
import com.github.benchmarkr.annotation.Description;
import com.github.benchmarkr.annotation.LowerBound;
import com.github.benchmarkr.annotation.TestName;
import com.github.benchmarkr.annotation.UpperBound;

public class GroupsBenchmark {
  private static String var;

  @BeforeClass
  public static void setUpClass() {
    var = "Hello World";
  }

  @AfterClass
  public static void tearDownClass() {
    var = "";
  }

  @LowerBound(40)
  @UpperBound(60)
  @Description("Add a group to the database")
  @Benchmark
  public void addGroup() throws InterruptedException {
    if (!var.equals("Hello World")) {
      throw new IllegalStateException("Class not properly setup");
    }
    Thread.sleep(ThreadLocalRandom.current().nextInt(20, 40));
  }
// -cp jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.base;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.compiler;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.datatransfer;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.desktop;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.instrument;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.logging;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.management;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.management.rmi;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.naming;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.net.http;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.prefs;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.rmi;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.scripting;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.se;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.security.jgss;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.security.sasl;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.smartcardio;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.sql;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.sql.rowset;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.transaction.xa;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.xml;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/java.xml.crypto;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.accessibility;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.aot;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.attach;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.charsets;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.compiler;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.crypto.cryptoki;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.crypto.ec;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.crypto.mscapi;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.dynalink;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.editpad;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.hotspot.agent;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.httpserver;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.internal.ed;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.internal.jvmstat;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.internal.le;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.internal.opt;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.internal.vm.ci;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.internal.vm.compiler;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.internal.vm.compiler.management;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.jartool;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.javadoc;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.jcmd;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.jconsole;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.jdeps;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.jdi;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.jdwp.agent;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.jfr;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.jlink;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.jshell;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.jsobject;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.jstatd;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.localedata;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.management;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.management.agent;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.management.jfr;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.naming.dns;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.naming.ldap;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.naming.rmi;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.net;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.pack;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.rmic;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.scripting.nashorn;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.scripting.nashorn.shell;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.sctp;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.security.auth;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.security.jgss;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.unsupported;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.unsupported.desktop;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.xml.dom;jrt://C:/Users/tnwei/.jdks/corretto-11.0.14!/jdk.zipfs;file://D:/workspace/Benchmarkr/benchmarkr-java/target/test-classes;file://D:/workspace/Benchmarkr/benchmarkr-java/target/classes;jar://C:/Users/tnwei/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.13.3/jackson-databind-2.13.3.jar!/;jar://C:/Users/tnwei/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.13.3/jackson-annotations-2.13.3.jar!/;jar://C:/Users/tnwei/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.13.3/jackson-core-2.13.3.jar!/;jar://C:/Users/tnwei/.m2/repository/org/projectlombok/lombok/1.18.24/lombok-1.18.24.jar!/;jar://C:/Users/tnwei/.m2/repository/org/apache/logging/log4j/log4j-core/2.17.2/log4j-core-2.17.2.jar!/;jar://C:/Users/tnwei/.m2/repository/org/apache/logging/log4j/log4j-api/2.17.2/log4j-api-2.17.2.jar!/;jar://C:/Users/tnwei/.m2/repository/org/apache/logging/log4j/log4j-core/2.17.2/log4j-core-2.17.2-tests.jar!/;jar://C:/Users/tnwei/.m2/repository/org/junit/jupiter/junit-jupiter-engine/5.8.2/junit-jupiter-engine-5.8.2.jar!/;jar://C:/Users/tnwei/.m2/repository/org/junit/platform/junit-platform-engine/1.8.2/junit-platform-engine-1.8.2.jar!/;jar://C:/Users/tnwei/.m2/repository/org/opentest4j/opentest4j/1.2.0/opentest4j-1.2.0.jar!/;jar://C:/Users/tnwei/.m2/repository/org/junit/platform/junit-platform-commons/1.8.2/junit-platform-commons-1.8.2.jar!/;jar://C:/Users/tnwei/.m2/repository/org/junit/jupiter/junit-jupiter-api/5.8.2/junit-jupiter-api-5.8.2.jar!/;jar://C:/Users/tnwei/.m2/repository/org/apiguardian/apiguardian-api/1.1.2/apiguardian-api-1.1.2.jar!/;jar://C:/Users/tnwei/.m2/repository/com/google/guava/guava/31.1-jre/guava-31.1-jre.jar!/;jar://C:/Users/tnwei/.m2/repository/com/google/guava/failureaccess/1.0.1/failureaccess-1.0.1.jar!/;jar://C:/Users/tnwei/.m2/repository/com/google/guava/listenablefuture/9999.0-empty-to-avoid-conflict-with-guava/listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar!/;jar://C:/Users/tnwei/.m2/repository/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar!/;jar://C:/Users/tnwei/.m2/repository/org/checkerframework/checker-qual/3.12.0/checker-qual-3.12.0.jar!/;jar://C:/Users/tnwei/.m2/repository/com/google/errorprone/error_prone_annotations/2.11.0/error_prone_annotations-2.11.0.jar!/;jar://C:/Users/tnwei/.m2/repository/com/google/j2objc/j2objc-annotations/1.3/j2objc-annotations-1.3.jar!/;jar://C:/Users/tnwei/.m2/repository/org/apache/maven/maven-model/3.8.5/maven-model-3.8.5.jar!/;jar://C:/Users/tnwei/.m2/repository/org/codehaus/plexus/plexus-utils/3.3.0/plexus-utils-3.3.0.jar!/;jar://C:/Users/tnwei/.m2/repository/info/picocli/picocli/4.6.3/picocli-4.6.3.jar!/;jar://C:/Users/tnwei/.m2/repository/org/jline/jline/3.21.0/jline-3.21.0.jar!/
  @LowerBound(40)
  @UpperBound(250)
  @CustomProperty(key = "category", value = "Group")
  @CustomProperty(key = "cache", value = "false")
  @TestName("Update Group")
  @Benchmark
  public void updateGroup() throws InterruptedException {
    Thread.sleep(ThreadLocalRandom.current().nextInt(0, 50));
    throw new IllegalStateException("Unable to connect to the database");
  }
}
