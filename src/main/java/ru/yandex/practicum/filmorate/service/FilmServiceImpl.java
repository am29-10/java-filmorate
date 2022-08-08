package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {

    private static final Map<Integer, Film> films = new HashMap<>();
    private static int id;
    private static final LocalDate BIRTHDAY_MOVIE = LocalDate.of(1895,12,28);
    private static final int LIMIT_DESCRIPTION = 200;

    @Override
    public Film create(Film film) throws ValidationException {
        if (film.getId() == 0) {
            film.setId(++id);
        }
        validate(film);
        films.put(film.getId(), film);
        log.info("Фильм с id '{}' добавлен в список", film.getId());
        return film;
    }

    @Override
    public Map<Integer, Film> readAll() {
        log.info("Количество имеющихся фильмов: {}", films.size());
        return films;
    }


    @Override
    public Film update(Film film) throws ValidationException, EntityNotFoundException {
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


    public void validate(Film film) throws ValidationException {
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
