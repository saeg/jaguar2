# Running Jaguar2 with JaCoco in Jsoup
## 0. Prerequisites 
* JDK 11
* Maven 3.9.9+
* Maven Surefire plugin 3.5.3+
* JaCoCo Maven plugin 0.8.7
* Junit 4 (preferred 4.12)
* Maven Central set up as repository
* Defects4J

## 1. Setting up the dependencies 
Add these Jaguar2's dependencies to your `dependencies` tag:

```
    <dependency>
      <groupId>br.usp.each.saeg</groupId>
      <artifactId>jaguar2-junit4</artifactId>
      <version>0.0.2</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>br.usp.each.saeg</groupId>
      <artifactId>jaguar2-jacoco-provider</artifactId>
      <version>0.0.2</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>br.usp.each.saeg</groupId>
      <artifactId>jaguar2-csv-exporter</artifactId>
      <version>0.0.2</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>br.usp.each.saeg</groupId>
      <artifactId>jaguar2-core</artifactId>
      <version>0.0.2</version>
      <scope>test</scope>
    </dependency>
```
The newest version of Jaguar2 available in Maven Central as of September 2025 is 0.0.2.

## 2. Attach JaCoCo agent
This execution method runs Jaguar2's listener on the data collected by JaCoCo, therefore the JaCoCo agent must be run before calling the listener. Add this plugin:
```
      <plugin>
        <groupId>org.jacoco</groupId>
        <artifactId>jacoco-maven-plugin</artifactId>
        <version>0.8.7</version>
        <executions>
          <execution>
            <id>prepare-agent</id>
            <goals>
              <goal>prepare-agent</goal>
            </goals>
          </execution>
          <execution>
            <id>report</id>
            <phase>prepare-package</phase>
            <goals>
              <goal>report</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
```
Through this plugin, JaCoCo will generate the jacoco.exec file which will be used by Jaguar2 for its analysis.

Although version 0.8.7 of `jacoco-maven-plugin` is not the newest, it is the one that seems to run better with Jaguar2.

## 3. Setup Java's version
Jsoup and Jaguar2 are designed to be compatible with Java 11, therefore you need to do the following:

#### a. Comment out `animal-sniffer-maven-plugin` 
This plugin forces the use of Java 7 and causes the build to fail when trying to build with other Javac version.
#### b. Set the Java version for the compiler plugin
Add this setup for the `maven-compiler-plugin` under the `<build>` tag:
```
     <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.8.0</version>
        <configuration>
          <source>11</source>
          <target>11</target>
          <encoding>UTF-8</encoding>
        </configuration>
      </plugin> 
```
## 4. Run maven 'test' lifecycle
Assuming the personalized POM is the file `jsoup-jaguar2-pom.xml`, run the following command line:
```
mvn -f jsoup-jaguar2-pom.xml clean test
```
