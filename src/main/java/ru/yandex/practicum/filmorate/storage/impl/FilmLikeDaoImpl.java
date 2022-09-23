package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmLikeDao;

import java.util.List;

@Component
public class FilmLikeDaoImpl implements FilmLikeDao {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmLikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(int userId, int filmId) {
        jdbcTemplate.update("INSERT INTO LIKES (USER_ID, FILM_ID) VALUES (?, ?)",
                userId, filmId);
    }

    @Override
    public List<User> readLikesByFilmId(int filmId) {
        return jdbcTemplate.query("SELECT * FROM (SELECT USER_ID " +
                        "FROM LIKES " +
                        "WHERE FILM_ID = ?) AS USERS_LIKE_BY_FILM " +
                        "RIGHT JOIN USERS ON USERS_LIKE_BY_FILM.USER_ID = USERS.ID",
                new BeanPropertyRowMapper<>(User.class), filmId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        List<Film> films = jdbcTemplate.query("SELECT *, COUNT(user_id) AS likes " +
                        "FROM FILMS " +
                        "LEFT JOIN LIKES ON LIKES.FILM_ID = FILMS.ID " +
                        "GROUP BY FILMS.ID " +
                        "ORDER BY likes " +
                        "DESC LIMIT ?",
                new BeanPropertyRowMapper<>(Film.class), count);
        for (Film film: films) {
            Mpa mpa = jdbcTemplate.query("SELECT MPA.ID, MPA.NAME, MPA.DESCRIPTION FROM MPA " +
                            "JOIN FILMS ON FILMS.MPA_ID = MPA.ID " +
                            "WHERE FILMS.ID = ?", new Object[]{film.getId()},
                    new BeanPropertyRowMapper<>(Mpa.class)).stream().findAny().orElse(null);
            film.setMpa(mpa);
            List<Genre> genresByFilm = jdbcTemplate.query("SELECT * FROM (SELECT GENRE_ID " +
                            "FROM FILM_GENRE " +
                            "WHERE FILM_ID = ?) AS GENRES_BY_FILM " +
                            "RIGHT JOIN FILMS ON GENRES_BY_FILM.GENRE_ID = FILMS.ID " +
                            "JOIN GENRES ON GENRES.ID = GENRES_BY_FILM.GENRE_ID",
                    new BeanPropertyRowMapper<>(Genre.class), film.getId());
            film.setGenres(genresByFilm);
        }
        return films;
    }

    @Override
    public void delete(int userId, int filmId) {
        jdbcTemplate.update("DELETE FROM LIKES WHERE USER_ID=? AND FILM_ID =?", userId, filmId);
    }
}
