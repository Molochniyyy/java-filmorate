package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;

    public Film addLike(long filmId, long userId) {
        storage.getFilmById(filmId).getLikes().add(userId);
        return storage.getFilmById(filmId);
    }

    public Film deleteLike(long filmId, long userId) {

        if (userId > 0 && (storage.getFilmById(filmId).getLikes().contains(userId))) {
            storage.getFilmById(filmId).getLikes().remove(userId);
        } else {
            throw new NotFoundException("Нет такого id пользователя");
        }
        return storage.getFilmById(filmId);
    }

    public ArrayList<Film> getMostPopularFilms(int count) {
        ArrayList<Film> films = new ArrayList<>();
        Comparator<Film> comparator = Comparator.comparingInt(film -> film.getLikes().size());
        Object[] arr = storage.get().stream().sorted(comparator).toArray();
        if (count > arr.length) {
            count = arr.length;
        }
        for (int i = arr.length - 1; i >= arr.length - count; i--) {
            films.add((Film) arr[i]);
        }
        return films;
    }

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
