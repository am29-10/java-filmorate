package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final Map<Integer, User> users = new HashMap<>();
    private static int id;

    @Override
    public void create(User user) {
        if (user.getId() == 0) {
            user.setId(++id);
        }
        users.put(user.getId(), user);
    }

    @Override
    public Map<Integer, User> readAll() {
        return users;
    }

    @Override
    public User read(int id) {
        return users.get(id);
    }

    @Override
    public void update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        }
    }

    @Override
    public void delete(int id) {
        users.remove(id);
    }
}

