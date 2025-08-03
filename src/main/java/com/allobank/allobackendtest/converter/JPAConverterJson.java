package com.allobank.allobackendtest.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;

@Converter
public class JPAConverterJson implements AttributeConverter<List<String>, String>{

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute){
        try {
            return objectMapper.writeValueAsString(attribute);}
        catch (JsonProcessingException e){
            throw new IllegalArgumentException("Gagal konversi ke JSON", e);
        }
        }

    @Override
    public List<String> convertToEntityAttribute(String dbData){
        try{
            return objectMapper.readValue(dbData,objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (IOException e) {
            throw new IllegalArgumentException("Gagal baca JSON dari DB", e);
        }
    }
    }

