package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipDao {

    void create(int userId, int friendId);

    List<User> readFriendsByUserId(int userId);

    List<User> getCommonFriends(int userId, int friendId);

    void delete(int userId, int friendId);
}
