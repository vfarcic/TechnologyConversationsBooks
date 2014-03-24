BOOKS
=====

Whole project is done in the TDD fashion (test, implementation, refactor).
Dependency management and tasks execution is done using Gradle.

## Tests

Tests are executed on every commit through [Travis](https://travis-ci.org/vfarcic/TechnologyConversationsBooks)

To run tests manually:

```shell
$ gradle test
```

## Distribution

To create standalone distribution:

```shell
$ gradle distZip
```

Resulting ZIP file contains bin directory with OS specific startup scripts.
API can be seen through the [WADL](http://localhost:8080/api/application.wadl).

To create WAR:

```shell
$ gradle war
```