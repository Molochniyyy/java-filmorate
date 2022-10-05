package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private int nextId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> get() {
        log.info("Текущее количество пользователей: {}", users.values().size());
        return users.values();
    }

    @PostMapping
    public User add(@RequestBody User user) {
        check(user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getId() <= 0) {
            user.setId(++nextId);
        }
        users.put(user.getId(), user);
        log.info("Пользователь с id = {} добавлен",user.getId());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        check(user);
        if (user.getName() == null || user.getName().length() == 0) {
            log.info("Пользователю с пустым именем был присвоен его логин {}",user.getLogin());
            user.setName(user.getLogin());
        }
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователя с таким id не существует");
        }
        users.put(user.getId(), user);
        return user;
    }

    private void check(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Адрес электронной почты не должен быть равен null");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и не может содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
