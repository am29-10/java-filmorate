package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    Film create(Film film);

    Map<Integer, Film> readAll();

    Film update(Film film);

}
