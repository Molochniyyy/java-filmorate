package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
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
}
