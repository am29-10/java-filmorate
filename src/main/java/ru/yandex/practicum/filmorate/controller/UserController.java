package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController  {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User get(@Valid @PathVariable int id) {
        log.info("Получен запрос GET /users/{}", id);
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Получен запрос GET /users");
        return new ArrayList<>(userService.readAll());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос POST /users");
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Получен запрос PUT /users");
        return userService.update(user);
    }

    @PutMapping ("/{id}/friends/{friendId}")
    public void addFriend(@Valid @PathVariable int id, @Valid @PathVariable int friendId) {
        log.info("Получен запрос PUT /users/{}/friends/{}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@Valid @PathVariable int id, @Valid @PathVariable int friendId) {
        log.info("Получен запрос DELETE /users/{}/friends/{}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@Valid @PathVariable int id) {
        log.info("Получен запрос GET /users/{}/friends", id);
        return userService.getFriendsForUserId(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
        public List<User> getCommonFriends(@Valid @PathVariable int id, @Valid @PathVariable int otherId) {
        log.info("Получен запрос GET /users/{}/friends/common/{}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

}
