package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmLikeDao {

    void create(int userId, int filmId);

    List<User> readLikesByFilmId(int filmId);

    List<Film> getPopularFilms(int count);

    void delete(int userId, int filmId);
}
