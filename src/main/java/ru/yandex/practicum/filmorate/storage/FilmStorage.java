package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Film create(Film film);

    Map<Integer, Film> readAll();

    Film update(Film film);

    Film getFilmById(int id);

    List<Film> getPopularFilms(int count);
}
