package org.plantict.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TicketDTO {

    private UUID            idTicket;

    private UserDTO         user;

    private AttractionDTO   attraction;

    private Long            number;

    private LocalDateTime   emissionDate;

    private LocalDateTime   expirationDate;

    private LocalDateTime   callingDate;
}
