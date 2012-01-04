package com.codiform.moo.translator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

import com.codiform.moo.InvalidPropertyException;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.annotation.Ignore;

public class PropertyFactory {

	public static Property createProperty(Field field, AccessMode mode) {
		if( isCollectionOrMap( field.getType() ) ) {
			return createCollectionFieldProperty( field, mode );
		} else {
			return createFieldProperty( field, mode );
		}
	}

	private static Property createCollectionFieldProperty(Field field,
			AccessMode mode) {
		Ignore ignoreAnnotation = field.getAnnotation( Ignore.class );
		com.codiform.moo.annotation.Property propertyAnnotation = field.getAnnotation( com.codiform.moo.annotation.Property.class );

		String name = field.getName();
		boolean explicit = isExplicit( ignoreAnnotation, propertyAnnotation );
		boolean ignore = ignoreAnnotation != null;
		String expression = getExpression( name, propertyAnnotation );

		if( explicit || mode == AccessMode.FIELD ) {
			String errorMessage = validateField( field );
			if( errorMessage == null ) {
				return new CollectionFieldProperty( field, name, expression,
						explicit,
						ignore );
			} else if( explicit ) {
				throw new InvalidPropertyException( name,
						field.getDeclaringClass(), errorMessage );
			}
		}
		return null;
	}

	private static boolean isExplicit(Ignore ignoreAnnotation,
			com.codiform.moo.annotation.Property propertyAnnotation) {
		return propertyAnnotation != null
				|| ignoreAnnotation != null;
	}

	private static Property createFieldProperty(Field field, AccessMode mode) {
		Ignore ignoreAnnotation = field.getAnnotation( Ignore.class );
		com.codiform.moo.annotation.Property propertyAnnotation = field.getAnnotation( com.codiform.moo.annotation.Property.class );

		String name = field.getName();
		boolean explicit = isExplicit( ignoreAnnotation, propertyAnnotation );
		boolean ignore = ignoreAnnotation != null;
		String expression = getExpression( name, propertyAnnotation );

		if( explicit || mode == AccessMode.FIELD ) {
			String errorMessage = validateField( field );
			if( errorMessage == null ) {
				return new FieldProperty( field, name, expression, explicit,
						ignore );
			} else if( explicit ) {
				throw new InvalidPropertyException( name,
						field.getDeclaringClass(), errorMessage );
			}
		}
		return null;
	}

	private static String validateField(Field field) {
		int modifiers = field.getModifiers();
		if( Modifier.isStatic( modifiers ) ) {
			return "%s (%s) is annotated with @Property, but is static.  Moo does not support static fields as properties.";
		} else if( Modifier.isFinal( modifiers ) ) {
			return "%s (%s) is annotated with @Property, but is final.  Moo cannot write to final fields as properties.";
		} else {
			return null;
		}
	}

	private static String getExpression(String name,
			com.codiform.moo.annotation.Property annotation) {
		if( annotation == null || annotation.translation() == null
				|| annotation.translation().isEmpty() ) {
			return name;
		} else {
			return annotation.translation();
		}
	}

	private static boolean isCollectionOrMap(Class<?> type) {
		return Map.class.isAssignableFrom( type )
				|| Collection.class.isAssignableFrom( type );
	}

	public static Property createProperty(Method method, AccessMode mode) {
		com.codiform.moo.annotation.Property propertyAnnotation = method.getAnnotation( com.codiform.moo.annotation.Property.class );
		Ignore ignoreAnnotation = method.getAnnotation( Ignore.class );
		String methodName = method.getName();
		String propertyName = getPropertyName( methodName );
		Class<?>[] parameters = method.getParameterTypes();

		boolean explicit = isExplicit( ignoreAnnotation, propertyAnnotation );
		boolean ignore = ignoreAnnotation != null;
		String expression = getExpression( propertyName, propertyAnnotation );

		if( explicit || mode == AccessMode.METHOD ) {
			String errorMessage = validateMethod( methodName, propertyName,
					parameters, method.getModifiers() );
			if( errorMessage == null ) {
				if( isCollectionOrMap( parameters[0] ) ) {
					return createCollectionMethodProperty( method,
							propertyName, expression, explicit, ignore );
				} else {
					return createMethodProperty( method, propertyName,
							expression,	explicit, ignore );
				}
			} else if( explicit ) {
				throw new InvalidPropertyException(
						propertyName == null ? methodName : propertyName,
						method.getDeclaringClass(), errorMessage );
			}
		}
		return null;
	}

	private static Property createCollectionMethodProperty(Method method,
			String name, String expression, boolean explicit, boolean ignore) {
		return new CollectionMethodProperty( method, name, expression, explicit, ignore );
	}

	private static Property createMethodProperty(Method method, String name,
			String expression, boolean explicit, boolean ignore) {
		return new MethodProperty( method, name, expression, explicit, ignore );
	}

	private static String validateMethod(String methodName,
			String propertyName, Class<?>[] parameters, int modifiers) {
		if( propertyName == null ) {
			return "Method %s (in %s) is annotated as a property but does not follow the 'set<Name>' pattern required of a method property.";
		}
		if( parameters.length != 1 ) {
			return "Method %s (in %s) is marked with @Property but is not a single-parameter method.";
		}
		if( Modifier.isStatic( modifiers ) ) {
			return "Method %s (in %s) is marked with @Property but is static; Moo doesn't support static methods as properties.";
		}

		return null;
	}

	private static String getPropertyName(String methodName) {
		if( methodName.length() > 3 && methodName.startsWith( "set" ) ) {
			return Character.toLowerCase( methodName.charAt( 3 ) )
					+ methodName.substring( 4 );
		} else {
			return null;
		}
	}

}
