package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User getUserById(long id);
    Collection<User> getUsers();

    User add(User user);

    User update(User user);
}
