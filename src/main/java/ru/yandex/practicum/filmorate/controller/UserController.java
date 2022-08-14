package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController  {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() throws EntityNotFoundException {
        return new ArrayList<>(userService.readAll().values());
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException, EntityNotFoundException {
        return userService.update(user);
    }

}
