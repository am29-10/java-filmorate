package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {
    User create(User user);

    List<User> readAll();

    User update(User user);

    User getUserById(int id);



}
