package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController(new FilmServiceImpl());
        film = new Film();
        film.setName("Властелин колец");
        film.setDescription("Фантастический фильм, представляющий экранизацию романа");
        film.setReleaseDate(LocalDate.of(2001, 12, 19));
        film.setDuration(178);
    }

    @Test
    void validateNameTest() {
        film.setName(" ");
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Пустое название фильма", exception.getMessage());
    }

    @Test
    void validateDescriptionTest() {
        film.setDescription("a".repeat(201));
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Выход за пределы длины описания фильма", exception.getMessage());
    }

    @Test
    void validateReleaseDateTest() {
        film.setReleaseDate(LocalDate.of(1895, 12, 20));
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Дата релиза раньше дня рождения кино", exception.getMessage());
    }

    @Test
    void validateDurationTest() {
        film.setDuration(-2);
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Продолжительность фильма имеет отрицательное значение", exception.getMessage());
    }

    @Test
    void validateIdTest() {
        film.setId(-1);
        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(film));
        assertEquals("Значение id не может быть отрицательным", exception.getMessage());
    }
}
