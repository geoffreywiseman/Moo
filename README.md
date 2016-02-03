# Moo: Mapping Objects to Objects

It's not uncommon to need to do some mapping from one object model to another, whether that's
_data transfer objects_, an _anti-corruption layer_, or something else.  Two projects I've been on
have written code similar to this, and I thought it was time to start re-using rather than
building from scratch.

## More Information

You can find more information by reading one of the following:

* the [user guide][moo-guide] for Moo  
* the [website][moo-site] for Moo   
* the automated tests for Moo [Core][core-tests] and [MVEL][mvel-tests]
* the [user group][moo-group] for Moo

## License and Copyright

More information on the copyright and license can be found in NOTICE and LICENSE respectively, but basically
it's BSD licensed and Copyright (C) 2009, Geoffrey Wiseman.

## Continuous Integration

Moo is built by [Travis CI][travis].

[core-tests]: https://github.com/geoffreywiseman/Moo/tree/master/moo-core/src/test/java/com/codiform/moo/ "Tests for Moo Core (github.com)"
[mvel-tests]: https://github.com/geoffreywiseman/Moo/tree/master/moo-mvel/src/test/java/com/codiform/moo/ "Tests for Moo MVEL (github.com)"
[travis]: http://travis-ci.org/#!/geoffreywiseman/Moo "Moo's Continuous Integration (travis-ci.org)"
[moo-guide]: http://wiki.github.com/geoffreywiseman/Moo/user-guide "Moo's User Guide (github.com)"
[moo-site]: http://geoffreywiseman.github.io/Moo "Moo's Website (github.io)"
[moo-group]: https://groups.google.com/forum/#!forum/moo-user "Moo's User Group (groups.google.com)"