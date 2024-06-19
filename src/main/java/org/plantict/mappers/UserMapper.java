package org.plantict.mappers;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.plantict.dtos.UserDTO;
import org.plantict.entities.UserEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Named("toEntity")
    UserEntity toEntity(UserDTO dto);

    @Named("toDTO")
    UserDTO toDTO(UserEntity entity);

    @IterableMapping(qualifiedByName = "toEntity")
    List<UserEntity> toEntityList(List<UserDTO> dtos);

    @IterableMapping(qualifiedByName = "toDTO")
    List<UserDTO> toDTOList(List<UserEntity> entities);
}
