package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class UserService {
    private static final Map<Integer, User> users = new HashMap<>();
    private static int id;
    @Autowired
    public UserService() {
    }

    public User create(User user) {
        validate(user);
        if (user.getId() == 0) {
            user.setId(++id);
        } else if(users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с таким id уже есть в базе");
        }
        users.put(user.getId(), user);
        log.info("Пользователь с id '{}' добавлен в список", user.getId());
        return user;
    }

    public User update(User user) {
        if (users.containsKey(user.getId())) {
            validate(user);
            users.put(user.getId(), user);
            log.info("Пользователь с id '{}' обновлен", user.getId());
            return user;
        } else {
            log.info("NoMovieFoundException (Пользователь не может быть обновлен, т.к. его нет в списке)");
            throw new EntityNotFoundException("Пользователь не может быть обновлен, т.к. его нет в списке");
        }
    }

    public Map<Integer, User> readAll() {
        return users;
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
}
