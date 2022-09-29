package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
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
        List<Film> films = jdbcTemplate.query("SELECT F.* " +
                        "FROM FILMS F, MPA M " +
                        "WHERE F.MPA_ID=M.ID " +
                        "ORDER BY M.NAME DESC " +
                        "LIMIT ? ",
                new BeanPropertyRowMapper<>(Film.class), count);
        return films;
    }

    @Override
    public void delete(int userId, int filmId) {
        jdbcTemplate.update("DELETE FROM LIKES WHERE USER_ID=? AND FILM_ID =?", userId, filmId);
    }
}
