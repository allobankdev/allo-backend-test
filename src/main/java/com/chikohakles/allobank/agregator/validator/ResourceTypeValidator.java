package com.chikohakles.allobank.agregator.validator;

import com.chikohakles.allobank.agregator.constant.ResourceType;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ResourceTypeValidator {
    public void validate(String resourceType) {
        if (StringUtils.isBlank(resourceType)) {
            throw new IllegalArgumentException(resourceType + " is not a valid resource type.");
        }

        //the .fromCode() will always throw an exception if there's no enum matched with given code
        ResourceType.fromCode(resourceType);
    }
}
