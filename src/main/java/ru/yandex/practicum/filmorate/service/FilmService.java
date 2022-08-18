package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }
    public Map<Integer, Film> readAll() {
        return filmStorage.readAll();
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }
    public void addLike(int userId, int filmId) {
        if (userStorage.readAll().containsKey(userId) && readAll().containsKey(filmId)) {
            readAll().get(filmId).getLikes().add(userId);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    public void removeLike(int userId, int filmId) {
        if (userStorage.readAll().containsKey(userId) && readAll().containsKey(filmId)) {
            readAll().get(filmId).getLikes().remove(userId);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    public List<Film> getTop10Films() {
        return readAll().values().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
    }
}
