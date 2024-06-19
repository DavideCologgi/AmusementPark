package org.plantict.services;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.plantict.dtos.TicketDTO;
import org.plantict.entities.AttractionEntity;
import org.plantict.entities.TicketCounterEntity;
import org.plantict.entities.TicketEntity;
import org.plantict.entities.UserEntity;
import org.plantict.exceptions.AlreadyExistingException;
import org.plantict.exceptions.ExceptionHandler;
import org.plantict.exceptions.FullBookingException;
import org.plantict.mappers.TicketMapper;
import org.plantict.models.TicketAssignRequest;
import org.plantict.models.TicketRequest;
import org.plantict.repositories.AttractionRepository;
import org.plantict.repositories.TicketRepository;
import org.plantict.repositories.UserRepository;
import org.plantict.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttractionRepository attractionRepository;

    @Autowired
    private TicketCounterService ticketCounterService;

    private final Logger logger = LogManager.getLogger(ExceptionHandler.class);

    @Override
    public long totalTickets() { return ticketRepository.count();}

    @Override
    public List<TicketDTO> getAllTickets(int page, int size) {
        logger.info("asked pagination of page {} and size {} ", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<TicketEntity> ticketEntities = ticketRepository.findAll(pageable);
        return TicketMapper.INSTANCE.toDTOList(ticketEntities.toList());
    }

    @Override
    public TicketDTO getTicketByNumber(Long number) {
        TicketEntity ticket = ticketRepository.findByNumber(number)
                .orElseThrow(() -> new EntityNotFoundException("ticket with number " + number + " not found"));
        return TicketMapper.INSTANCE.toDTO(ticket);
    }

    @Override
    @Transactional
    public TicketDTO createTicket(TicketRequest request) throws AlreadyExistingException {
        if (request.getIdAttraction() == null) {
            logger.error("id attraction cannot be null");
            throw new IllegalArgumentException("id attraction cannot be null");
        }
        TicketEntity ticket = new TicketEntity();
        AttractionEntity attraction = attractionRepository.findById(request.getIdAttraction())
                        .orElseThrow(() -> new EntityNotFoundException("attraction with specified id does not exist"));
        ticket.setAttraction(attraction);
        ticket.setNumber(ticketCounterService.getNextCounter(1) + 1);
        ticket.setEmissionDate(Utils.getCurrentTimestamp().toLocalDateTime());
        ticket.setExpirationDate(ticket.getEmissionDate().plusDays(attraction.getDuration()));
        ticket = ticketRepository.save(ticket);
        logger.info("new ticket created with id {}", ticket.getIdTicket());
        return TicketMapper.INSTANCE.toDTO(ticket);
    }

    @Override
    public void removeTicket(UUID id) {
       Optional<TicketEntity> ticket = ticketRepository.findById(id);
       if (ticket.isEmpty()) {
           logger.error("ticket with id {} does not exist", id);
           throw new EntityNotFoundException("ticket with specified id does not exist");
       }
       ticketRepository.deleteById(id);
    }

    @Override
    public boolean validate(UUID id) {
        TicketEntity ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("no ticket found with id " + id));
        return checkValidity(ticket);
    }

    @Override
    public boolean checkValidity(TicketEntity ticket) {
        return !ticket.getExpirationDate().isBefore(Utils.getCurrentTimestamp().toLocalDateTime());
    }

    @Scheduled(cron = "0 * * * * ?")
    //@Scheduled(cron = " 0 0 0 * * ?")
    @Transactional
    public void generateTickets() {
        logger.info("Starting generation for new tickets...");
        List<TicketEntity> ticketEntities = new ArrayList<>();
        List<AttractionEntity> attractionEntities = attractionRepository.findAll();

        int totalTicketsToGenerate = attractionEntities.stream().mapToInt(AttractionEntity::getMaxTicket).sum();
        Long counter = ticketCounterService.getNextCounter(totalTicketsToGenerate);

        for (AttractionEntity attraction : attractionEntities) {
            for (int i = 0; i < attraction.getMaxTicket(); i++) {
                TicketEntity ticket = new TicketEntity();
                ticket.setAttraction(attraction);
                ticket.setEmissionDate(Utils.getCurrentTimestamp().toLocalDateTime());
                ticket.setExpirationDate(ticket.getEmissionDate().plusDays(attraction.getDuration()));
                counter++;
                ticket.setNumber(counter);
                ticketEntities.add(ticket);
            }
        }
        ticketRepository.saveAll(ticketEntities);
        logger.info("new tickets successfully generated");
    }

    @Override
    @Transactional
    public TicketDTO assignTicket(TicketAssignRequest request) throws FullBookingException, AlreadyExistingException {
        AttractionEntity attraction = attractionRepository.findById(request.getIdAttraction())
                .orElseThrow(() -> new EntityNotFoundException("attraction with specified id does not exist"));
        UserEntity user = userRepository.findById(request.getIdUser())
                .orElseThrow(() -> new EntityNotFoundException("user with specified id does not exist"));
        List<TicketEntity> ticketFound = ticketRepository.findByAttractionAndUser(attraction, user);
        if (!ticketFound.isEmpty() && ticketFound.get(0).getExpirationDate().isAfter(Utils.getCurrentTimestamp().toLocalDateTime())) {
            logger.error("user {} already has a valid ticket for {}", user.getName(), attraction.getName());
            throw new AlreadyExistingException(user.getName() + " already has a valid ticket for " + attraction.getName());
        }
        if (request.getCallingDate().isBefore(Utils.getCurrentTimestamp().toLocalDateTime())) {
            logger.error("wrong param in input for calling date");
            throw new IllegalArgumentException("wrong param for calling date");
        }
        List<TicketEntity> availableTickets = ticketRepository.findByAttractionAndUser(attraction, null);
        availableTickets.removeIf(ticket -> !checkValidity(ticket));
        availableTickets.removeIf(ticket -> request.getCallingDate().isAfter(ticket.getExpirationDate()));
        if (availableTickets.isEmpty()) {
            logger.error("no ticket available at the moment for attraction {}", attraction.getName());
            throw new FullBookingException("no ticket available at the moment for attraction " + attraction.getName());
        } else {
            for (TicketEntity ticket : availableTickets) {
                if (ticket.getExpirationDate().isAfter(Utils.getCurrentTimestamp().toLocalDateTime())) {
                    ticket.setUser(user);
                    ticket.setCallingDate(request.getCallingDate());
                    break;
                } else if (!availableTickets.iterator().hasNext()) {
                    logger.error("no ticket available at the moment for attraction {}", attraction.getName());
                    throw new FullBookingException("no ticket available at the moment for attraction " + attraction.getName());
                }
            }
            logger.info("ticket {} for {} assigned to user {}", availableTickets.get(0).getNumber(), attraction.getName(), user.getName());
        }
        availableTickets = ticketRepository.saveAll(availableTickets);
        return TicketMapper.INSTANCE.toDTO(availableTickets.get(0));
    }

    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void deleteExpiredTickets() {
        logger.info("Deleting all expired tickets...");
        List<TicketEntity> ticketEntityList = ticketRepository.findAll();
        for (TicketEntity ticket : ticketEntityList) {
            if (ticket.getExpirationDate().isBefore(Utils.getCurrentTimestamp().toLocalDateTime())) {
                ticketRepository.deleteById(ticket.getIdTicket());
            }
        }
        logger.info("expired tickets deleted successfully");
    }
}
