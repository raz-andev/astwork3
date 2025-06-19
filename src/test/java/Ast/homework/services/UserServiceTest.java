package Ast.homework.services;

import Ast.homework.dto.UserDTO;
import Ast.homework.models.User;
import Ast.homework.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserEventProducer userEventProducer;

    @Autowired
    private UserService userService;

    private final static Integer VALID_USER_ID = 1;
    private final static Integer INVALID_USER_ID = 999;
    private final static User VALID_USER = new User("test","test@test.com",25);
    private final static UserDTO VALID_USER_DTO = new UserDTO("test","test@test.com",25);

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(VALID_USER));

        List<UserDTO> users = userService.getAllUsers();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals(VALID_USER_DTO.getName(), users.getFirst().getName());
        assertEquals(VALID_USER_DTO.getEmail(), users.getFirst().getEmail());
        assertEquals(VALID_USER_DTO.getAge(), users.getFirst().getAge());

        verify(userRepository,times(1)).findAll();
    }

    @Test
    void getUserByIdValidUser() {
        when(userRepository.findById(VALID_USER_ID)).thenReturn(Optional.of(VALID_USER));

        UserDTO dto = userService.findById(VALID_USER_ID);

        assertNotNull(dto);
        assertEquals(VALID_USER_DTO.getName(), dto.getName());
        assertEquals(VALID_USER_DTO.getEmail(), dto.getEmail());
        assertEquals(VALID_USER_DTO.getAge(), dto.getAge());

        verify(userRepository,times(1)).findById(VALID_USER_ID);
    }

    @Test
    void getUserByIdInvalidUser() {
        when(userRepository.findById(INVALID_USER_ID)).thenReturn(Optional.empty());

        EntityNotFoundException ex =
                assertThrows(EntityNotFoundException.class, () -> userService.findById(INVALID_USER_ID));

        assertEquals("User not found", ex.getMessage());

        verify(userRepository,times(1)).findById(INVALID_USER_ID);
    }

    @Test
    void saveUserValidCreate() {
        when(userRepository.save(VALID_USER)).thenReturn(VALID_USER);

        UserDTO dto = userService.save(VALID_USER_DTO);

        assertNotNull(dto);
        assertEquals(VALID_USER_DTO.getName(), dto.getName());
        assertEquals(VALID_USER_DTO.getEmail(), dto.getEmail());
        assertEquals(VALID_USER_DTO.getAge(), dto.getAge());

        verify(userRepository,times(1)).save(VALID_USER);
        verify(userEventProducer).publishUserEvent(
                eq("CREATED"),
                eq(VALID_USER.getEmail())
        );
    }

    @Test
    void updateUserValidUpdate() {
        when(userRepository.findById(VALID_USER_ID)).thenReturn(Optional.of(VALID_USER));

        userService.update(VALID_USER_ID, VALID_USER_DTO);

        verify(userRepository,times(1)).findById(VALID_USER_ID);
    }

    @Test
    void updateUserInvalidUpdate() {
            when(userRepository.findById(INVALID_USER_ID)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                    userService.update(INVALID_USER_ID, VALID_USER_DTO));

            assertEquals("User not found", exception.getMessage());

            verify(userRepository, times(1)).findById(INVALID_USER_ID);
    }

    @Test
    void deleteUserValidDelete() {
        when(userRepository.findById(VALID_USER_ID)).thenReturn(Optional.of(VALID_USER));

        userService.delete(VALID_USER_ID);

        verify(userRepository,times(1)).findById(VALID_USER_ID);
        verify(userEventProducer).publishUserEvent(
                eq("DELETED"),
                eq(VALID_USER.getEmail())
        );
    }

    @Test
    void deleteUserInvalidDelete() {
        when(userRepository.findById(INVALID_USER_ID)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> userService.delete(INVALID_USER_ID));
        assertEquals("User not found", ex.getMessage());

        verify(userRepository,times(1)).findById(INVALID_USER_ID);
    }


}
