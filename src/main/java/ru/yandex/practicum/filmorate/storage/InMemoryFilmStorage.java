package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{

    private int nextId = 0;

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film getFilmById(long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильма с таким id не существует");
        }
        return films.get(id);
    }

    @Override
    public Collection<Film> get() {
        log.info("Количество фильмов на данный момент - {}", films.size());
        return films.values();
    }

    @Override
    public Film update(Film film) {
        check(film);
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильма с таким id не существует");
        }
        films.put(film.getId(), film);
        log.info("Фильм {} обновлен", film.getName());
        return film;
    }

    @Override
    public Film add(Film film) {
        check(film);
        if (film.getId() <= 0) {
            film.setId(++nextId);
        }
        films.put(film.getId(), film);
        log.info("Фильм с id = {} добавлен", film.getId());
        return film;
    }

    private void check(Film film) {
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
