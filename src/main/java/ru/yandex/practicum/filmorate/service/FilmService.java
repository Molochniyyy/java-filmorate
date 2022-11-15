package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;

    public Film getById(long id) {
        return storage.getFilmById(id);
    }

    public Collection<Film> get() {
        return storage.get();
    }

    public Film update(Film film) {
        return storage.update(film);
    }

    public Film add(Film film) {
        return storage.add(film);
    }
}
