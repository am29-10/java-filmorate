package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Service
public class FilmServiceImpl implements FilmService {

    private static final Map<Integer, Film> films = new HashMap<>();
    private static int id;

    @Override
    public void create(Film film) {
        if (film.getId() == 0) {
            film.setId(++id);
        }
        films.put(film.getId(), film);
    }

    @Override
    public Map<Integer, Film> readAll() {
        return films;
    }

    @Override
    public Film read(int id) {
        return films.get(id);
    }

    @Override
    public void update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        }
    }

    @Override
    public void delete(int id) {
        films.remove(id);
    }

}
