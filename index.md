---
layout: default
title: Mapping Objects to Objects
---
<div class="description">
Moo maps an object or graph of objects to another object or set of objects while trying to stay as unintrusive as possible and easy-to-use.  Moo makes it possible to create quick copies and data transfer objects.
</div>

There are a lot of ways you could use Moo -- to create data transfer objects, to work as an anti-corruption layer, to help summarize an object graph before serializing to XML or JSON. Examples of Moo at work can be found in the automated tests (for [moo-core][tests-core], [moo-mvel][tests-mvel]).

The simplest examples of using Moo might look something like these:

```java
Translate.to( UserDto.class ).from( currentUser );

Translate.to( UserDto.class ).fromEach( selectedUsers );

Update.from( userDto ).to( currentUser );
```

<div class="button-bar">
	<a class="button" href="/Moo/get-moo.html">Get It</a>
	<a class="button" href="https://github.com/geoffreywiseman/Moo/wiki">Learn More</a>
	<a class="button" href="/Moo/news.html">News</a>
	<a class="button" href="https://github.com/geoffreywiseman/Moo/">Code</a>
</div>

[tests-core]: https://github.com/geoffreywiseman/Moo/tree/master/moo-core/src/test/java/com/codiform/moo/
[tests-mvel]: https://github.com/geoffreywiseman/Moo/tree/master/moo-mvel/src/test/java/com/codiform/moo/
