package com.codiform.moo.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simplistic model for comparing primitives for assignment. Stupid, but better
 * than Java reflection.
 */
public class PrimitiveAssignment {

	private static final Map<Class<?>, List<Class<?>>> compatibilityMatrix = new HashMap<>();

	static {
		primitive(byte.class, canAssign(byte.class, Byte.class));
		primitive(short.class, canAssign(short.class, Short.class, byte.class,
				Byte.class));
		primitive(char.class, canAssign(short.class, Short.class, byte.class,
				Byte.class));
		primitive(int.class, canAssign(int.class, Integer.class, short.class,
				Short.class, byte.class, Byte.class));
		primitive(long.class,
				canAssign(long.class, Long.class, int.class, Integer.class,
						short.class, Short.class, byte.class, Byte.class));
		primitive(float.class, canAssign(float.class, Float.class, long.class,
				Long.class, int.class, Integer.class, short.class, Short.class,
				byte.class, Byte.class));
		primitive(double.class,
				canAssign(double.class, Double.class, float.class, Float.class,
						long.class, Long.class, int.class, Integer.class,
						short.class, Short.class, byte.class, Byte.class));
		primitive(boolean.class, canAssign(Boolean.class, boolean.class));
	}

	public static boolean isCompatible(Class<?> primitiveType,
			Class<?> otherType) {
		if (compatibilityMatrix.containsKey(primitiveType)) {
			return compatibilityMatrix.get(primitiveType).contains(otherType);
		} else {
			return false;
		}
	}

	private static void primitive(Class<?> primitiveClass,
			List<Class<?>> canAssign) {
		compatibilityMatrix.put(primitiveClass, canAssign);
	}

	private static List<Class<?>> canAssign(Class<?>... items) {
		List<Class<?>> classes = new ArrayList<>();
		for (Class<?> item : items) {
			classes.add(item);
		}
		return classes;
	}

}
