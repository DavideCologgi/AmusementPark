package org.plantict.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.plantict.dtos.UserDTO;
import org.plantict.exceptions.AlreadyExistingException;
import org.plantict.models.SearchContainer;
import org.plantict.models.Sort;
import org.plantict.models.UserRequest;
import org.plantict.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    private final Logger logger = LogManager.getLogger(ExceptionHandler.class);

    @GetMapping("/list")
    public ResponseEntity<SearchContainer<UserDTO>> getAllUsersPage(@RequestParam(defaultValue = "0", name = "page") int page, @RequestParam(defaultValue = "10", name = "size") int size) {
        logger.info("GET/list call for user entity");
        List<UserDTO> userPages = userService.getAllUsers(page, size);
        logger.info("GET successful");
        long count = userService.totalUsers();
        Sort sort = new Sort();
        sort.setField("type");
        sort.setOrder("desc");
        SearchContainer<UserDTO> list = new SearchContainer<>(count, page, size, sort, userPages);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getByEmail(@PathVariable("email") String email) {
        logger.info("GET call for user with email {}", email);
        UserDTO userDTO = userService.getUserByEmail(email);
        logger.info("GET successful. result: {}", userDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UserDTO> postUser(@RequestBody UserRequest userRequest) throws AlreadyExistingException {
        logger.info("POST call for user entity");
        UserDTO createdUser = userService.createUser(userRequest);
        logger.info("POST successful. result: {}", createdUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") UUID id, @RequestBody UserRequest request) throws AlreadyExistingException{
        logger.info("UPDATE call for user with id {}", id);
        UserDTO updatedUser = userService.editUser(id, request);
        logger.info("UPDATE successful. result: {}", updatedUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") UUID id) {
        logger.info("DELETE call for user with id {}", id);
        userService.removeUser(id);
        logger.info("DELETE successful");
        return ResponseEntity.noContent().build();
    }
}