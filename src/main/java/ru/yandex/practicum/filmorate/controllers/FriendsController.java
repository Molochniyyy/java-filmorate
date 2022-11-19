package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendsService;

import java.util.List;

@RestController
@RequestMapping("/users/{id}/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService service;

    @PutMapping("/{friendId}")
    public void add(@PathVariable long id, @PathVariable long friendId){
        service.addFriend(id,friendId);
    }

    @DeleteMapping("/{friendId}")
    public void delete(@PathVariable int id, @PathVariable int friendId){
        service.deleteFriend(id,friendId);
    }

    @GetMapping
    public List<User> get(@PathVariable int id){
        return service.getFriends(id);
    }

    @GetMapping("/common/{otherId}")
    public List<User> getMutual(@PathVariable int id, @PathVariable int otherId){
        return service.getMutualFriends(id, otherId);
    }
}
