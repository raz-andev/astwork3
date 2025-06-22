package Ast.homework.controllers;

import Ast.homework.dto.UserDTO;
import Ast.homework.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerMockTest {

    @MockitoBean
    private UserService userService;

    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final static Integer VALID_USER_ID = 1;
    private final static Integer INVALID_USER_ID = 999;
    private final static UserDTO USER_DTO = new UserDTO("test","test@test.com",22);
    private final static List<UserDTO> LIST = List.of(USER_DTO);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
    }

    @Test
    void getAllUsers_ShouldReturnValid() throws Exception {
        when(userService.getAllUsers()).thenReturn(LIST);

        mockMvc.perform(get("/api/v1/user-service/all-users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",is(1)))
                .andExpect(jsonPath("$[0].name",is(USER_DTO.getName())));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById_ShouldReturnValid() throws Exception {
        when(userService.findById(VALID_USER_ID)).thenReturn(USER_DTO);

        mockMvc.perform(get("/api/v1/user-service/{id}", VALID_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(USER_DTO.getName())));

        verify(userService, times(1)).findById(VALID_USER_ID);
    }

    @Test
    void getUserById_UserNotFound() throws Exception {
        when(userService.findById(INVALID_USER_ID)).thenReturn(null);

        mockMvc.perform(get("/api/v1/user-service/{id}", INVALID_USER_ID))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(INVALID_USER_ID);

    }

    @Test
    void createUser_ReturnValid() throws Exception {
        when(userService.save(USER_DTO)).thenReturn(USER_DTO);

         mockMvc.perform(post("/api/v1/user-service/add-user")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(objectMapper.writeValueAsString(USER_DTO)))
                 .andExpect(status().isCreated());

         verify(userService, times(1)).save(USER_DTO);
    }

    @Test
    void updateUser_ReturnValid() throws Exception {
        when(userService.update(VALID_USER_ID, USER_DTO)).thenReturn(USER_DTO);

        mockMvc.perform(patch("/api/v1/user-service/{id}", VALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(USER_DTO)))
                .andExpect(status().isOk());

        verify(userService, times(1)).update(VALID_USER_ID, USER_DTO);
    }

    @Test
    void deleteUser_ReturnValid() throws Exception {
        doNothing().when(userService).delete(VALID_USER_ID);

        mockMvc.perform(delete("/api/v1/user-service/{id}", VALID_USER_ID))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(VALID_USER_ID);
    }

}
