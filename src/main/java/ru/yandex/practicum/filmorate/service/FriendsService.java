package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendsDao;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FriendsService {

    private final FriendsDao friendsDao;

    public void addFriend(long userId, long friendId) {
        friendsDao.addFriend(userId, friendId);
        log.info("User с id = {} добавил в друзья User'a с id = {}", userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        friendsDao.deleteFriend(userId, friendId);
        log.info("User с id = {} удалил из друзей User'a с id = {}", userId, friendId);
    }

    public List<User> getFriends(int id) {
        List<User> friends = friendsDao.getFriends(id);
        log.info("Друзья пользователя с id = {} найдены", id);
        return friends;
    }

    public List<User> getMutualFriends(int userId, int otherId) {
        List<User> friends = friendsDao.getMutualFriends(userId, otherId);
        log.info("Общие друзья пользователей с id = {} и {} найдены", userId, otherId);
        return friends;
    }

}
