# frontiersmen-server

[![Build Status](https://travis-ci.org/frontiersmen/frontiersmen-server.svg?branch=master)](https://travis-ci.org/frontiersmen/frontiersmen-server)
[![Coverage Status](https://coveralls.io/repos/github/frontiersmen/frontiersmen-server/badge.svg?branch=master)](https://coveralls.io/github/frontiersmen/frontiersmen-server?branch=master)
[![codecov](https://codecov.io/gh/frontiersmen/frontiersmen-server/branch/master/graph/badge.svg)](https://codecov.io/gh/frontiersmen/frontiersmen-server)
[![yeah](https://img.shields.io/badge/yeah-👍-blue.svg)](https://en.wiktionary.org/wiki/yeah)

## Requirements
* [PostgreSQL (9.5+)](https://www.postgresql.org/download/)

## Setup
```sh
git clone https://github.com/frontiersmen/frontiersmen-server.git
cd frontiersmen-server
./gradlew createdb
./gradlew run
```
Server should be running on localhost:4567

## Built With
* [Spark](http://sparkjava.com/)
* [Gradle](https://gradle.org/)
* [PostgreSQL](https://www.postgresql.org/)
