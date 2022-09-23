package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDao;

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
        if(validateGenreId(genre.getId())) {
            throw new ValidationException("Жанр с таким id уже есть в базе");
        }
        validate(genre);
        genreDao.create(genre);
        log.info("Жанр с id '{}' добавлен в список", genre.getId());
        return genre;
    }

    public List<Genre> readAll() {
        return genreDao.readAll();
    }

    public Genre update(Genre genre) {
        if (validateGenreId(genre.getId())) {
            validate(genre);
            genreDao.update(genre);
            log.info("Жанр с id '{}' обновлен", genre.getId());
            return genre;
        } else {
            log.info("EntityNotFoundException (Жанр не может быть обновлен, т.к. его нет в списке)");
            throw new EntityNotFoundException("Жанр не может быть обновлен, т.к. его нет в списке");
        }
    }

    public Genre getGenreById(int id) {
        if (validateGenreId(id)) {
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

    private boolean validateGenreId(int id) {
        boolean isInStock = false;
        for (Genre genre : genreDao.readAll()) {
            int genreId = genre.getId();
            if (genreId == id) {
                isInStock = true;
            }
        }
        return isInStock;
    }
}
