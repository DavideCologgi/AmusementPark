package org.plantict.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class TicketAssignRequest extends TicketRequest{

    private UUID            idUser;
    private LocalDateTime   callingDate;
}
