package org.plantict.mappers;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.plantict.dtos.TicketDTO;
import org.plantict.entities.TicketEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    TicketMapper INSTANCE = Mappers.getMapper(TicketMapper.class);

    @Named("toEntity")
    TicketEntity toEntity(TicketDTO dto);

    @Named("toDTO")
    TicketDTO toDTO(TicketEntity entity);

    @IterableMapping(qualifiedByName = "toEntity")
    List<TicketEntity> toEntityList(List<TicketDTO> dtos);

    @IterableMapping(qualifiedByName = "toDTO")
    List<TicketDTO> toDTOList(List<TicketEntity> entities);
}
