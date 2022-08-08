package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserService {
    User create(User user) throws ValidationException;

    Map<Integer, User> readAll() throws EntityNotFoundException;

    User update(User user) throws ValidationException, EntityNotFoundException;

}
