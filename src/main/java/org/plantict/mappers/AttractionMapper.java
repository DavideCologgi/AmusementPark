package org.plantict.mappers;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.plantict.dtos.AttractionDTO;
import org.plantict.entities.AttractionEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttractionMapper {

    AttractionMapper INSTANCE = Mappers.getMapper(AttractionMapper.class);

    @Named("toEntity")
    AttractionEntity toEntity(AttractionDTO dto);

    @Named("toDTO")
    AttractionDTO toDTO(AttractionEntity entity);

    @IterableMapping(qualifiedByName = "toEntity")
    List<AttractionEntity> toEntityList(List<AttractionDTO> dtos);

    @IterableMapping(qualifiedByName = "toDTO")
    List<AttractionDTO> toDTOList(List<AttractionEntity> entities);
}
