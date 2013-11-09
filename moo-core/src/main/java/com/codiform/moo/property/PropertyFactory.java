package com.codiform.moo.property;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

import com.codiform.moo.InvalidPropertyException;
import com.codiform.moo.annotation.AccessMode;
import com.codiform.moo.annotation.Ignore;
import com.codiform.moo.annotation.InvalidAnnotationException;

public class PropertyFactory {

	public static Property createProperty( Field field, AccessMode mode ) {
		if ( isCollection( field.getType() ) ) {
			return createCollectionFieldProperty( field, mode );
		} else if ( isMap( field.getType() ) ) {
			return createMapFieldProperty( field, mode );
		} else {
			return createFieldProperty( field, mode );
		}
	}

	private static Property createCollectionFieldProperty( Field field, AccessMode mode ) {
		Ignore ignoreAnnotation = field.getAnnotation( Ignore.class );
		com.codiform.moo.annotation.CollectionProperty propertyAnnotation = field
				.getAnnotation( com.codiform.moo.annotation.CollectionProperty.class );

		String name = field.getName();
		Class<?> declaringClass = field.getDeclaringClass();

		validateNoPropertyAnnotation( field.getAnnotation( com.codiform.moo.annotation.Property.class ), name, declaringClass );
		validateNoMapPropertyAnnotation( field.getAnnotation( com.codiform.moo.annotation.MapProperty.class ), name, declaringClass );

		boolean explicit = isExplicit( ignoreAnnotation, propertyAnnotation );
		boolean ignore = ignoreAnnotation != null;
		String expression = getExpression( name, propertyAnnotation );

		if ( explicit || mode == AccessMode.FIELD ) {
			String errorMessage = validateField( field );
			if ( errorMessage == null ) {
				return new CollectionFieldProperty( field, propertyAnnotation, name, expression, explicit, ignore );
			} else if ( explicit ) {
				throw new InvalidPropertyException( name, declaringClass, errorMessage );
			}
		}
		return null;
	}

	private static Property createMapFieldProperty( Field field, AccessMode mode ) {
		Ignore ignoreAnnotation = field.getAnnotation( Ignore.class );
		com.codiform.moo.annotation.MapProperty propertyAnnotation = field.getAnnotation( com.codiform.moo.annotation.MapProperty.class );

		String name = field.getName();
		Class<?> declaringClass = field.getDeclaringClass();

		validateNoPropertyAnnotation( field.getAnnotation( com.codiform.moo.annotation.Property.class ), name, declaringClass );
		validateNoCollectionPropertyAnnotation( field.getAnnotation( com.codiform.moo.annotation.CollectionProperty.class ), name, declaringClass );

		boolean explicit = isExplicit( ignoreAnnotation, propertyAnnotation );
		boolean ignore = ignoreAnnotation != null;
		String expression = getExpression( name, propertyAnnotation );

		if ( explicit || mode == AccessMode.FIELD ) {
			String errorMessage = validateField( field );
			if ( errorMessage == null ) {
				return new MapFieldProperty( field, propertyAnnotation, name, expression, explicit, ignore );
			} else if ( explicit ) {
				throw new InvalidPropertyException( name, declaringClass, errorMessage );
			}
		}
		return null;
	}

	private static void validateNoPropertyAnnotation( com.codiform.moo.annotation.Property annotation, String propertyName, Class<?> declaringClass ) {
		if ( annotation != null ) {
			throw new InvalidAnnotationException(
					"The property %s on class %s is a map or collection and should not be annotated with @Property; use @CollectionProperty or @MapProperty instead.",
					propertyName, declaringClass );
		}
	}

	private static void validateNoCollectionPropertyAnnotation( com.codiform.moo.annotation.CollectionProperty annotation, String propertyName,
			Class<?> declaringClass ) {
		if ( annotation != null ) {
			throw new InvalidAnnotationException(
					"The property %s on class %s is not a collection and should not be annotated with @CollectionProperty.", propertyName,
					declaringClass );
		}
	}

	private static void validateNoMapPropertyAnnotation( com.codiform.moo.annotation.MapProperty annotation, String propertyName,
			Class<?> declaringClass ) {
		if ( annotation != null ) {
			throw new InvalidAnnotationException( "The property %s on class %s is not a map and should not be annotated with @MapProperty.",
					propertyName, declaringClass );
		}
	}

	private static boolean isExplicit( Ignore ignoreAnnotation, Annotation... propertyAnnotations ) {
		if ( ignoreAnnotation != null ) {
			return true;
		} else {
			for ( Annotation item : propertyAnnotations ) {
				if ( item != null ) {
					return true;
				}
			}
			return false;
		}
	}

	private static Property createFieldProperty( Field field, AccessMode mode ) {
		Ignore ignoreAnnotation = field.getAnnotation( Ignore.class );
		com.codiform.moo.annotation.Property propertyAnnotation = field.getAnnotation( com.codiform.moo.annotation.Property.class );

		String name = field.getName();
		Class<?> declaringClass = field.getDeclaringClass();
		boolean explicit = isExplicit( ignoreAnnotation, propertyAnnotation );
		boolean ignore = ignoreAnnotation != null;

		validateNoCollectionPropertyAnnotation( field.getAnnotation( com.codiform.moo.annotation.CollectionProperty.class ), name, declaringClass );
		validateNoMapPropertyAnnotation( field.getAnnotation( com.codiform.moo.annotation.MapProperty.class ), name, declaringClass );

		String expression = getExpression( name, propertyAnnotation );

		if ( explicit || mode == AccessMode.FIELD ) {
			String errorMessage = validateField( field );
			if ( errorMessage == null ) {
				return new FieldProperty( field, propertyAnnotation, name, expression, explicit, ignore );
			} else if ( explicit ) {
				throw new InvalidPropertyException( name, declaringClass, errorMessage );
			}
		}
		return null;
	}

	private static String validateField( Field field ) {
		int modifiers = field.getModifiers();
		if ( Modifier.isStatic( modifiers ) ) {
			return "%s (%s) is annotated with @Property, but is static.  Moo does not support static fields as properties.";
		} else if ( Modifier.isFinal( modifiers ) ) {
			return "%s (%s) is annotated with @Property, but is final.  Moo cannot write to final fields as properties.";
		} else {
			return null;
		}
	}

	private static String getExpression( String name, com.codiform.moo.annotation.Property annotation ) {
		if ( annotation == null || annotation.source() == null || annotation.source().isEmpty() ) {
			return name;
		} else {
			return annotation.source().trim();
		}
	}

	private static String getExpression( String name, com.codiform.moo.annotation.MapProperty annotation ) {
		if ( annotation == null || annotation.source() == null || annotation.source().isEmpty() ) {
			return name;
		} else {
			return annotation.source().trim();
		}
	}

	
	private static String getExpression( String name, com.codiform.moo.annotation.CollectionProperty annotation ) {
		if ( annotation == null || annotation.source() == null || annotation.source().isEmpty() ) {
			return name;
		} else {
			return annotation.source().trim();
		}
	}

	private static boolean isCollection( Class<?> type ) {
		return Collection.class.isAssignableFrom( type );
	}

	private static boolean isMap( Class<?> type ) {
		return Map.class.isAssignableFrom( type );
	}

	public static Property createProperty( Method method, AccessMode mode ) {
		com.codiform.moo.annotation.Property propertyAnnotation = method.getAnnotation( com.codiform.moo.annotation.Property.class );
		com.codiform.moo.annotation.CollectionProperty collectionAnnotation = method
				.getAnnotation( com.codiform.moo.annotation.CollectionProperty.class );
		com.codiform.moo.annotation.MapProperty mapAnnotation = method.getAnnotation( com.codiform.moo.annotation.MapProperty.class );
		Ignore ignoreAnnotation = method.getAnnotation( Ignore.class );
		String methodName = method.getName();
		String propertyName = getPropertyName( methodName );
		Class<?>[] parameters = method.getParameterTypes();

		boolean explicit = isExplicit( ignoreAnnotation, propertyAnnotation, collectionAnnotation );
		boolean ignore = ignoreAnnotation != null;

		if ( explicit || mode == AccessMode.METHOD ) {
			String errorMessage = validateMethod( methodName, propertyName, parameters, method.getModifiers() );
			Class<?> declaringClass = method.getDeclaringClass();
			if ( errorMessage == null ) {
				if ( isCollection( parameters[0] ) ) {
					validateNoPropertyAnnotation( propertyAnnotation, propertyName, declaringClass );
					validateNoMapPropertyAnnotation( mapAnnotation, propertyName, declaringClass );
					return createCollectionMethodProperty( method, collectionAnnotation, propertyName, explicit, ignore );
				} else if ( isMap( parameters[0] ) ) {
					validateNoPropertyAnnotation( propertyAnnotation, propertyName, declaringClass );
					validateNoCollectionPropertyAnnotation( collectionAnnotation, propertyName, declaringClass );
					return createMapMethodProperty( method, mapAnnotation, propertyName, explicit, ignore );
				} else {
					validateNoCollectionPropertyAnnotation( collectionAnnotation, propertyName, declaringClass );
					validateNoMapPropertyAnnotation( mapAnnotation, propertyName, declaringClass );
					return createMethodProperty( method, propertyAnnotation, propertyName, explicit, ignore );
				}
			} else if ( explicit ) {
				throw new InvalidPropertyException( propertyName == null ? methodName : propertyName, method.getDeclaringClass(), errorMessage );
			}
		}
		return null;
	}

	private static Property createCollectionMethodProperty( Method method, com.codiform.moo.annotation.CollectionProperty annotation,
			String propertyName, boolean explicit, boolean ignore ) {
		String expression = getExpression( propertyName, annotation );
		return new CollectionMethodProperty( method, annotation, propertyName, expression, explicit, ignore );
	}

	private static Property createMapMethodProperty( Method method, com.codiform.moo.annotation.MapProperty annotation,
			String propertyName, boolean explicit, boolean ignore ) {
		String expression = getExpression( propertyName, annotation );
		return new MapMethodProperty( method, annotation, propertyName, expression, explicit, ignore );
	}

	private static Property createMethodProperty( Method method, com.codiform.moo.annotation.Property annotation, String propertyName,
			boolean explicit, boolean ignore ) {
		String expression = getExpression( propertyName, annotation );
		return new MethodProperty( method, annotation, propertyName, expression, explicit, ignore );
	}

	private static String validateMethod( String methodName, String propertyName, Class<?>[] parameters, int modifiers ) {
		if ( propertyName == null ) {
			return "Method %s (in %s) is annotated as a property but does not follow the 'set<Name>' pattern required of a method property.";
		}
		if ( parameters.length != 1 ) {
			return "Method %s (in %s) is marked with @Property but is not a single-parameter method.";
		}
		if ( Modifier.isStatic( modifiers ) ) {
			return "Method %s (in %s) is marked with @Property but is static; Moo doesn't support static methods as properties.";
		}
		return null;
	}

	private static String getPropertyName( String methodName ) {
		if ( methodName.length() > 3 && methodName.startsWith( "set" ) ) {
			return Character.toLowerCase( methodName.charAt( 3 ) ) + methodName.substring( 4 );
		} else {
			return null;
		}
	}

}
