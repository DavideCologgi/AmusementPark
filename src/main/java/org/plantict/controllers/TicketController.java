package org.plantict.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.plantict.dtos.TicketDTO;
import org.plantict.exceptions.AlreadyExistingException;
import org.plantict.exceptions.ExceptionHandler;
import org.plantict.exceptions.FullBookingException;
import org.plantict.models.SearchContainer;
import org.plantict.models.Sort;
import org.plantict.models.TicketAssignRequest;
import org.plantict.models.TicketRequest;
import org.plantict.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    private final Logger logger = LogManager.getLogger(ExceptionHandler.class);

    @GetMapping("/list")
    public ResponseEntity<SearchContainer<TicketDTO>> getAllTicketsPage(@RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "10", name = "size") int size) {
        logger.info("GET/list call for ticket entity");
        List<TicketDTO> ticketDTOList = ticketService.getAllTickets(page, size);
        logger.info("GET successful");
        long count = ticketService.totalTickets();
        Sort sort = new Sort();
        sort.setField("type");
        sort.setOrder("desc");
        SearchContainer<TicketDTO> list = new SearchContainer<>(count, page, size, sort, ticketDTOList);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{number}")
    public ResponseEntity<TicketDTO> getByNumber(@PathVariable("number") Long number) {
        logger.info("GET call by number for ticket entity");
        TicketDTO ticketDTO = ticketService.getTicketByNumber(number);
        logger.info("GET successful");
        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TicketDTO> postTicket(@RequestBody TicketRequest request) throws AlreadyExistingException {
        logger.info("POST call for ticket entity");
        TicketDTO ticketDTO = ticketService.createTicket(request);
        logger.info("POST successful. Result: {}", ticketDTO);
        return new ResponseEntity<>(ticketDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable("id")UUID id) {
        logger.info("DELETE call for ticket entity");
        ticketService.removeTicket(id);
        logger.info("DELETE successful");
    }

    @PutMapping("/assign")
    public ResponseEntity<TicketDTO> assignTicketToUser(@RequestBody TicketAssignRequest request) throws FullBookingException, AlreadyExistingException {
        logger.info("Assign request for attraction with id {} from user with id {}", request.getIdAttraction(), request.getIdUser());
        TicketDTO ticketDTO = ticketService.assignTicket(request);
        logger.info("assign completed for user with id {}", request.getIdUser());
        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @GetMapping("/validate")
    public String validateTicket(@RequestParam(name = "idTicket") UUID id) {
        logger.info("Requested validation for ticket with id {}", id);
        boolean check = ticketService.validate(id);
        if (check) {
            return "Ticket with id " + id + " is still valid";
        } else {
            return "Ticket with id " + id + " is expired";
        }
    }
}

