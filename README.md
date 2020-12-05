# advent-of-code-2020

This repository contains code for the Advent of Code 2020 (https://adventofcode.com/2020)

## Structure

The project is written in Kotlin and built with Gradle (no standalone Kotlin
compiler on Debian yet, :sob:), so the structure follows the Kotlin application
plugin conventions.

All the code is under `app/src/main/kotlin`, where you can find the directory
structure matching the package `advent.of.code`, Java-style. Each day's puzzles
are solved in a namesake singleton `object` (e.g. `advent.of.code.Day1`),
contained in its own file (e.g. `advent/of/code/Day1.kt`). These object expose
two public methods, called `puzzle1` and `puzzle2`, that take in the list of
lines read from the input file and any additional arguments. The signature
looks as follows:

```kotlin
fun puzzle1(inputLines: Sequence<String>, args: Iterable<String>)
```

## Running

The code can be run by means of the Gradle task `run`, exposed by the Java
application plugins. The task arguments are, in order:
- `dayX` - where `X` is a number, to select the day. Mandatory.
- `puzzleX` - where `X` is a number, to select the puzzle. Mandatory.
- The path to the input file. Optional, defaults to the first argument with a
  `.txt` extension appended. Can be one of:
  + An absolute path
  + A path relative to the repository root directory
  + A path relative to `inputs` in the repository root directory.

Some examples, ran from the repository root directory:
```shell
./gradlew run --args 'day3 puzzle2'

./gradlew run --args 'day2 puzzle1 data.txt'

./gradlew run --args 'day10 puzzle2 inputs/values.txt'

./gradlew run --args "day7 puzzle1 $HOME/lines.txt"
```
