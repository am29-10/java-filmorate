package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
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
        return jdbcTemplate.query("SELECT * " +
                "FROM films ", new BeanPropertyRowMapper<>(Film.class));
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update("UPDATE films SET name=?, description=?, release_date=?, duration=?, mpa_id=? WHERE id=?",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        return jdbcTemplate.query("SELECT F.* " +
                                "FROM FILMS AS F " +
                                "JOIN MPA ON F.MPA_ID = MPA.ID " +
                                "WHERE F.ID = ? "
                        , new Object[]{id} ,new BeanPropertyRowMapper<>(Film.class)).stream()
                .findAny().orElse(null);
    }
}
