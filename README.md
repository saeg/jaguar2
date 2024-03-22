# jaguar2

## Releases

**Attention:** we are not using semantic versioning (yet).

### 0.0.3-SNAPSHOT

#### Non-functional Changes

Note: Build using Java 22 is skiping BA-DUA examples ([9f1cc63](https://github.com/saeg/jaguar2/pull/98/commits/9f1cc635156ba0b4f4f5980ec7f9bfba6170443d)).

* Update Mockito to 2.28.2 (
  [#92](https://github.com/saeg/jaguar2/pull/92)
).
* CI targeting current JDK (
  [#86](https://github.com/saeg/jaguar2/pull/86),
  [#98](https://github.com/saeg/jaguar2/pull/98)
).
  * GitHub Actions targets all supported bytecode versions (6 to ~~21~~ 22);
  * JaCoCo updated from 0.8.7 to ~~0.8.9~~ 0.8.11;
  * ~~Build with JDK >= 16 opens `java.base/java.lang` to unnamed module;~~ (Reverted by [#92](https://github.com/saeg/jaguar2/pull/92))
  * Build targeting Java 21 and 22 class files skips Animal Sniffer Maven Plugin.
* Update Apache Maven PMD Plugin to 3.21.2 (
  [#93](https://github.com/saeg/jaguar2/pull/93)
).
* Disable PMD type resolution when targeting Java 21 and 22 class files (
  [#94](https://github.com/saeg/jaguar2/pull/94),
  [#98](https://github.com/saeg/jaguar2/pull/98),
).
* Fix invalid `-html5` Javadoc option when building with Java 9 or 10 and using JDK Toolchain 6 or 7 (
  [#96](https://github.com/saeg/jaguar2/pull/96)
).
* Dogfooding 🐕 (
  [#100](https://github.com/saeg/jaguar2/pull/100)
).

### [v0.0.2](https://github.com/saeg/jaguar2/releases/tag/v0.0.2)

This version added a new API on `CoverageController` and Jaguar2 Core will not
work with older Jaguar2 Coverage Providers.

#### New Features

* Dump coverage (
  [#67](https://github.com/saeg/jaguar2/pull/67)
).

#### Non-functional Changes

* Require Java 8 to build (
  [#63](https://github.com/saeg/jaguar2/pull/63),
  [#68](https://github.com/saeg/jaguar2/pull/68)
).
* Maven and plugins were updated (
  [#64](https://github.com/saeg/jaguar2/pull/64),
  [#69](https://github.com/saeg/jaguar2/pull/69)
).
* New module Jaguar2 Build (
  [#79](https://github.com/saeg/jaguar2/pull/79),
  [#82](https://github.com/saeg/jaguar2/pull/82),
  [#84](https://github.com/saeg/jaguar2/pull/84),
  [026eb1c](https://github.com/saeg/jaguar2/commit/026eb1c0552390bd0fb61a311f8df1c1fcdc215c),
  [54ee0b1](https://github.com/saeg/jaguar2/commit/54ee0b19545d3da4af7bc4e167fa6e53e8e7ff56),
  [78e62ee](https://github.com/saeg/jaguar2/commit/78e62ee89c0540d8add8fed75ba73955fd65b515)
).

### [v0.0.1-jdk6](https://github.com/saeg/jaguar2/releases/tag/v0.0.1-jdk6)

This is same as [v0.0.1](https://github.com/saeg/jaguar2/releases/tag/v0.0.1)
but was created using JDK `1.6.0_65` (Java 6).

### [v0.0.1](https://github.com/saeg/jaguar2/releases/tag/v0.0.1)

The first release of Jaguar2.

This version was created with Apache Maven 3.2.5 using JDK `1.8.0_382` (Java 8)
but compiled classes target Java 6 (i.e., Class file version 50.0).
