package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoUserFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() throws NoUserFoundException {
        if (!userService.readAll().isEmpty()) {
            log.info("Количество пользователей: {}", userService.readAll().size());
            return new ArrayList<>(userService.readAll().values());
        } else {
            throw new NoUserFoundException("Список пользователей пуст");
        }
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        validate(user);
        userService.create(user);
        log.info("Пользователь c id '{}' добавлен в список", user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        validate(user);
        userService.update(user);
        log.info("Пользователь с id '{}' обновлен", user.getId());
        return user;
    }

    public void validate(User user) throws ValidationException {
        if (user.getEmail().isEmpty() || user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            log.info("ValidationException (Пустой email или email не содержит '@')");
            throw new ValidationException("Пустой email или email не содержит '@'");
        }
        if (user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("ValidationException (Пустой логин или логин содержит пробелы)");
            throw new ValidationException("Пустой логин или логин содержит пробелы");
        }
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            log.info("Имя пользователя соответствует его логину");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("ValidationException (Дата рождения указана в будущем времени)");
            throw new ValidationException("Дата рождения указана в будущем времени");
        }
        if (user.getId() < 0) {
            log.info("ValidationException (Значение id не может быть отрицательным)");
            throw new ValidationException("Значение id не может быть отрицательным");
        }
    }
}
