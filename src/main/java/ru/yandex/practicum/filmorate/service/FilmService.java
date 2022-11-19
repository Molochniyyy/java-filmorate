package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;

    public Film getById(long id) {
        log.info("Получен фильм с id = {}", id);
        return storage.getFilmById(id);
    }

    public Collection<Film> get() {
        log.info("Получены все фильмы");
        return storage.get();
    }

    public Film update(Film film) {
        log.info("Обновлен фильм с id = {}", film.getId());
        return storage.update(film);
    }

    public Film add(Film film) {
        log.info("Добавлен фильм с id = {}", film.getId());
        return storage.add(film);
    }
}
