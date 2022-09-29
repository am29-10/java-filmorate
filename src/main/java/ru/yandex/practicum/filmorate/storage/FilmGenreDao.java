package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmGenreDao {


    void create(int filmId, int genreId);

    List<Genre> readGenresByFilmId(int filmId);

    void delete(int filmId);
}
