package Ast.homework.mappers;

import Ast.homework.controllers.UserController;
import Ast.homework.dto.UserDTO;
import Ast.homework.models.UserModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDTO, UserModel> {

    public UserModel toModel(UserDTO user) {
        UserModel model = new UserModel(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge()
        );

        model.add(linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).getUsers()).withRel("all-users"));
        model.add(linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel("delete"));

        return model;
    }
}
