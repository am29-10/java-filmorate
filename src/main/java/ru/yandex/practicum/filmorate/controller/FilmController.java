package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoMovieFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() throws NoMovieFoundException {
        if (!filmService.readAll().isEmpty()) {
            log.info("Количество имеющихся фильмов: {}", filmService.readAll().size());
            return new ArrayList<>(filmService.readAll().values());
        } else {
            log.info("NoMovieFoundException (Пустой список фильмов)");
            throw new NoMovieFoundException("Список фильмов пуст");
        }
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        validate(film);
        filmService.create(film);
        log.info("Фильм с id '{}' добавлен в список", film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        validate(film);
        filmService.update(film);
        log.info("Фильм с id '{}' обновлен", film.getId());
        return film;

    }

    public void validate(Film film) throws ValidationException {
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            log.info("ValidationException (Пустое название фильма)");
            throw new ValidationException("Пустое название фильма");
        }
        if (film.getDescription().length() > 200) {
            log.info("ValidationException (Выход за пределы длины описания фильма)");
            throw new ValidationException("Выход за пределы длины описания фильма");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.info("ValidationException (Дата релиза раньше дня рождения кино)");
            throw new ValidationException("Дата релиза раньше дня рождения кино");
        }
        if (film.getDuration() < 0) {
            log.info("ValidationException (Продолжительность фильма имеет отрицательное значение)");
            throw new ValidationException("Продолжительность фильма имеет отрицательное значение");
        }
        if (film.getId() < 0) {
            log.info("ValidationException (Значение id не может быть отрицательным)");
            throw new ValidationException("Значение id не может быть отрицательным");
        }
    }
}
