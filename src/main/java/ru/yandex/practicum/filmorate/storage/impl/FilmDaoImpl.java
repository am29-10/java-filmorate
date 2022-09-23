package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDao;

import java.util.List;

@Component
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        jdbcTemplate.update("INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId());
        int id = jdbcTemplate.queryForObject("SELECT MAX(id) FROM FILMS", Integer.class);
        film.setId(id);
        return film;
    }

    @Override
    public List<Film> readAll() {
        List<Film> films = jdbcTemplate.query("SELECT * " +
                "FROM films ", new BeanPropertyRowMapper<>(Film.class));
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
    public Film update(Film film) {
        jdbcTemplate.update("UPDATE films SET name=?, description=?, release_date=?, duration=?, mpa_id=? WHERE id=?",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        Mpa mpa = jdbcTemplate.query("SELECT MPA.ID, MPA.NAME, MPA.DESCRIPTION FROM MPA " +
                        "JOIN FILMS ON FILMS.MPA_ID = MPA.ID " +
                        "WHERE FILMS.ID = ?", new Object[]{film.getId()},
                new BeanPropertyRowMapper<>(Mpa.class)).stream().findAny().orElse(null);
        film.setMpa(mpa);
        List<Genre> genresByFilm = jdbcTemplate.query("SELECT * " +
                        "FROM FILM_GENRE " +
                        "JOIN GENRES ON GENRES.ID = FILM_GENRE.GENRE_ID " +
                        "WHERE FILM_ID = ? " +
                        "ORDER BY GENRE_ID ",
                new BeanPropertyRowMapper<>(Genre.class), film.getId());
        film.setGenres(genresByFilm);
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        Film film = jdbcTemplate.query("SELECT * " +
                                "FROM FILMS " +
                                "WHERE ID = ? "
                        , new Object[]{id} ,new BeanPropertyRowMapper<>(Film.class)).stream()
                .findAny().orElse(null);
        Mpa mpa = jdbcTemplate.query("SELECT MPA.ID, MPA.NAME, MPA.DESCRIPTION FROM MPA " +
                        "JOIN FILMS ON FILMS.MPA_ID = MPA.ID " +
                        "WHERE FILMS.ID = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(Mpa.class)).stream().findAny().orElse(null);
        film.setMpa(mpa);
        List<Genre> genresByFilm = jdbcTemplate.query("SELECT * " +
                        "FROM FILM_GENRE " +
                        "JOIN GENRES ON GENRES.ID = FILM_GENRE.GENRE_ID " +
                        "WHERE FILM_ID = ? " +
                        "ORDER BY GENRE_ID ",
                new BeanPropertyRowMapper<>(Genre.class), id);
        film.setGenres(genresByFilm);
        return film;

    }



}
