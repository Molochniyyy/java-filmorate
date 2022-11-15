    package ru.yandex.practicum.filmorate.service;

    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import ru.yandex.practicum.filmorate.model.User;
    import ru.yandex.practicum.filmorate.storage.UserStorage;

    import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public User getById(long id){
        return storage.getUserById(id);
    }

    public User add(User user){
        return storage.add(user);
    }

    public User update(User user){
        return storage.update(user);
    }

    public Collection<User> get(){
        return storage.getUsers();
    }
}
