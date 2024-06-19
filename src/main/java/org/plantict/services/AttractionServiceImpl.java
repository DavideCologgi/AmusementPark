package org.plantict.services;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.plantict.dtos.AttractionDTO;
import org.plantict.entities.AttractionEntity;
import org.plantict.exceptions.AlreadyExistingException;
import org.plantict.exceptions.ExceptionHandler;
import org.plantict.exceptions.WrongParamsException;
import org.plantict.mappers.AttractionMapper;
import org.plantict.models.AttractionRequest;
import org.plantict.repositories.AttractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttractionServiceImpl implements AttractionService {

    @Autowired
    private AttractionRepository repository;

    private final Logger logger = LogManager.getLogger(ExceptionHandler.class);

    @Override
    public long totalAttractions() { return repository.count();}

    @Override
    public List<AttractionDTO> getAllAttractions(int page, int size) {
        logger.info("asked pagination of page {} and size {} ", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<AttractionEntity> attractionEntities = repository.findAll(pageable);
        return AttractionMapper.INSTANCE.toDTOList(attractionEntities.toList());
    }

    @Override
    public AttractionDTO getAttractionByName(String name) {
        AttractionEntity attraction = repository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("attraction with name " + name + " not found"));
        return AttractionMapper.INSTANCE.toDTO(attraction);
    }

    @Override
    @Transactional
    public AttractionDTO createAttraction(AttractionRequest request) throws AlreadyExistingException {
        if (request.getName() == null || request.getName().isBlank()) {
            logger.error("name cannot be null or empty");
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        if (request.getTicketDuration() == null || request.getTicketDuration() < 1) {
            logger.error("ticket duration cannot be null or smaller than 1");
            throw new IllegalArgumentException("ticket must be valid for at least 1 day");
        }
        if (request.getMaxTicket() == null || request.getMaxTicket() < 1) {
            logger.error("max ticket cannot be null or smaller than 1");
            throw new IllegalArgumentException("max ticket must be greater than 1");
        }
        AttractionEntity attraction = new AttractionEntity();
        Optional<AttractionEntity> attractionCheck = repository.findByName(request.getName());
        if (attractionCheck.isPresent()) {
            logger.error("attraction with name {} already exists", request.getName());
            throw new AlreadyExistingException("attraction with specified name already exists");
        }
        attraction.setName(request.getName());
        attraction.setDescription(request.getDescription());
        attraction.setDuration(request.getTicketDuration());
        attraction.setMaxTicket(request.getMaxTicket());
        attraction = repository.save(attraction);
        logger.info("new attraction created with id {}", attraction.getIdAttraction());
        return AttractionMapper.INSTANCE.toDTO(attraction);
    }

    @Override
    @Transactional
    public AttractionDTO editAttraction(UUID id, AttractionRequest request) throws AlreadyExistingException, WrongParamsException {
        AttractionEntity attractionToUpdate = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("attraction with specified id does not exist"));
        if (request.getTicketDuration() < 1) {
            logger.error("ticket must be valid at least 1 day");
            throw new WrongParamsException("ticket must be valid at least 1 day");
        }
        if (request.getMaxTicket() < 1) {
            logger.error("max ticket must be greater than 1");
            throw new WrongParamsException("max ticket must be greater than 1");
        }
        Optional<AttractionEntity> attractionCheck = repository.findByName(request.getName());
        if (attractionCheck.isPresent()) {
            logger.error("attraction with name {} already exists", request.getName());
            throw new AlreadyExistingException("attraction with specified name already exists");
        }
        if (request.getName() != null && !request.getName().isEmpty()) {
            attractionToUpdate.setName(request.getName());
        }
        if (request.getDescription() != null) {
            attractionToUpdate.setDescription(request.getDescription());
        }
        if (request.getTicketDuration() != null) {
            attractionToUpdate.setDuration(request.getTicketDuration());
        }
        if (request.getMaxTicket() != null) {
            attractionToUpdate.setMaxTicket(request.getMaxTicket());
        }
        attractionToUpdate = repository.save(attractionToUpdate);
        return AttractionMapper.INSTANCE.toDTO(attractionToUpdate);
    }

    @Override
    public void removeAttraction(UUID id) {
        Optional<AttractionEntity> attraction = repository.findById(id);
        if (attraction.isEmpty()) {
            logger.error("attraction with the specified id does not exist");
            throw new EntityNotFoundException("attraction with the specified id does not exist");
        }
        repository.deleteById(id);
    }
}
