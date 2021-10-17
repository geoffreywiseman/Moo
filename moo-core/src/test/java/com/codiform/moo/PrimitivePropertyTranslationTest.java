package com.codiform.moo;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

public class PrimitivePropertyTranslationTest {

	private TestDomain domain = new TestDomain( Integer.MAX_VALUE, Long.MAX_VALUE, Boolean.TRUE, Float.MAX_VALUE, Double.MAX_VALUE );

	@Test
	public void testTranslateCopiesObjectPropertyValuesFromSourceToDestination() {
		TestDto dto = new Moo().translate( domain, TestDto.class );
		assertEquals( domain.getIntegerProperty(), dto.getIntegerProperty() );
		assertEquals( domain.getLongProperty(), dto.getLongProperty() );
		assertEquals( domain.getBooleanProperty(), dto.getBooleanProperty() );
		assertThat( domain.getFloatProperty(), is( dto.getFloatProperty() ) );
		assertThat( domain.getDoubleProperty(), is( dto.getDoubleProperty() ) );
	}

	public static class TestDomain {

		public TestDomain( int integerProperty, long longProperty, boolean booleanProperty, float floatProperty, double doubleProperty ) {
			super();
			this.integerProperty = integerProperty;
			this.longProperty = longProperty;
			this.booleanProperty = booleanProperty;
			this.floatProperty = floatProperty;
			this.doubleProperty = doubleProperty;
		}

		private int integerProperty;
		private long longProperty;
		private boolean booleanProperty;
		private float floatProperty;
		private double doubleProperty;

		public int getIntegerProperty() {
			return integerProperty;
		}

		public long getLongProperty() {
			return longProperty;
		}

		public boolean getBooleanProperty() {
			return booleanProperty;
		}

		public float getFloatProperty() {
			return floatProperty;
		}

		public double getDoubleProperty() {
			return doubleProperty;
		}

	}

	public static class TestDto {

		private int integerProperty;
		private long longProperty;
		private boolean booleanProperty;
		private float floatProperty;
		private double doubleProperty;

		public int getIntegerProperty() {
			return integerProperty;
		}

		public long getLongProperty() {
			return longProperty;
		}

		public boolean getBooleanProperty() {
			return booleanProperty;
		}

		public float getFloatProperty() {
			return floatProperty;
		}

		public double getDoubleProperty() {
			return doubleProperty;
		}

	}
}
