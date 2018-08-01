package com.codiform.moo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Ignore;
import org.junit.Test;

public class AssignmentTestCase {

	@Test
	public void testPrimitiveAssignment() throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			SecurityException, NoSuchMethodException {
		Method getter = getClass().getMethod("getFloat");
		Method setter = getClass().getMethod("setDouble", double.class);
		Object result = getter.invoke(this);
		setter.invoke(this, result);
	}

	@Test
	public void testAutomaticConversionAssignment() throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			SecurityException, NoSuchMethodException {
		Method getter = getClass().getMethod("getFloat");
		Method setter = getClass().getMethod("setFloat", float.class);
		Object result = getter.invoke(this);
		setter.invoke(this, result);
	}

	@Test
	@Ignore("Reflection Doesn't Know")
	public void testAutomaticConversionAssignable() {
		System.out.println("l->i: " + long.class.isAssignableFrom(int.class));
		System.out.println("i->l: " + int.class.isAssignableFrom(long.class));
	}

	@Test
	@Ignore("Reflection Doesn't Know")
	public void testPrimitiveAssignable() throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Method getter = getClass().getMethod("getFloat");
		Method setter = getClass().getMethod("setFloat", float.class);
		Object result = getter.invoke(this);
		assertCanAssign(setter, result);
	}

	private void assertCanAssign(Method setter, Object result) {
		Class<?> parameterClass = setter.getParameterTypes()[0];
		Class<?> resultClass = result.getClass();

		System.out.println("Setter Parameter: " + parameterClass.getName());
		System.out.println("Result: " + result + " (" + resultClass.getName()
				+ ")");
		System.out
				.println("parameterClass.isAssignableFrom(result.getClass() == "
						+ parameterClass.isAssignableFrom(result.getClass()));
		System.out.println("resultClass.isAssignableFrom(parameterClass == "
				+ resultClass.isAssignableFrom(parameterClass));
		System.out.println("parameterClass.instanceOf(result) == "
				+ parameterClass.isInstance(result));
	}

	public float getFloat() {
		return 1F;
	}

	public void setFloat(float item) {
		// do nothing
	}
	
	public void setDouble(double item) {
		//do nothing
	}

}
