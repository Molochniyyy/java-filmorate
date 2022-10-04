package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private int nextId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Количество фильмов на данный момент - {}",films.values().size());
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        checkFilm(film);
        if (film.getId() <= 0) {
            film.setId(++nextId);
            films.put(film.getId(), film);
        } else {
            films.put(film.getId(), film);
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        checkFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильма с таким id не существует");
        }
        films.put(film.getId(), film);
        return film;
    }

    private void checkFilm(Film film) {
        if (film.getName().length() == 0 || film.getName() == null) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не должна быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не должна быть отрицательной");
        }
    }
}
