package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private static final Map<Integer, Film> films = new HashMap<>();
    private static int id;
    private static final LocalDate BIRTHDAY_MOVIE = LocalDate.of(1895,12,28);
    private static final int LIMIT_DESCRIPTION = 200;
    @Autowired
    public FilmService() {
    }

    public Film create(Film film) {
        validate(film);
        if (film.getId() == 0) {
            film.setId(++id);
        } else if(films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с таким id уже есть в базе");
        }
        films.put(film.getId(), film);
        log.info("Фильм с id '{}' добавлен в список", film.getId());
        return film;
    }
    public Map<Integer, Film> readAll() {
        return films;
    }

    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            validate(film);
            films.put(film.getId(), film);
            log.info("Фильм с id '{}' обновлен", film.getId());
            return film;
        } else {
            log.info("NoMovieFoundException (Фильм не может быть обновлен, т.к. его нет в списке)");
            throw new EntityNotFoundException("Фильм не может быть обновлен, т.к. его нет в списке");
        }
    }

    public Film getFilmById(int id) {
        if (readAll().containsKey(id)) {
            return readAll().get(id);
        } else {
            throw new EntityNotFoundException(String.format("Фильма с id=%d нет в списке", id));
        }
    }
    public void addLike(int userId, int filmId) {
        if (readAll().containsKey(userId) && readAll().containsKey(filmId)) {
            getFilmById(filmId).getLikes().add(userId);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    public void removeLike(int userId, int filmId) {
        if (readAll().containsKey(userId) && readAll().containsKey(filmId)) {
            getFilmById(filmId).getLikes().remove(userId);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    public List<Film> getPopularFilms(int count) {
        return readAll().values().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public void validate(Film film) {
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            log.info("ValidationException (Пустое название фильма)");
            throw new ValidationException("Пустое название фильма");
        }
        if (film.getDescription().length() > LIMIT_DESCRIPTION) {
            log.info("ValidationException (Выход за пределы длины описания фильма)");
            throw new ValidationException("Выход за пределы длины описания фильма");
        }
        if (film.getReleaseDate().isBefore(BIRTHDAY_MOVIE)) {
            log.info("ValidationException (Дата релиза раньше дня рождения кино)");
            throw new ValidationException("Дата релиза раньше дня рождения кино");
        }
        if (film.getDuration() < 0) {
            log.info("ValidationException (Продолжительность фильма имеет отрицательное значение)");
            throw new ValidationException("Продолжительность фильма имеет отрицательное значение");
        }
        if (film.getId() < 0) {
            log.info("ValidationException (Значение id не может быть отрицательным)");
            throw new ValidationException("Значение id не может быть отрицательным");
        }
    }


}
