package org.plantict.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.plantict.dtos.AttractionDTO;
import org.plantict.exceptions.AlreadyExistingException;
import org.plantict.exceptions.ExceptionHandler;
import org.plantict.exceptions.WrongParamsException;
import org.plantict.models.AttractionRequest;
import org.plantict.models.SearchContainer;
import org.plantict.models.Sort;
import org.plantict.services.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/attraction")
public class AttractionController {

    @Autowired
    private AttractionService service;

    final private Logger logger = LogManager.getLogger(ExceptionHandler.class);

    @GetMapping("/list")
    public ResponseEntity<SearchContainer<AttractionDTO>> getAllAttractionsPage(@RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "10", name = "size") int size) {
        logger.info("GET/list call for attraction entity");
        List<AttractionDTO> attractionDTOList = service.getAllAttractions(page, size);
        logger.info("GET successful");
        long count = service.totalAttractions();
        Sort sort = new Sort();
        sort.setField("type");
        sort.setOrder("desc");
        SearchContainer<AttractionDTO> list = new SearchContainer<>(count, page, size, sort, attractionDTOList);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{name}")
    public ResponseEntity<AttractionDTO> getByName(@PathVariable("name") String name) {
        logger.info("GET call by name for attraction entity");
        AttractionDTO attractionDTO = service.getAttractionByName(name);
        logger.info("GET successful");
        return new ResponseEntity<>(attractionDTO, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<AttractionDTO> postAttraction(@RequestBody AttractionRequest request) throws AlreadyExistingException {
        logger.info("POST call for attraction entity");
        AttractionDTO attractionDTO = service.createAttraction(request);
        logger.info("POST successful. Result: {}", attractionDTO);
        return new ResponseEntity<>(attractionDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttractionDTO> updateAttraction(@PathVariable("id") UUID id, @RequestBody AttractionRequest request) throws AlreadyExistingException, WrongParamsException {
        logger.info("PUT call for attraction entity");
        AttractionDTO attractionDTO = service.editAttraction(id, request);
        logger.info("PUT successful. Result: {}", attractionDTO);
        return new ResponseEntity<>(attractionDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteAttraction(@PathVariable("id") UUID id) {
        logger.info("DELETE call for attraction entity");
        service.removeAttraction(id);
        logger.info("DELETE successful");
    }
}
