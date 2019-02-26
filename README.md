# Trickl AssertJ JSON

[![build_status](https://travis-ci.com/trickl/assertj-json.svg?branch=master)](https://travis-ci.com/trickl/assertj-json)
[![Maintainability](https://api.codeclimate.com/v1/badges/1f66926c8f391be20ad4/maintainability)](https://codeclimate.com/github/trickl/assertj-json/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/1f66926c8f391be20ad4/test_coverage)](https://codeclimate.com/github/trickl/assertj-json/test_coverage)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

An AssertJ fluent assertion wrapper around the Skyscreamer JSON comparison library

### Prerequisites

Requires Maven and a Java 8 compiler installed on your system.

## Example

```
    assertThat(json("{\"age\":43, \"friend_ids\":[16, 23, 52]}"))
        .allowingExtraUnexpectedFields()
        .allowingAnyArrayOrdering()
        .isSameJsonAs("{\"friend_ids\":[52, 23, 16]}");
```

### Installing

To download the library into a folder called "assertj-json" run

```
git clone https://github.com/trickl/assertj-json.git
```

To build the library run

```
mvn clean build
```

## Acknowledgments

AssertJ - http://joel-costigliola.github.io/assertj/

Skyscreamer JSON Library - https://github.com/skyscreamer/JSONassert
