package Ast.homework.controllers;

import Ast.homework.dto.UserDTO;
import Ast.homework.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
class UserControllerUnitTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private UserController userController;

    @Test
    void getAllUsers_ReturnsValidResponseEntity() {
        var users = List.of(new UserDTO("test","test@test.com",25),
                new UserDTO("test2","test2@test.com",25));

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<UserDTO>> responseEntity = userController.getUsers();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(users, responseEntity.getBody());

        verify(userService, times(1)).getAllUsers();

    }

    @Test
    void getAllUsers_ReturnsNotFoundWhenNoUsers() {
        when(userService.getAllUsers())
                .thenThrow(new EntityNotFoundException("No users found in database!"));

        assertThrows(EntityNotFoundException.class, () -> {
            userController.getUsers();
        });

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getAllUsers_HandlesNullFromService() {
        when(userService.getAllUsers()).thenReturn(null);

        ResponseEntity<List<UserDTO>> response = userController.getUsers();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    void getUser_ReturnsValidUserWhenExists() {
        UserDTO mockUser = new UserDTO("test", "test@test.com", 25);
        when(userService.findById(1)).thenReturn(mockUser);

        ResponseEntity<UserDTO> response = userController.getUser(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        verify(userService, times(1)).findById(1);
    }

    @Test
    void getUser_ReturnsNoContentWhenNotExists() {
        when(userService.findById(99)).thenReturn(null);

        ResponseEntity<UserDTO> response = userController.getUser(99);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).findById(99);
    }

    @Test
    void getUser_ReturnsValidUserDTOStructure() {
        UserDTO mockUser = new UserDTO("valid", "valid@test.com", 30);
        when(userService.findById(1)).thenReturn(mockUser);

        ResponseEntity<UserDTO> response = userController.getUser(1);
        UserDTO responseUser = response.getBody();

        assertEquals("valid", responseUser.getName());
        assertEquals("valid@test.com", responseUser.getEmail());
        assertEquals(30, responseUser.getAge());
    }

    @Test
    void createUser_ReturnsCreatedUserWhenValid() {
        UserDTO inputUser = new UserDTO("test", "test@test.com", 25);
        UserDTO savedUser = new UserDTO("test", "test@test.com", 25);


        when(userService.save(inputUser)).thenReturn(savedUser);

        ResponseEntity<UserDTO> response = userController.createUser(inputUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedUser, response.getBody());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        verify(userService, times(1)).save(inputUser);
    }

}