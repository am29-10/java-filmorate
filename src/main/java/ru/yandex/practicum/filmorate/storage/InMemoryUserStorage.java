package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final UserService userService;
    @Autowired
    public InMemoryUserStorage(UserService userService) {
        this.userService = userService;
    }
    @Override
    public User create(User user) {
        return userService.create(user);
    }
    @Override
    public Map<Integer, User> readAll() {
        return userService.readAll();

    }
    @Override
    public User update(User user) {
        return userService.update(user);
    }
    @Override
    public User getUserById(int id) {
        return userService.getUserById(id);
    }

}

