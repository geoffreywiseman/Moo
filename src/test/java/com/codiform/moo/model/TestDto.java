package com.codiform.moo.model;

import java.util.List;

import com.codiform.moo.annotation.Translate;
import com.codiform.moo.annotation.TranslateCollection;
import com.codiform.moo.annotation.Translation;

public class TestDto {

	private String string;
	
	private int integer;
	
	private float floatingPoint;
	
	private List<Integer> values;
	
	@Translate
	private TestDto self;
	
	@TranslateCollection(TestDto.class)
	private List<TestDto> selves;

	@Translation("string.length()")
	private int stringLength;
	
	public String getString() {
		return string;
	}

	public int getInteger() {
		return integer;
	}
	
	public float getFloatingPoint() {
		return floatingPoint;
	}
	
	public int getStringLength() {
		return stringLength;
	}

	public List<Integer> getValues() {
		return values;
	}
	
	public TestDto getSelf() {
		return self;
	}
	
	public List<TestDto> getSelves() {
		return selves;
	}
}
