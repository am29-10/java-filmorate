package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

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

    public User getUserById(int id) {
        if (readAll().containsKey(id)) {
            return readAll().get(id);
        } else {
            throw new EntityNotFoundException(String.format("Пользователя с id=%d нет в списке", id));
        }
    }
    public void addFriend(int userID, int friendID) {
        if (readAll().containsKey(userID) && readAll().containsKey(friendID)) {
            getUserById(userID).getFriends().add(friendID);
            getUserById(friendID).getFriends().add(userID);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    public void deleteFriend(int userID, int friendID) {
        if (readAll().containsKey(userID) && readAll().containsKey(friendID)) {
            getUserById(userID).getFriends().remove(friendID);
            getUserById(friendID).getFriends().remove(userID);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    public List<User> getFriendsForUserId(int id) {
        List<User> friendsForUserId = new ArrayList<>();
        for (int friendId: getUserById(id).getFriends()) {
            friendsForUserId.add(getUserById(friendId));
        }
        return friendsForUserId;
    }

    public Set<User> getCommonFriends(int userID, int friendID) {
        Set<User> commonFriends = new HashSet<>();
        if (readAll().containsKey(userID) && readAll().containsKey(friendID)) {
            for (Integer userById : getUserById(userID).getFriends()) {
                if (getUserById(friendID).getFriends().contains(userById)) {
                    commonFriends.add(readAll().get(userById));
                }
            }
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
        return commonFriends;
    }
}
