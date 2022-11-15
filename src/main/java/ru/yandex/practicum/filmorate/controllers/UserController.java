package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public Collection<User> get() {
        return service.get();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable long id){
        return service.getById(id);
    }

    @PostMapping
    public User add(@RequestBody User user) {
        return service.add(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return service.update(user);
    }

}
