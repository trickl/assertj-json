# Trickl AssertJ JSON
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.trickl/assertj-json/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.trickl/assertj-json)
[![build_status](https://travis-ci.com/trickl/assertj-json.svg?branch=master)](https://travis-ci.com/trickl/assertj-json)
[![Maintainability](https://api.codeclimate.com/v1/badges/1f66926c8f391be20ad4/maintainability)](https://codeclimate.com/github/trickl/assertj-json/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/1f66926c8f391be20ad4/test_coverage)](https://codeclimate.com/github/trickl/assertj-json/test_coverage)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

An AssertJ fluent assertion wrapper around the Skyscreamer JSON comparison library

Installation
============

To install from Maven Central:

```xml
<dependency>
  <groupId>com.github.trickl</groupId>
  <artifactId>assertj-json</artifactId>
  <version>0.2.8</version>
</dependency>
```

Example
==========

```
    assertThat(json("{\"age\":43, \"friend_ids\":[16, 23, 52]}"))
        .allowingExtraUnexpectedFields()
        .allowingAnyArrayOrdering()
        .isSameJsonAs("{\"friend_ids\":[52, 23, 16]}");
```

## Acknowledgments

AssertJ - http://joel-costigliola.github.io/assertj/

Skyscreamer JSON Library - https://github.com/skyscreamer/JSONassert
