package ru.yandex.practicum.filmorate.exceptions;

public class NoMovieFoundException extends Throwable {
    public NoMovieFoundException(String message) {
        super(message);
    }
}
