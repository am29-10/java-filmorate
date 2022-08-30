package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final FilmService filmService;

    @Autowired
    public InMemoryFilmStorage(FilmService filmService) {
        this.filmService = filmService;
    }
    @Override
    public Film create(Film film) {
        return filmService.create(film);
    }
    @Override
    public Map<Integer, Film> readAll() {
        return filmService.readAll();
    }
    @Override
    public Film update(Film film) {
        return filmService.update(film);
    }
    @Override
    public Film getFilmById(int id) {
        return filmService.getFilmById(id);
    }
    @Override
    public List<Film> getPopularFilms(int count) {
        return filmService.getPopularFilms(count);
    }


}
