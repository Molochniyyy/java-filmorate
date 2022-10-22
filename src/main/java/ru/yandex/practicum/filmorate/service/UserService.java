package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public User addFriend(long userId, long friendId) {
        storage.getUserById(friendId).getFriends().add(userId);
        storage.getUserById(userId).getFriends().add(friendId);
        return storage.getUserById(userId);
    }

    public User deleteFriend(long userId, long friendId) {
        storage.getUserById(userId).getFriends().remove(friendId);
        return storage.getUserById(userId);
    }

    public List<User> getFriends(long id) {
        List<User> friends = new ArrayList<>(Collections.emptyList());
        if(!storage.getUserById(id).getFriends().isEmpty()) {
            storage.getUserById(id).getFriends().forEach(friendId -> friends.add(storage.getUserById(friendId)));
        }
        return friends;
    }

    public List<User> getMutualFriends(long userId, long friendId){
        ArrayList<User> mutualFriends = new ArrayList<>();
        if(storage.getUserById(friendId).getFriends().isEmpty() || storage.getUserById(userId).getFriends().isEmpty()){
            return Collections.emptyList();
        }
        for(long id : storage.getUserById(userId).getFriends()){
            if(storage.getUserById(friendId).getFriends().contains(id)){
                mutualFriends.add(storage.getUserById(id));
            }
        }
        return mutualFriends;
    }

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
