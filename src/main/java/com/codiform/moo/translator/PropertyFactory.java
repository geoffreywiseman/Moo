package com.codiform.moo.translator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import com.codiform.moo.annotation.AccessMode;

public class PropertyFactory {

	public static Property createProperty(Field field, AccessMode mode) {
		if( isCollectionOrMap( field.getType() ) ) {
			CollectionFieldProperty property = new CollectionFieldProperty( field );
			if( property.isProperty( mode ) ) {
				return property;
			} else {
				return null;
			}
		} else {
			FieldProperty property = new FieldProperty( field );
			if( property.isProperty( mode ) )
				return property;
			else
				return null;
		}
	}

	private static boolean isCollectionOrMap(Class<?> type) {
		return Map.class.isAssignableFrom( type ) || Collection.class.isAssignableFrom( type );
	}

	public static Property createProperty(Method method, AccessMode mode ) {
		Class<?>[] parameters = method.getParameterTypes();
		if( parameters.length == 1 && isCollectionOrMap( parameters[0] ) ) {
			CollectionMethodProperty property = new CollectionMethodProperty( method );
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
