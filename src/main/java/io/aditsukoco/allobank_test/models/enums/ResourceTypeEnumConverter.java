package io.aditsukoco.allobank_test.models.enums;

import io.aditsukoco.allobank_test.exceptions.BadRequestRestException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ResourceTypeEnumConverter implements Converter<String, ResourceTypeEnum> {
    @Override
    public ResourceTypeEnum convert(String source) {
        for (ResourceTypeEnum typeEnum : ResourceTypeEnum.values()) {
            if (typeEnum.getValue().equals(source)) {
                return typeEnum;
            }
        }
        throw new BadRequestRestException("unknown value for \""+source+"\"");
    }
}
