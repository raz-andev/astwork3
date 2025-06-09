package Ast.homework.mappers;


import Ast.homework.dto.UserDTO;
import Ast.homework.models.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

@Component
public class UserMapper {

    @Autowired
    private ModelMapper mapper;

    public UserDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }
        UserDTO dto = mapper.map(entity, UserDTO.class);
        System.out.println("Mapping User to DTO:");
        System.out.println("Entity: " + entity);
        System.out.println("DTO: " + dto);
        return dto;
    }

    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User entity = mapper.map(userDTO, User.class);
        System.out.println("Mapping DTO to User:");
        System.out.println("DTO: " + userDTO);
        System.out.println("Entity: " + entity);
        return entity;
    }

}
