package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private int nextId = 0;

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User getUserById(long id){
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
        return users.get(id);
    }

    @Override
    public Collection<User> getUsers() {
        log.info("Текущее количество пользователей: {}", users.values().size());
        return users.values();
    }

    @Override
    public User add(User user) {
        check(user);
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        if (user.getId() <= 0) {
            user.setId(++nextId);
        }
        users.put(user.getId(), user);
        log.info("Пользователь с id = {} добавлен", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        check(user);
        if (user.getName() == null || user.getName().length() == 0) {
            log.info("Пользователю с пустым именем был присвоен его логин {}", user.getLogin());
            user.setName(user.getLogin());
        }
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователя с таким id не существует");
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
