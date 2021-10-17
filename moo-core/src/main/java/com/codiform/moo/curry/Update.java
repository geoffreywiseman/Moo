package com.codiform.moo.curry;

import java.util.HashMap;
import java.util.Map;

import com.codiform.moo.session.TranslationSession;
import com.codiform.moo.translator.CachingTranslatorFactory;
import com.codiform.moo.translator.TranslatorFactory;

/**
 * A curried update operation, where the source of the update has been
 * specified, but the object to which updates should be applied has not.
 * 
 * <p>
 * This can be used to perform updates on several objects, or simply to act as a
 * kind of easy-to-use DSL.
 * </p>
 */
public class Update {

	/**
	 * The source of data to be applied as updates.
	 */
	private Object source;

	private Map<String, Object> variables;
	
	private TranslatorFactory translatorFactory;

	public Update(TranslatorFactory factory, Object source) {
		this.translatorFactory = factory;
		this.source = source;
		this.variables = new HashMap<>();
	}

	/**
	 * Apply the update from the source to the specified destination.
	 * 
	 * @param destination
	 *            the object to which updates should be applied.
	 */
	public void to(Object destination) {
		new TranslationSession(translatorFactory,variables).update(source, destination);
	}

	/**
	 * Create a curried update based on a particular source.
	 * 
	 * @param source
	 *            the source of the update
	 * @return the curried update operation
	 */
	public static Update from(Object source) {
		return new Update(new CachingTranslatorFactory(), source);
	}

	/**
	 * Retain a variable for use by translations during the update.
	 * 
	 * @param variableName the name of the variable to be stored
	 * @param variableValue the value for the variable to be stored
	 * @return the curried update operation
	 */
	public Update withVariable(String variableName, Object variableValue) {
		variables.put(variableName, variableValue);
		return this;
	}

}
