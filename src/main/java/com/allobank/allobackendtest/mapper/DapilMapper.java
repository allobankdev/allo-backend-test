package com.allobank.allobackendtest.mapper;

import com.allobank.allobackendtest.dto.DapilDto;
import com.allobank.allobackendtest.entity.DapilEntity;
import com.allobank.allobackendtest.model.Dapil;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for Dapil (Electoral District) entities.
 */
@Mapper(componentModel = "spring")
public interface DapilMapper extends BaseMapper<DapilEntity, DapilDto, Dapil> {
    DapilMapper INSTANCE = Mappers.getMapper(DapilMapper.class);
    // CREATE request mapping
    @Override
    @Mapping(target = "id", ignore=true)
    DapilEntity toEntity(DapilDto dto);

    // READ request mapping
    @Override
    Dapil toResponse(DapilEntity entity);

    // UPDATE request mapping
    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(DapilDto dto, @MappingTarget DapilEntity entity);

}