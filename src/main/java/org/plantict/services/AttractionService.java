package org.plantict.services;

import org.plantict.dtos.AttractionDTO;
import org.plantict.exceptions.AlreadyExistingException;
import org.plantict.exceptions.WrongParamsException;
import org.plantict.models.AttractionRequest;

import java.util.List;
import java.util.UUID;

public interface AttractionService {

    long                totalAttractions();

    List<AttractionDTO> getAllAttractions(int page, int size);

    AttractionDTO       getAttractionByName(String name);

    AttractionDTO       createAttraction(AttractionRequest request) throws AlreadyExistingException;

    AttractionDTO       editAttraction(UUID id, AttractionRequest request) throws AlreadyExistingException, WrongParamsException;

    void                removeAttraction(UUID id);
}
