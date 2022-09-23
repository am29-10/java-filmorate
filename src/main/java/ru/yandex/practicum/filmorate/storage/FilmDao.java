package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {

    Film create(Film film);

    List<Film> readAll();

    Film update(Film film);

    Film getFilmById(int id);

}
