package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.*;

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
    UserDao userDao;

    FilmGenreDao filmGenreDao;
    MpaDao mpaDao;

    @Autowired
    public FilmService(FilmDao filmDao, FilmLikeDao filmLikeDao, UserService userService, FilmGenreDao filmGenreDao,
                       MpaDao mpaDao, UserDao userDao) {
        this.filmDao = filmDao;
        this.filmLikeDao = filmLikeDao;
        this.userService = userService;
        this.filmGenreDao = filmGenreDao;
        this.mpaDao = mpaDao;
        this.userDao = userDao;
    }

    public Film create(Film film) {
        validate(film);
        if(filmDao.getFilmById(film.getId()) != null) {
            throw new ValidationException("Фильм с таким id уже есть в базе");
        }
        Film newFilm = filmDao.create(film);
        log.info("Фильм с id '{}' добавлен в список", newFilm.getId());
        newFilm.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
        if (film.getGenres() != null) {
            addFilmGenre(newFilm.getId(), new HashSet<>(film.getGenres()));
            newFilm.setGenres(filmGenreDao.readGenresByFilmId(film.getId()));
        }
        return newFilm;
    }

    public List<Film> readAll() {
        List<Film> allFilms = filmDao.readAll();
        for (Film film :allFilms) {
            film.setMpa(mpaDao.getMpaByFilmId(film.getId()));
            film.setGenres(filmGenreDao.readGenresByFilmId(film.getId()));
        }
        return allFilms;
    }

    public Film update(Film film) {
        validate(film);
        if (filmDao.getFilmById(film.getId()) != null) {
            Film updateFilm = filmDao.update(film);
            updateFilm.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
            if (film.getGenres() != null) {
                filmGenreDao.delete(film.getId());
                addFilmGenre(updateFilm.getId(), new HashSet<>(film.getGenres()));
                updateFilm.setGenres(filmGenreDao.readGenresByFilmId(film.getId()));
            }
            log.info("Фильм с id '{}' обновлен", updateFilm.getId());
            return updateFilm;
        } else {
            log.info("NoMovieFoundException (Фильм не может быть обновлен, т.к. его нет в списке)");
            throw new EntityNotFoundException("Фильм не может быть обновлен, т.к. его нет в списке");
        }
    }

    public Film getFilmById(int id) {
        if (filmDao.getFilmById(id) != null) {
            Film getFilm = filmDao.getFilmById(id);
            getFilm.setMpa(mpaDao.getMpaByFilmId(id));
            getFilm.setGenres(filmGenreDao.readGenresByFilmId(id));
            return getFilm;
        } else {
            throw new EntityNotFoundException(String.format("Фильма с id=%d нет в списке", id));
        }
    }
    public void addLike(int userId, int filmId) {
        if (userDao.getUserById(userId) != null && filmDao.getFilmById(filmId) != null) {
            filmLikeDao.create(userId, filmId);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    void addFilmGenre(int filmId, Set<Genre> genres) {
        genres.forEach((genre) -> filmGenreDao.create(filmId, genre.getId()));
    }

    public void removeLike(int userId, int filmId) {
        if (userDao.getUserById(userId) != null && filmDao.getFilmById(filmId) != null) {
            filmLikeDao.delete(userId, filmId);
        } else {
            throw new EntityNotFoundException("Объект не найден. Необходимо проверить id");
        }
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> popularFilms = filmLikeDao.getPopularFilms(count);
        for (Film film :popularFilms) {
            film.setMpa(mpaDao.getMpaByFilmId(film.getId()));
            film.setGenres(filmGenreDao.readGenresByFilmId(film.getId()));
        }
        return popularFilms;
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
    }

}
