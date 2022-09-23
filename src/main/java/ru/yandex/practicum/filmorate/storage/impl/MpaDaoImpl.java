package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDao;

import java.util.List;

@Component
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa create(Mpa mpa) {
        jdbcTemplate.update("INSERT INTO mpa (name, description) VALUES (?, ?)", mpa.getName(), mpa.getDescription());
        int id = jdbcTemplate.queryForObject("SELECT MAX(id) FROM MPA", Integer.class);
        mpa.setId(id);
        return mpa;
    }

    @Override
    public List<Mpa> readAll() {
        return jdbcTemplate.query("SELECT * FROM MPA", new BeanPropertyRowMapper<>(Mpa.class));
    }

    @Override
    public Mpa update(Mpa mpa) {
        jdbcTemplate.update("UPDATE mpa SET name=?, description=? WHERE id=?", mpa.getName(), mpa.getDescription(), mpa.getId());
        return mpa;
    }

    @Override
    public Mpa getMpaById(int id) {
        return jdbcTemplate.query("SELECT * FROM MPA WHERE id = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(Mpa.class)).stream().findAny().orElse(null);
    }
}
