package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmDao;
import ru.yandex.practicum.filmorate.storage.FilmGenreDao;
import ru.yandex.practicum.filmorate.storage.FilmLikeDao;
import ru.yandex.practicum.filmorate.storage.MpaDao;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class FilmService {
    private static final LocalDate BIRTHDAY_MOVIE = LocalDate.of(1895,12,28);
    private static final int LIMIT_DESCRIPTION = 200;

    FilmDao filmDao;
    FilmLikeDao filmLikeDao;
    UserService userService;

    FilmGenreDao filmGenreDao;
    MpaDao mpaDao;

    @Autowired
    public FilmService(FilmDao filmDao, FilmLikeDao filmLikeDao, UserService userService, FilmGenreDao filmGenreDao, MpaDao mpaDao) {
        this.filmDao = filmDao;
        this.filmLikeDao = filmLikeDao;
        this.userService = userService;
        this.filmGenreDao = filmGenreDao;
        this.mpaDao = mpaDao;
    }

    public Film create(Film film) {
        if(validateFilmId(film.getId())) {
            throw new ValidationException("Фильм с таким id уже есть в базе");
        }
        validate(film);
        filmDao.create(film);
        log.info("Фильм с id '{}' добавлен в список", film.getId());
        if (film.getGenres() != null) {
            addFilmGenre(film.getId(), new HashSet<>(film.getGenres()));
            film.setGenres(filmGenreDao.readGenresByFilmId(film.getId()));
        }
        return film;
    }

    public List<Film> readAll() {
        return filmDao.readAll();
    }

    public Film update(Film film) {
        if (validateFilmId(film.getId())) {
            validate(film);
            if (film.getGenres() != null) {
                filmGenreDao.delete(film.getId());
                addFilmGenre(film.getId(), new HashSet<>(film.getGenres()));
                film.setGenres(filmGenreDao.readGenresByFilmId(film.getId()));
            }
            filmDao.update(film);
            log.info("Фильм с id '{}' обновлен", film.getId());
            return film;
        } else {
            log.info("NoMovieFoundException (Фильм не может быть обновлен, т.к. его нет в списке)");
            throw new EntityNotFoundException("Фильм не может быть обновлен, т.к. его нет в списке");
        }
    }

    public Film getFilmById(int id) {
        if (validateFilmId(id)) {
            return filmDao.getFilmById(id);
        } else {
            throw new EntityNotFoundException(String.format("Фильма с id=%d нет в списке", id));
        }
    }
    public void addLike(int userId, int filmId) {
        if (userService.validateUserId(userId) && validateFilmId(filmId)) {
            filmLikeDao.create(userId, filmId);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    void addFilmGenre(int filmId, Set<Genre> genres) {
        genres.forEach((genre) -> filmGenreDao.create(filmId, genre.getId()));
    }

    public void removeLike(int userId, int filmId) {
        if (userService.validateUserId(userId) && validateFilmId(filmId)) {
            filmLikeDao.delete(userId, filmId);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    public List<Film> getPopularFilms(int count) {
        return filmLikeDao.getPopularFilms(count);
    }

    private void validate(Film film) {
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

    private boolean validateFilmId(int id) {
        boolean isInStock = false;
        for (Film film : filmDao.readAll()) {
            int filmId = film.getId();
            if (filmId == id) {
                isInStock = true;
            }
        }
        return isInStock;
    }
}
