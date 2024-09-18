package com.allobank.allobackendtest.converter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ListStringConverter implements AttributeConverter<List<String>, String>{
	private static final String SPLIT_CHAR = ";";
	
	@Override
	public String convertToDatabaseColumn(List<String> stringList) {
		return stringList != null ? String.join(SPLIT_CHAR, stringList) : "";
	}

	@Override
	public List<String> convertToEntityAttribute(String string) {
		return string != null ? Arrays.asList(string.split(SPLIT_CHAR)) : new LinkedList<>();
	}
	
}
