package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {

    private int id;
    @NotBlank(message = "email не может быть пустым.")
    @Email
    private String email;
    @NotBlank(message = "логин не может быть пустым.")
    private String login;
    private String name;
    @NotNull(message = "Дата рождения не может быть пустой.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}
