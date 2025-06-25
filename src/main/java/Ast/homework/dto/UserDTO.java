package Ast.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO пользователя")
public class UserDTO implements Serializable {

    @NotEmpty(message = "Поле name не должно быть пустое!")
    @NotBlank
    @Schema(description = "Имя пользователя", example = "Иван")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",message = "Неверный формат mail!")
    @NotEmpty(message = "Поле email не должно быть пустое!")
    @Schema(description = "Email пользователя",example = "test@test.com")
    private String email;

    @Min(value = 1,message = "Возраст должен быть положительным!")
    @Schema(description = "Возраст пользователя", example = "25", minimum = "1")
    private int age;


}
