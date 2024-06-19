package org.plantict.services;

import org.plantict.dtos.UserDTO;
import org.plantict.exceptions.AlreadyExistingException;
import org.plantict.models.UserRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {

    long            totalUsers();

    List<UserDTO>   getAllUsers(int page, int size);

    UserDTO         getUserByEmail(String email);

    UserDTO         createUser(UserRequest request) throws AlreadyExistingException;

    UserDTO         editUser(UUID id, UserRequest userRequest) throws AlreadyExistingException;

    void            removeUser(UUID id);
}
