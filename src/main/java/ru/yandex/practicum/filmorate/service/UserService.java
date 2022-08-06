package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserService {
    void create(User user);

    Map<Integer, User> readAll();

    User read(int id);

    void update(User user);

    void delete(int id);
}
