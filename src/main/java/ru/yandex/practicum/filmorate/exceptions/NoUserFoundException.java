package ru.yandex.practicum.filmorate.exceptions;

public class NoUserFoundException extends Throwable {
    public NoUserFoundException(String message) {
        super(message);
    }
}
