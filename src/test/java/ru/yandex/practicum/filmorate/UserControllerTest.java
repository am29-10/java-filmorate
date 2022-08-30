package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {

    private UserController userController;
    private User user;

    @BeforeEach
    void beforeEach() {
        userController = new UserController(new UserService());
        user = new User();
        user.setName("Фродо Бэггинс");
        user.setBirthday(LocalDate.of(1999, 10, 29));
        user.setEmail("frodo1999@mail.ru");
        user.setLogin("charm2910");
    }

    @Test
    void validateEmailTest() {
        user.setEmail("Bob23");
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Пустой email или email не содержит '@'", exception.getMessage());
    }

    @Test
    void validateLoginTest() {
        user.setLogin("charm 2910");
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Пустой логин или логин содержит пробелы", exception.getMessage());
    }

    @Test
    void validateNameTest() throws ValidationException {
        user.setName("");
        userController.create(user);
        assertEquals("charm2910", user.getName());
    }

    @Test
    void  validateBirthdayTest() {
        user.setBirthday(LocalDate.of(2023, 1, 1));
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Дата рождения указана в будущем времени", exception.getMessage());
    }

    @Test
    void validateIdTest() {
        user.setId(-1);
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(user));
        assertEquals("Значение id не может быть отрицательным", exception.getMessage());
    }
}
