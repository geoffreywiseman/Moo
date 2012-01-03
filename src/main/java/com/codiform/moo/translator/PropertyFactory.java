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
			CollectionFieldProperty property = new CollectionFieldProperty(
					field );
			if( property.isProperty( mode ) ) {
				return property;
			} else {
				return null;
			}
		} else {
			return createFieldProperty( field, mode );
		}
	}

	private static Property createFieldProperty(Field field, AccessMode mode) {
		Ignore ignoreAnnotation = field.getAnnotation( Ignore.class );
		com.codiform.moo.annotation.Property propertyAnnotation = field.getAnnotation( com.codiform.moo.annotation.Property.class );

		String name = field.getName();
		boolean explicit = propertyAnnotation != null
				|| ignoreAnnotation != null;
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
			} else {
				return null;
			}
		} else {
			return null;
		}
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
		Class<?>[] parameters = method.getParameterTypes();
		if( parameters.length == 1 && isCollectionOrMap( parameters[0] ) ) {
			CollectionMethodProperty property = new CollectionMethodProperty(
					method );
			if( property.isProperty( mode ) ) {
				return property;
			} else {
				return null;
			}
		} else {
			MethodProperty property = new MethodProperty( method );
			if( property.isProperty( mode ) ) {
				return property;
			} else {
				return null;
			}
		}
	}

}
