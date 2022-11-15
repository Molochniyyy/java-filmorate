package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final MpaService service;

    @GetMapping
    public List<MPA> get(){
        return service.findAllMpa();
    }

    @GetMapping("/{id}")
    public MPA getById(@PathVariable int id){
        return service.findMpaById(id);
    }

}
