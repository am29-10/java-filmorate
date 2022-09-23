package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDao;

import java.util.List;

@Component
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre create(Genre genre) {
        jdbcTemplate.update("INSERT INTO genres (name) VALUES (?)", genre.getName());
        int id = jdbcTemplate.queryForObject("SELECT MAX(id) FROM GENRES", Integer.class);
        genre.setId(id);
        return genre;
    }

    @Override
    public List<Genre> readAll() {
        return jdbcTemplate.query("SELECT * FROM GENRES", new BeanPropertyRowMapper<>(Genre.class));
    }

    @Override
    public Genre update(Genre genre) {
        jdbcTemplate.update("UPDATE GENRES SET name=? WHERE id=?", genre.getName(), genre.getId());
        return genre;
    }

    @Override
    public Genre getGenreById(int id) {
        return jdbcTemplate.query("SELECT * FROM GENRES WHERE id = ?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Genre.class)).stream().findAny().orElse(null);
    }
}
