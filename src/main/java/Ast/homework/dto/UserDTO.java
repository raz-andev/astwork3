package Ast.homework.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDTO implements Serializable {

    @NotEmpty(message = "Поле name не должно быть пустое!")
    @NotBlank
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",message = "Неверный формат mail!")
    @NotEmpty(message = "Поле email не должно быть пустое!")
    private String email;

    @Min(value = 1,message = "Возраст должен быть положительным!")
    private int age;


}
