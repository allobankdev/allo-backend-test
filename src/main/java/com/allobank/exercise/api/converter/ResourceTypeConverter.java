package com.allobank.exercise.api.converter;
import com.allobank.exercise.api.enumeration.ResourceType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ResourceTypeConverter implements Converter<String, ResourceType> {

    @Override
    public ResourceType convert(String source) {
        return ResourceType.fromPath(source);
    }
}

