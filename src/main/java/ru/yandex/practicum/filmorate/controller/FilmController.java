package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public Film get(@Valid @PathVariable int id) {
        log.info("Получен запрос GET /films/{}", id);
        return filmService.getFilmById(id);
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Получен запрос GET /films");
        return new ArrayList<>(filmService.readAll());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST /films");
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT /films");
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@Valid @PathVariable int id, @Valid @PathVariable int userId) {
        log.info("Получен запрос PUT /films/{}/like/{}", id, userId);
        filmService.addLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@Valid @PathVariable int id, @Valid @PathVariable int userId) {
        log.info("Получен запрос DELETE /films/{}/like/{}", id, userId);
        filmService.removeLike(userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@Valid @RequestParam(defaultValue = "10") int count) {
        log.info("Получен запрос GET /films/popular");
        return filmService.getPopularFilms(count);
    }

}
