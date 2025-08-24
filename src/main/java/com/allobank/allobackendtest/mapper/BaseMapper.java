package com.allobank.allobackendtest.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * BaseMapper interface for mapping between entity, DTO, and response objects.
 *
 * @param <E>  the entity type
 * @param <Req> the request DTO type
 * @param <Res> the response type
 */
public interface BaseMapper<E,Req, Res> {
    // CREATE: map from DTO to entity
    E toEntity(Req request);

    // READ: map from entity to response
    Res toResponse(E entity);

    // READ: map from entity list to response list
    List<Res> toResponseList(List<E> entities);

    // CREATE: map from request list to entity list
    List<E> toEntityList(List<Req> dtos);

    // UPDATE (partial/patch): field null on DTO is ignored (not overwritten)
    void updateFromRequest(Req dto, @MappingTarget E entity);
}