package com.allobank.allobackendtest.model.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Converter
public class StringListToJsonConverter implements AttributeConverter<List<String>, String> {
  private final Gson gson = new Gson();

  @Override
  public String convertToDatabaseColumn(List<String> attribute) {
    return gson.toJson(attribute);
  }

  @Override
  public List<String> convertToEntityAttribute(String dbData) {
    if (dbData == null) return new ArrayList<>();
    Type listType = new TypeToken<ArrayList<String>>() {}.getType();
    return gson.fromJson(dbData, listType);
  }
}
