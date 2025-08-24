package com.allobank.allobackendtest.mapper;

import com.allobank.allobackendtest.dto.CalegDto;
import com.allobank.allobackendtest.entity.CalegEntity;
import com.allobank.allobackendtest.model.Caleg;
import org.mapstruct.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", uses = {DapilMapper.class, PartaiMapper.class})
public interface CalegMapper extends BaseMapper<CalegEntity, CalegDto, Caleg> {

    CalegMapper INSTANCE = Mappers.getMapper(CalegMapper.class);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dapil", ignore = true) 
    @Mapping(target = "partai", ignore = true)
    CalegEntity toEntity(CalegDto dto);

    @Override
    @Mapping(target = "dapil", source = "dapil")
    @Mapping(target = "partai", source = "partai")
    Caleg toResponse(CalegEntity entity);

    @Override
    @Mapping(target = "dapil", ignore = true)
    @Mapping(target = "partai", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(CalegDto dto, @MappingTarget CalegEntity entity);
}



