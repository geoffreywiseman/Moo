package com.codiform.moo.translator;


/**
 * An interface for finding the target of an update in one collection based on a source object from another.
 * Without this, matches will simply be made by iterating through both collections and updating in order.
 * 
 * @param <S> the type that will be used to search for a target 
 * @param <T> the type of the target
 */
public interface IndexMatcher<S,T> extends CollectionMatcher<S,T> {
}
