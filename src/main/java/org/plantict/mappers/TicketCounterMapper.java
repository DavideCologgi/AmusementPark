package org.plantict.mappers;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.plantict.dtos.TicketCounterDTO;
import org.plantict.entities.TicketCounterEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketCounterMapper {

    TicketCounterMapper INSTANCE = Mappers.getMapper(TicketCounterMapper.class);

    @Named("toEntity")
    TicketCounterEntity toEntity(TicketCounterDTO dto);

    @Named("toDTO")
    TicketCounterDTO toDTO(TicketCounterEntity entity);

    @IterableMapping(qualifiedByName = "toEntity")
    List<TicketCounterEntity> toEntityList(List<TicketCounterDTO> dtos);

    @IterableMapping(qualifiedByName = "toDTO")
    List<TicketCounterDTO> toDTOList(List<TicketCounterEntity> entities);
}
