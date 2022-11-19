package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film getFilmById(long id);
    Collection<Film> get();

    Film update(Film film);

    Film add(Film film);
}
