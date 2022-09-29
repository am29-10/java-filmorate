package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreDao;

import java.util.List;

@Component
public class FilmGenreDaoImpl implements FilmGenreDao {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(int filmId, int genreId) {
        jdbcTemplate.update("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)",
                filmId, genreId);
    }

    @Override
    public List<Genre> readGenresByFilmId(int filmId) {
        return jdbcTemplate.query("SELECT GENRES.ID, GENRES.NAME " +
                        "FROM GENRES " +
                        "JOIN FILM_GENRE ON GENRES.id = FILM_GENRE.genre_id " +
                        "WHERE FILM_GENRE.film_id = ? " +
                        "ORDER BY GENRES.ID ",
                new BeanPropertyRowMapper<>(Genre.class), filmId);
    }

    @Override
    public void delete(int filmId) {
        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE FILM_ID = ?",
                filmId);
    }


}
