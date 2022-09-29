package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDao;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
public class GenreService {

    GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Genre create(Genre genre) {
        validate(genre);
        if(genreDao.getGenreById(genre.getId()) != null) {
            throw new ValidationException("Жанр с таким id уже есть в базе");
        }
        Genre newGenre = genreDao.create(genre);
        log.info("Жанр с id '{}' добавлен в список", genre.getId());
        return newGenre;
    }

    public List<Genre> readAll() {
        return genreDao.readAll();
    }

    public Genre update(Genre genre) {
        validate(genre);
        if (genreDao.getGenreById(genre.getId()) != null) {
            log.info("Жанр с id '{}' обновлен", genre.getId());
            return genreDao.update(genre);
        } else {
            log.info("EntityNotFoundException (Жанр не может быть обновлен, т.к. его нет в списке)");
            throw new EntityNotFoundException("Жанр не может быть обновлен, т.к. его нет в списке");
        }
    }

    public Genre getGenreById(int id) {
        if (genreDao.getGenreById(id) != null) {
            return genreDao.getGenreById(id);
        } else {
            throw new EntityNotFoundException(String.format("Жанра с id=%d нет в списке", id));
        }
    }

    private void validate(Genre genre) {
        if (genre.getName().isEmpty() || genre.getName().isBlank()) {
            log.info("ValidationException (Пустое название жанра)");
            throw new ValidationException("Пустое название жанра");
        }
        if (genre.getId() < 0) {
            log.info("ValidationException (Значение id не может быть отрицательным)");
            throw new ValidationException("Значение id не может быть отрицательным");
        }
    }

}
