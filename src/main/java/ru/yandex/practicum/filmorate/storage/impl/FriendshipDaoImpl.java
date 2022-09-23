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
                        "FROM (SELECT FRIEND_ID " +
                        "FROM FRIENDSHIP " +
                        "WHERE USER_ID = ?) AS FRIENDS_BY_USER " +
                        "JOIN USERS ON USERS.ID=FRIENDS_BY_USER.FRIEND_ID",
                new BeanPropertyRowMapper<>(User.class), userId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        return jdbcTemplate.query("SELECT * FROM USERS WHERE id IN " +
                        "(SELECT t.id_2 AS friend_id FROM " +
                        "(SELECT F.user_id AS id_1, F.friend_id AS id_2 " +
                        "FROM FRIENDSHIP AS F " +
                        "UNION " +
                        "SELECT F.FRIEND_ID AS id_1, F.USER_ID AS id_2 " +
                        "FROM FRIENDSHIP AS F " +
                        "ORDER BY id_1, id_2) AS t " +
                        "WHERE t.id_1 IN (?, ?) " +
                        "GROUP BY friend_id " +
                        "HAVING count(t.id_2) > 1);",
                new BeanPropertyRowMapper<>(User.class), userId, friendId);
    }

    @Override
    public void delete(int userId, int friendId) {
        jdbcTemplate.update("DELETE FROM FRIENDSHIP WHERE USER_ID=? AND FRIEND_ID=?", userId, friendId);
    }
}
