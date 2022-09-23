package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {

    Genre create(Genre genre);

    List<Genre> readAll();

    Genre update(Genre genre);

    Genre getGenreById(int id);
}
