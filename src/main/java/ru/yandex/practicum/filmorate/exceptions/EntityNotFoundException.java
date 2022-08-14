package ru.yandex.practicum.filmorate.exceptions;

public class EntityNotFoundException extends Throwable {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
