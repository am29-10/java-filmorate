package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.UserDao;

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
        validate(user);
        if(userDao.getUserById(user.getId()) != null) {
            throw new ValidationException("Пользователь с таким id уже есть в базе");
        }
        User createUser = userDao.create(user);
        log.info("Пользователь с id '{}' добавлен в список", createUser.getId());
        return createUser;
    }

    public User update(User user) {
        validate(user);
        if (userDao.getUserById(user.getId()) != null) {
            User updateUser = userDao.update(user);
            log.info("Пользователь с id '{}' обновлен", updateUser.getId());
            return updateUser;
        } else {
            log.info("NoMovieFoundException (Пользователь не может быть обновлен, т.к. его нет в списке)");
            throw new EntityNotFoundException("Пользователь не может быть обновлен, т.к. его нет в списке");
        }
    }

    public List<User> readAll() {
        return userDao.readAll();
    }

    public User getUserById(int id) {
        if (userDao.getUserById(id) != null) {
            return userDao.getUserById(id);
        } else {
            throw new EntityNotFoundException(String.format("Пользователя с id=%d нет в списке", id));
        }
    }
    public void addFriend(int userID, int friendID) {
        if (userDao.getUserById(userID) != null && userDao.getUserById(friendID) != null) {
            friendshipDao.create(userID, friendID);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    public void deleteFriend(int userID, int friendID) {
        if (userDao.getUserById(userID) != null && userDao.getUserById(friendID) != null) {
            friendshipDao.delete(userID, friendID);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    public List<User> getFriendsForUserId(int id) {
        return friendshipDao.readFriendsByUserId(id);
    }

    public List<User> getCommonFriends(int userID, int friendID) {
        if (userDao.getUserById(userID) != null && userDao.getUserById(friendID) != null) {
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
    }



}
