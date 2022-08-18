package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;
    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public Map<Integer, User> readAll() {
        return userStorage.readAll();
    }
    public void add(int userID, int friendID) {
        if (readAll().containsKey(userID) && readAll().containsKey(friendID)) {
            readAll().get(userID).getFriends().add(friendID);
            readAll().get(friendID).getFriends().add(userID);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    public void delete(int userID, int friendID) {
        if (readAll().containsKey(userID) && readAll().containsKey(friendID)) {
            readAll().get(userID).getFriends().remove(friendID);
            readAll().get(friendID).getFriends().remove(userID);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    public Set<User> getCommonFriends(int userID, int friendID) {
        Set<User> commonFriends = new HashSet<>();
        if (readAll().containsKey(userID) && readAll().containsKey(friendID)) {
            for (Integer userById : readAll().get(userID).getFriends()) {
                if (readAll().get(friendID).getFriends().contains(userById)) {
                    commonFriends.add(readAll().get(userById));
                }
            }
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
        return commonFriends;
    }
}
