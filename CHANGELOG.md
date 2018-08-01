# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.1.0] - 2017-08-01

### Added
- List item factories using `@CollectionProperty(itemFactory=FactoryClass.class)`

### Changed
- Made a series of small dependency updates:
    - Maven SCM, Release and Compiler plugins
    - MVEL
    - JUnit
    - SLF4J
    - Mockito
    - Logback


## [2.0] - 2014-05-23
For more details, see the [Wiki entry](https://github.com/geoffreywiseman/Moo/wiki/Release-2.0).

### Added
- *Item Source Expressions*: This allows the the mapping of an item in a collection 
to a property of that source item in the destination.
- *Map Translation*: Support for the translation of maps.
- Translating objects to strings.
- `TranslationTargetFactory` allowing the target's implementation to be controlled somewhat.
- Cross-collection translation, like translating a list to a set.  

### Changed
- Moved to a multi-module build.
- Split `moo-mvel` into a separate module for people who didn't want or need MVEL.
