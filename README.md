![](images/BenchmarkrJava.svg)

## Overview

Set standards for your software.

This repository provides a benchmarking framework for Java. See [Benchmarkr CPP](https://github.com/tnweiss/Benchmarkr-cpp) 
to get the binaries used to set up ELK indices and upload test results. 

The goal of this project is to track **relative performance**, not absolute performance. No two runs, even on the same machine,
will yield the same results. This project attempts to highlight significant trends and major anomalies by allowing
developers to constantly benchmark their software.

The screenshot below shows the default dashboard that comes with this project. It provides basic information about
which tests have outperformed expectations and which have underperformed.

![](https://github.com/tnweiss/Benchmarkr-cpp/raw/master/images/BenchmarkrDashboard.PNG)

The line graph, in the screenshot above, becomes more useful when you filter on a single test. In the screenshot below
we filtered by `TestName : NormalizeDataTest` and this is what our line graph looked like...

![](https://github.com/tnweiss/Benchmarkr-cpp/raw/master/images/BenchmarkrDashboardLineGraph.PNG)

And then our table, gave us some insight into individual results that could then be further filtered and organized.

![](https://github.com/tnweiss/Benchmarkr-cpp/raw/master/images/BenchmarkrDashboardTable.PNG)

## Related Benchmarkr Projects

- [Benchmarkr](https://github.com/tnweiss/Benchmarkr-cpp) - 
  Includes source code for the benchmarkr executable and c++ dev libraries
- [Benchmarkr Maven Plugin](https://github.com/tnweiss/benchmarkr-java-maven-plugin) -
  Benchmarkr plugin for maven
- [Benchmarkr Gradle Plugin](https://github.com/tnweiss/benchmarkr-java-gradle-plugin) -
  Benchmarkr plugin for gradle
- [Benchmarkr Jetbrains Plugin](https://github.com/tnweiss/benchmarkr-jetbrains-plugin) -
  Jetbrains plugin for Intellij and CLION that makes it easier to run individual tests from the gutter
- [Benchmarkr Configuration](https://github.com/tnweiss/benchmarkr-configuration) -
  Centralized configuration for elastic dashboards and indices

## Usage

### Installing Benchmarkr

If you'd like to upload test results, be sure to follow the instructions on [Benchmarkr CPP](https://github.com/tnweiss/Benchmarkr-cpp)

In order to use this project, you may need to add githubs maven repositories into your settings.xml. The snippet below 
shows the profile you may need to use.

```xml
    <profile>
      <id>benchmarkr</id>
      <repositories>
        <repository>
          <id>central</id>
          <url>https://repo.maven.apache.org/maven2</url>
          <snapshots>
            <enabled>false</enabled>
          </snapshots>
        </repository>
        <repository>
          <id>benchmarkr-java</id>
          <url>https://maven.pkg.github.com/tnweiss/benchmarkr-java</url>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
        </repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
            <id>benchmarkr-java-maven-plugin</id>
            <url>https://maven.pkg.github.com/tnweiss/benchmarkr-java-maven-plugin</url>
        </pluginRepository>
      </pluginRepositories>
    </profile>
```

If you're using gradle...

```groovy
repositories {
    // ...
    maven {
        name = "BenchmarkrJavaGithubPackages"
        url = "https://maven.pkg.github.com/tnweiss/benchmarkr-java"
        credentials {
            username = System.getenv("GITHUB_USERNAME")
            password = System.getenv("GITHUB_ACCESS_TOKEN")
        }
    }
    maven {
      name = "BenchmarkrJavaGradleGithubPackages"
      url = "https://maven.pkg.github.com/tnweiss/benchmarkr-java-gradle-plugin"
      credentials {
        username = System.getenv("GITHUB_USERNAME")
        password = System.getenv("GITHUB_ACCESS_TOKEN")
      }
    }
}
```

Include benchmarkr in your maven project by adding it as a test dependency ...

```xml
<dependency>
      <groupId>com.github.benchmarkr</groupId>
      <artifactId>benchmarkr</artifactId>
      <version>0.0.2</version>
      <scope>test</scope>
</dependency>
```

If you're using gradle ...

```groovy
dependencies {
  testImplementation 'com.github.benchmarkr:benchmarkr:0.0.2'
}
```

This will give you access to annotations to mark benchmark tests, but you'll also need to add the benchmarkr plugin
to trigger the benchmarkr tests.

```xml
     <plugin>
        <groupId>com.github.benchmarkr</groupId>
        <artifactId>benchmarkr-maven-plugin</artifactId>
        <version>0.0.2</version>
        <executions>
          <execution>
            <id>benchmarkr</id>
            <goals>
              <goal>benchmark</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
```

If you're using gradle ...

```groovy
plugins {
    id 'java'
    id 'com.github.benchmarkr' version '0.0.2'
}
```

Below are some configuration options you can place in the `properties` section of your pom file.

- `benchmarkr.config.packageName` A specific package to discover tests in. Defaults to your projects `groupId`
- `benchmarkr.config.class` A specific class to search for benchmark tests in. Defaults to `null`.
- `benchmarkr.config.method` A specific method to run. Defaults to `null`
- `benchmarkr.config.iterations` The number of times to run each benchmarking tests. defaults to `1`
- `benchmarkr.config.console` The type of console to use when printing results. Default is `System`, options are
`System` or `Silent`
- `benchmarkr.config.record` Tells benchmarkr whether to write results to a file. Default is `true`
- `benchmarkr.config.ignoreFailures` If true, the test phase will pass even if a test duration exceeds its upper bound. 
Defaults to `false`

If you're using gradle, run `./gradlew help --task benchmark` to get command line options. You'll also have access
to task configuration in your build scripts.

```groovy
benchmark {
    ignoreFailures = false
    iterations = 10
    // ...
}
```

### Annotations Summary

Below is a summary of all the annotations provided by this project. 

#### Lifecycle Annotations

Below are all the annotations used to mark methods that should be run. The list is ordered by stage in the test lifecycle.

- `@BeforeAll` Run once before all tests. Must have a signature of `public static void <NAME>()`
- `@BeforeClass` Run once before all tests in a given class. Must have a signature of `public static void <NAME>()`
- `@Before` Run before each test in the given class. Must have a signature of `public void <NAME>()`
- `@Benchmark` Marks a benchmarking test. The execution of the entire method will be saved & persisted. 
Must have a signature of `public void <NAME>()`
- `@After` Run after each test in the given class. Must have a signature of `public void <NAME>()`
- `@AfterClass` Run once after all tests in a given class. Must have a signature of `public static void <NAME>()`
- `@AfterAll` Run once after all tests. Must have a signature of `public static void <NAME>()`

#### Metadata Annotations

The annotations below are used to supplement test results with context & descriptions about the test.

- `@CustomProperty`Custom Key / Value pair that persists with the test data. This annotation can be used multiple times
on a single test
- `@Description` provides a long description about the test, does not persist to elastic but will be used to provide 
context on any test failures
- `@TestName` provides a cleaner name for the test as an alternative to the package name + class name + method name.

#### Metric Annotations

The annotations below are used to provide constraints for tests.

- `@LowerBound` This is the expected floor of the benchmark test. If a test durations falls below this it ints at a
significant improvement in the software.
- `@UpperBound` This is the expected ceiling of the benchmark test. Going above this means something the software got 
significantly slower.

### Examples

Below are some example benchmarking tests.

#### Scenario 1, User Service Benchmark

```java
public class UsersBenchmark {
  @LowerBound(40)
  @UpperBound(250)
  @CustomProperty(key = "category", value = "user")
  @Benchmark
  public void addUser() throws InterruptedException {
    Thread.sleep(ThreadLocalRandom.current().nextInt(40, 250));
  }

  @UpperBound(250)
  @CustomProperty(key = "category", value = "user")
  @CustomProperty(key = "cache", value = "true")
  @TestName("GetUser")
  @Description("Test retrieving a user from the database")
  @Benchmark
  public void getUser() throws InterruptedException {
    Thread.sleep(ThreadLocalRandom.current().nextInt(0, 300));
  }

  @Benchmark
  public void updateUser() throws InterruptedException {
    Thread.sleep(ThreadLocalRandom.current().nextInt(0, 300));
  }

  @Description("Delete a user from the database")
  @LowerBound(10)
  @UpperBound(15)
  @Benchmark
  public void deleteUser() throws InterruptedException {
    Thread.sleep(ThreadLocalRandom.current().nextInt(0, 20));
  }
}
```

#### Scenario 2, Group Service Benchmark

```java
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
```
