package org.plantict.services;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.plantict.dtos.UserDTO;
import org.plantict.entities.UserEntity;
import org.plantict.exceptions.AlreadyExistingException;
import org.plantict.exceptions.ExceptionHandler;
import org.plantict.mappers.UserMapper;
import org.plantict.models.UserRequest;
import org.plantict.repositories.UserRepository;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    private final Logger logger = LogManager.getLogger(ExceptionHandler.class);

    public long totalUsers() {
        return repository.count();
    }

    @Override
    public List<UserDTO> getAllUsers(int page, int size) {
        logger.info("asked pagination of page {} and size {} ", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> userEntities = repository.findAll(pageable);
        return UserMapper.INSTANCE.toDTOList(userEntities.toList());
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        UserEntity userEntity = repository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("user with email " + email + " not found"));
        return UserMapper.INSTANCE.toDTO(userEntity);
    }

    @Transactional
    @Override
    public UserDTO createUser(UserRequest request) throws AlreadyExistingException {
        if (request.getName() == null || request.getName().isEmpty()) {
            logger.error("name cannot be null or empty");
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            logger.error("email cannot be null or empty");
            throw new IllegalArgumentException("email cannot be null or empty");
        }
        UserEntity userEntity = new UserEntity();
        Optional<UserEntity> userCheck = repository.findByEmail(request.getEmail());
        if (userCheck.isPresent()) {
            logger.error("user with email {} already exists", request.getEmail());
            throw new AlreadyExistingException("user with the specified email already exists");
        }
        userEntity.setName(request.getName());
        userEntity.setEmail(request.getEmail());
        userEntity = repository.save(userEntity);
        logger.info("user created with id {}", userEntity.getIdUser());
        return UserMapper.INSTANCE.toDTO(userEntity);
    }

    @Transactional
    @Override
    public UserDTO editUser(UUID id, UserRequest request) throws AlreadyExistingException {
        UserEntity userToUpdate = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with specified id does not exist"));
        Optional<UserEntity> userCheck = repository.findByEmail(request.getEmail());
        if (userCheck.isPresent()) {
            logger.error("user with email {} already exists", request.getEmail());
            throw new AlreadyExistingException("user with specified email already exists");
        }
        if (request.getName() != null && !request.getName().isEmpty())
            userToUpdate.setName(request.getName());
        if (request.getEmail() != null && !request.getEmail().isEmpty())
            userToUpdate.setEmail(request.getEmail());
        userToUpdate = repository.save(userToUpdate);
        return UserMapper.INSTANCE.toDTO(userToUpdate);
    }

    @Override
    public void removeUser(UUID id) {
        Optional<UserEntity> userEntity = repository.findById(id);
        if (userEntity.isEmpty()) {
            logger.error("no user found with specified id");
            throw new EntityNotFoundException("No user found with specified id");
        }
        repository.deleteById(id);
    }
}
