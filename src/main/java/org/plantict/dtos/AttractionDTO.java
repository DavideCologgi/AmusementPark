package org.plantict.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class AttractionDTO {

    private UUID    idAttraction;

    private String  name;

    private String  description;

    private Integer duration;

    private Integer maxTicket;
}
