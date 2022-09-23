package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Mpa {
    private int id;
    @NotBlank(message = "Название MPA не может быть пустым.")
    private String name;
    @NotBlank(message = "Описание MPA не может быть пустым.")
    private String description;

    public Mpa(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
