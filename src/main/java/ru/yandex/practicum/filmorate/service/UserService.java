    package ru.yandex.practicum.filmorate.service;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Service;
    import ru.yandex.practicum.filmorate.model.User;
    import ru.yandex.practicum.filmorate.storage.UserStorage;

    import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage storage;

    public User getById(long id){
        log.info("Получен пользователь с id = {}", id);
        return storage.getUserById(id);
    }

    public User add(User user){
        log.info("Добавлен пользователь с id = {}",user.getId());
        return storage.add(user);
    }

    public User update(User user){
        log.info("Обновлен пользователь с id = {}", user.getId());
        return storage.update(user);
    }

    public Collection<User> get(){
        log.info("Получены все пользователи");
        return storage.getUsers();
    }
}
