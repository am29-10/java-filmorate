package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDao {

    Mpa create(Mpa mpa);

    List<Mpa> readAll();

    Mpa update(Mpa mpa);

    Mpa getMpaById(int id);
}
