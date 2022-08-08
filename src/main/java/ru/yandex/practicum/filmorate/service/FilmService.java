package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exceptions.NoMovieFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmService {

    Film create(Film film) throws ValidationException;

    Map<Integer, Film> readAll() throws NoMovieFoundException;

    Film update(Film film) throws ValidationException, NoMovieFoundException;

}
