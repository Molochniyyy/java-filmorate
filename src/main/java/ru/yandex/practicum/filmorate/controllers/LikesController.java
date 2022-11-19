package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.LikesService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LikesController {
    private final LikesService service;

    @PutMapping("/films/{id}/like/{userId}")
    public void add(@PathVariable long id, @PathVariable long userId){
        service.addLike(id,userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void delete(@PathVariable long id, @PathVariable long userId){
        service.removeLike(id,userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", name = "count") int count){
        return service.getPopularFilms(count);
    }
}
