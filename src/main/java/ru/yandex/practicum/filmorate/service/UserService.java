package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.impl.UserDao;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class UserService {
    UserDao userDao;
    FriendshipDao friendshipDao;
    @Autowired
    public UserService(UserDao userDao, FriendshipDao friendshipDao) {
        this.userDao = userDao;
        this.friendshipDao = friendshipDao;
    }

    public User create(User user) {
        if(validateUserId(user.getId())) {
            throw new ValidationException("Пользователь с таким id уже есть в базе");
        }
        validate(user);
        log.info("Пользователь с id '{}' добавлен в список", user.getId());
        return userDao.create(user);
    }

    public User update(User user) {
        if (validateUserId(user.getId())) {
            validate(user);
            log.info("Пользователь с id '{}' обновлен", user.getId());
            return userDao.update(user);
        } else {
            log.info("NoMovieFoundException (Пользователь не может быть обновлен, т.к. его нет в списке)");
            throw new EntityNotFoundException("Пользователь не может быть обновлен, т.к. его нет в списке");
        }
    }

    public List<User> readAll() {
        return userDao.readAll();
    }

    public User getUserById(int id) {
        if (validateUserId(id)) {
            return userDao.getUserById(id);
        } else {
            throw new EntityNotFoundException(String.format("Пользователя с id=%d нет в списке", id));
        }
    }
    public void addFriend(int userID, int friendID) {
        if (validateUserId(userID) && validateUserId(friendID)) {
            friendshipDao.create(userID, friendID);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    public void deleteFriend(int userID, int friendID) {
        if (validateUserId(userID) && validateUserId(friendID)) {
            friendshipDao.delete(userID, friendID);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    public List<User> getFriendsForUserId(int id) {
        return friendshipDao.readFriendsByUserId(id);
    }

    public List<User> getCommonFriends(int userID, int friendID) {
        if (validateUserId(userID) && validateUserId(friendID)) {
            return friendshipDao.getCommonFriends(userID, friendID);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    public void validate(User user) {
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

    public boolean validateUserId(int id) {
        boolean isInStock = false;
        for (User user : userDao.readAll()) {
            int userId = user.getId();
            if (userId == id) {
                isInStock = true;
            }
        }
        return isInStock;
    }
}
