package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoMovieFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final Map<Integer, User> users = new HashMap<>();
    private static int id;

    @Override
    public User create(User user) throws ValidationException {
        if (user.getId() == 0) {
            user.setId(++id);
        }
        validate(user);
        users.put(user.getId(), user);
        log.info("Фильм с id '{}' добавлен в список", user.getId());
        return user;
    }

    @Override
    public Map<Integer, User> readAll() {
        log.info("Количество имеющихся фильмов: {}", users.size());
        return users;

    }

    @Override
    public User update(User user) throws ValidationException, NoMovieFoundException {
        if (users.containsKey(user.getId())) {
            validate(user);
            users.put(user.getId(), user);
            log.info("Фильм с id '{}' обновлен", user.getId());
            return user;
        } else {
            log.info("NoMovieFoundException (Фильм не может быть обновлен, т.к. его нет в списке)");
            throw new NoMovieFoundException("Фильм не может быть обновлен, т.к. его нет в списке");
        }
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

