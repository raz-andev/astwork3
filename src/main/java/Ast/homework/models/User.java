package Ast.homework.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Поле name не должно быть пустое!")
    @NotBlank
    private String name;

    @Column(name = "email")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",message = "Неверный формат mail!")
    @NotEmpty(message = "Поле email не должно быть пустое!")
    private String email;

    @Min(value = 1,message = "Возраст должен быть положительным!")
    @Column(name = "age")
    private int age;

    @Column(name = "created_at",updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime create;

    public User(String name, String email, int age, LocalDateTime create) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.create = create;
    }

    public User(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    @PrePersist
    public void onCreate(){
        create = LocalDateTime.now();
    }
}
