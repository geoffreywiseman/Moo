package com.codiform.moo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Test;

public class BasicObjectPropertyTranslationTest {

	@Test
	public void testTranslateCopiesObjectPropertyValuesFromSourceToDestination() {
		TestDomain domain = new TestDomain("Test", Integer.MAX_VALUE,
				Long.MAX_VALUE, Boolean.TRUE, Float.MAX_VALUE,
				Double.MAX_VALUE, new BigDecimal("123456789.123456789"));

		TestDto dto = new Moo().translate(domain,TestDto.class);
		assertEquals(domain.getStringProperty(), dto.getStringProperty());
		assertEquals(domain.getIntegerProperty(), dto.getIntegerProperty());
		assertEquals(domain.getLongProperty(), dto.getLongProperty());
		assertEquals(domain.getBooleanProperty(), dto.getBooleanProperty());
		assertEquals(domain.getFloatProperty(), dto.getFloatProperty());
		assertEquals(domain.getDoubleProperty(), dto.getDoubleProperty());
		assertEquals(domain.getBigDecimalProperty(), dto
				.getBigDecimalProperty());
	}

	@Test
	public void testTranslateCopiesNullValuesFromSourceToDestination() {
		TestDomain domain = new TestDomain(null, null, null, null, null, null,
				null);

		TestDto dto = new Moo().translate(domain,TestDto.class);
		assertNull(dto.getStringProperty());
		assertNull(dto.getIntegerProperty());
		assertNull(dto.getLongProperty());
		assertNull(dto.getBooleanProperty());
		assertNull(dto.getFloatProperty());
		assertNull(dto.getDoubleProperty());
		assertNull(dto.getBigDecimalProperty());
	}

	public static class TestDomain {

		private String stringProperty;
		private Integer integerProperty;
		private Long longProperty;
		private Boolean booleanProperty;
		private Float floatProperty;
		private Double doubleProperty;
		private BigDecimal bigDecimalProperty;

		public TestDomain(String stringProperty, Integer integerProperty,
				Long longProperty, Boolean booleanProperty,
				Float floatProperty, Double doubleProperty,
				BigDecimal bigDecimalProperty) {
			super();
			this.stringProperty = stringProperty;
			this.integerProperty = integerProperty;
			this.longProperty = longProperty;
			this.booleanProperty = booleanProperty;
			this.floatProperty = floatProperty;
			this.doubleProperty = doubleProperty;
			this.bigDecimalProperty = bigDecimalProperty;
		}

		public String getStringProperty() {
			return stringProperty;
		}

		public Integer getIntegerProperty() {
			return integerProperty;
		}

		public Long getLongProperty() {
			return longProperty;
		}

		public Boolean getBooleanProperty() {
			return booleanProperty;
		}

		public Float getFloatProperty() {
			return floatProperty;
		}

		public Double getDoubleProperty() {
			return doubleProperty;
		}

		public BigDecimal getBigDecimalProperty() {
			return bigDecimalProperty;
		}

	}

	public static class TestDto {
		private String stringProperty;
		private Integer integerProperty;
		private Long longProperty;
		private Boolean booleanProperty;
		private Float floatProperty;
		private Double doubleProperty;
		private BigDecimal bigDecimalProperty;

		public String getStringProperty() {
			return stringProperty;
		}

		public Integer getIntegerProperty() {
			return integerProperty;
		}

		public Long getLongProperty() {
			return longProperty;
		}

		public Boolean getBooleanProperty() {
			return booleanProperty;
		}

		public Float getFloatProperty() {
			return floatProperty;
		}

		public Double getDoubleProperty() {
			return doubleProperty;
		}

		public BigDecimal getBigDecimalProperty() {
			return bigDecimalProperty;
		}

	}
}
