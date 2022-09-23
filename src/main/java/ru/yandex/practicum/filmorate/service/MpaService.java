package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDao;

import java.util.List;

@Slf4j
@Component
public class MpaService {

    MpaDao mpaDao;

    @Autowired
    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public Mpa create(Mpa mpa) {
        if(validateMpaId(mpa.getId())) {
            throw new ValidationException("MPA с таким id уже есть в базе");
        }
        validate(mpa);
        mpaDao.create(mpa);
        log.info("MPA с id '{}' добавлен в список", mpa.getId());
        return mpa;
    }

    public List<Mpa> readAll() {
        return mpaDao.readAll();
    }

    public Mpa update(Mpa mpa) {
        if (validateMpaId(mpa.getId())) {
            validate(mpa);
            mpaDao.update(mpa);
            log.info("MPA с id '{}' обновлен", mpa.getId());
            return mpa;
        } else {
            log.info("EntityNotFoundException (MPA не может быть обновлен, т.к. его нет в списке)");
            throw new EntityNotFoundException("MPA не может быть обновлен, т.к. его нет в списке");
        }
    }

    public Mpa getMpaById(int id) {
        if (validateMpaId(id)) {
            return mpaDao.getMpaById(id);
        } else {
            throw new EntityNotFoundException(String.format("MPA с id=%d нет в списке", id));
        }
    }

    private void validate(Mpa mpa) {
        if (mpa.getName().isEmpty() || mpa.getName().isBlank()) {
            log.info("ValidationException (Пустое название MPA)");
            throw new ValidationException("Пустое название MPA");
        }
        if (mpa.getId() < 0) {
            log.info("ValidationException (Значение id не может быть отрицательным)");
            throw new ValidationException("Значение id не может быть отрицательным");
        }
    }

    private boolean validateMpaId(int id) {
        boolean isInStock = false;
        for (Mpa mpa : mpaDao.readAll()) {
            int mpaId = mpa.getId();
            if (mpaId == id) {
                isInStock = true;
            }
        }
        return isInStock;
    }
}
