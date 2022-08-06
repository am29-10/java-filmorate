package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmService {

    void create(Film film);

    Map<Integer, Film> readAll();

    Film read(int id);

    void update(Film film);

    void delete(int id);
}
