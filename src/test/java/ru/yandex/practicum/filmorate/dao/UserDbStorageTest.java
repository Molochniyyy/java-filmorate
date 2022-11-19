package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
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
    public void createAndUpdateUser() {
        User user = User.builder()
                .id(1)
                .name("Test")
                .login("TestLog")
                .email("test@yandex.ru")
                .birthday(LocalDate.of(2002, 2, 3))
                .build();
        assertEquals(user, storage.add(user),"User не создался");
        user.setEmail("test");
        assertThrows(ValidationException.class,()->storage.update(user),
                "User обновился с неправильным email");
        user.setLogin(" ");
        assertThrows(ValidationException.class,()->storage.update(user),
                "User обновился с неправильным логином");
        user.setLogin(null);
        assertThrows(ValidationException.class,()->storage.update(user),
                "User обновился с неправильным логином");
        user.setId(999);
        assertThrows(ValidationException.class,()->storage.update(user),
                "Обновился неизвестный User");
    }

    @Test
    public void getUser(){
        User user = User.builder()
                .id(1)
                .name("Test")
                .login("TestLog")
                .email("test@yandex.ru")
                .birthday(LocalDate.of(2002, 2, 3))
                .build();
        storage.add(user);
        assertEquals(1, storage.getUserById(1).getId(),"Не получили User'а");
        assertThrows(NotFoundException.class,()->storage.getUserById(999),
                "Получен неизвестный User");
        User user1 = User.builder()
                .id(2)
                .name("Test1")
                .login("TestLog1")
                .email("test1@yandex.ru")
                .birthday(LocalDate.of(2001, 1, 4))
                .build();
        storage.add(user1);
        assertEquals(2, storage.getUsers().size(),"Получено неверное количество User");
    }
}
