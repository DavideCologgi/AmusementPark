package org.plantict.services;

import org.plantict.entities.TicketCounterEntity;
import org.plantict.repositories.TicketCounterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketCounterServiceImpl implements TicketCounterService {

    @Autowired
    private TicketCounterRepository repository;

    @Override
    @Transactional
    public synchronized Long getNextCounter(int numberOfTickets) {
        TicketCounterEntity ticketCounter = repository.getById(1L);
        Long currentCounter = ticketCounter.getCounter();
        ticketCounter.setCounter(currentCounter + (long)numberOfTickets);
        ticketCounter = repository.save(ticketCounter);
        return currentCounter;
    }
}
