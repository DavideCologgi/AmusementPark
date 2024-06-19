package org.plantict.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {

    private UUID    idUser;

    private String  name;

    private String  email;
}
