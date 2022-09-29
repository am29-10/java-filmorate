package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipDao;

import java.util.List;

@Component
public class FriendshipDaoImpl implements FriendshipDao {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(int userId, int friendId) {
        jdbcTemplate.update("INSERT INTO friendship (USER_ID, FRIEND_ID) VALUES (?, ?)",
                userId, friendId);
    }

    @Override
    public List<User> readFriendsByUserId(int userId) {
        return jdbcTemplate.query("SELECT * " +
                        "FROM USERS, FRIENDSHIP " +
                        "WHERE USERS.ID=FRIENDSHIP.FRIEND_ID " +
                        "AND FRIENDSHIP.USER_ID=? ",
                new BeanPropertyRowMapper<>(User.class), userId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        return jdbcTemplate.query("SELECT * FROM users " +
                        "WHERE id IN (SELECT FRIEND_ID FROM FRIENDSHIP WHERE user_id = ?) " +
                        "AND id IN (SELECT FRIEND_ID FROM FRIENDSHIP WHERE user_id = ?) ",
                new BeanPropertyRowMapper<>(User.class), userId, friendId);
    }

    @Override
    public void delete(int userId, int friendId) {
        jdbcTemplate.update("DELETE FROM FRIENDSHIP WHERE USER_ID=? AND FRIEND_ID=?", userId, friendId);
    }
}
