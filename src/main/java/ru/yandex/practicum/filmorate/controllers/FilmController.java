package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService service;

    @GetMapping
    public Collection<Film> get() {
        return service.get();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable long id){
        return service.getById(id);
    }

    @PostMapping
    public Film add(@RequestBody Film film) {
        return service.add(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return service.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable long id, @PathVariable long userId) {
        return service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable long id, @PathVariable long userId) {
        return service.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public ArrayList<Film> getMostPopularFilms(@RequestParam(defaultValue = "10", name = "count") int count) {
        return service.getMostPopularFilms(count);
    }
}
