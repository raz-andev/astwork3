package Ast.homework.controllers;

import Ast.homework.dto.UserDTO;
import Ast.homework.dto.UserEvent;
import Ast.homework.services.UserEventProducer;
import Ast.homework.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/user-service")
public class UserController {

    private final UserService userService;
    private final UserEventProducer userEventProducer;

    @Autowired
    public UserController(UserService userService, UserEventProducer userEventProducer) {
        this.userService = userService;
        this.userEventProducer = userEventProducer;
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return users != null && !users.isEmpty()
                ? ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(users) :
                ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable @Min(0) int id) {
        UserDTO dto = userService.findById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto);
    }

    @PostMapping("/add-user")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        UserDTO savedUser = userService.save(userDTO);

        return ResponseEntity.created(URI.create("api/v1/user-service/" + userDTO.getName()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id,
                                              @RequestBody @Valid UserDTO userDTO) {

        UserDTO updatedUser = userService.update(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable int id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

}
