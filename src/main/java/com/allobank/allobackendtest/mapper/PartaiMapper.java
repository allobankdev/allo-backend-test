package com.allobank.allobackendtest.mapper;

import com.allobank.allobackendtest.dto.PartaiDto;
import com.allobank.allobackendtest.entity.PartaiEntity;
import com.allobank.allobackendtest.model.Partai;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PartaiMapper extends BaseMapper<PartaiEntity, PartaiDto, Partai> {


    @Override
    @Mapping(target = "id", ignore = true)
    PartaiEntity toEntity(PartaiDto dto);

    @Override
    Partai toResponse(PartaiEntity entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(PartaiDto dto, @MappingTarget PartaiEntity entity);
}
