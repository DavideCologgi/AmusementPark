package org.plantict.services;

import org.plantict.dtos.TicketDTO;
import org.plantict.entities.TicketEntity;
import org.plantict.exceptions.AlreadyExistingException;
import org.plantict.exceptions.FullBookingException;
import org.plantict.models.TicketAssignRequest;
import org.plantict.models.TicketRequest;

import java.util.List;
import java.util.UUID;

public interface TicketService {

    long            totalTickets();

    List<TicketDTO> getAllTickets(int page, int size);

    TicketDTO       getTicketByNumber(Long number);

    TicketDTO       createTicket(TicketRequest request) throws AlreadyExistingException;

    void            removeTicket(UUID id);

    void            generateTickets();

    void            deleteExpiredTickets();

    boolean         checkValidity(TicketEntity ticket);

    boolean         validate(UUID id);

    TicketDTO       assignTicket(TicketAssignRequest request) throws FullBookingException, AlreadyExistingException;
}
