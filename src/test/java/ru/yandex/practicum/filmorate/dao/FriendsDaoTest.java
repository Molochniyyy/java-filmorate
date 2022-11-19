package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendsDao;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendsDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final FriendsDao dao;
    private final UserDbStorage storage;

    @AfterEach
    public void tearDown() {
        jdbcTemplate.update("DELETE FROM LIKES");
        jdbcTemplate.update("DELETE FROM FILM_GENRE");
        jdbcTemplate.update("DELETE FROM FRIENDS");
        jdbcTemplate.update("DELETE FROM USERS");
        jdbcTemplate.update("DELETE FROM FILMS");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE FILMS ALTER COLUMN ID RESTART WITH 1");
    }

    @Test
    public void addAndGetFriends() {
        User user = User.builder()
                .id(1)
                .name("Test")
                .login("TestLog")
                .email("test@yandex.ru")
                .birthday(LocalDate.of(2002, 2, 3))
                .build();
        User user1 = User.builder()
                .id(2)
                .name("Friend")
                .login("LogFriend")
                .email("friend@yandex.ru")
                .birthday(LocalDate.of(2003, 4, 12))
                .build();
        User user2 = User.builder()
                .id(3)
                .name("Friend1")
                .login("LogFriend1")
                .email("friend1@yandex.ru")
                .birthday(LocalDate.of(2000, 3, 5))
                .build();
        storage.add(user);
        storage.add(user1);
        storage.add(user2);
        dao.addFriend(user.getId(), user1.getId());
        dao.addFriend(user.getId(), user2.getId());
        dao.addFriend(user1.getId(), user2.getId());
        assertEquals(3,dao.getMutualFriends(1,2).get(0).getId());
        assertEquals(2,dao.getFriends(1).size());
        assertEquals(1,dao.getFriends(2).size());
        dao.deleteFriend(1,2);
        assertEquals(1,dao.getFriends(1).size());
        assertThrows(NotFoundException.class,()->dao.addFriend(432,234));
    }
}
