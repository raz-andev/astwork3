package Ast.homework.services;

import Ast.homework.dto.UserDTO;
import Ast.homework.dto.UserEvent;
import Ast.homework.mappers.UserMapper;
import Ast.homework.models.User;
import Ast.homework.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEventProducer userEventProducer;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, UserEventProducer userEventProducer) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userEventProducer = userEventProducer;
    }

    public List<UserDTO> getAllUsers() {
        log.info("Run finding all users method");
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new EntityNotFoundException("No users found in database!");
        }

        return users.
                stream().
                map(userMapper::toDTO).
                collect(Collectors.toList());
    }

    public UserDTO findById(int id) {
        log.info("Run finding user method");

        return userRepository.findById(id).
                map(userMapper::toDTO).
                orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional
    public UserDTO save(UserDTO dto) {
        log.info("Run saving user method");
        userEventProducer.sendMessage(new UserEvent("CREATED", dto.getEmail()));

        return userMapper.toDTO(userRepository.save(userMapper.toEntity(dto)));

    }

    @Transactional
    public void delete(int id) {
        log.info("Run deleting user method");
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        userEventProducer.sendMessage(new UserEvent("DELETED", user.getEmail()));

        userRepository.deleteById(user.getId());
    }

    @Transactional
    public UserDTO update(Integer id, UserDTO userDto) {
        log.info("Run updating user method");
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }
        if (userDto.getAge() != 0) {
            existingUser.setAge(userDto.getAge());
        }

        User updatedUser = userRepository.save(existingUser);

        return userMapper.toDTO(updatedUser);
    }
}
