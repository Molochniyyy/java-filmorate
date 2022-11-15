package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.LikesDao;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikesDaoTest {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final LikesDao dao;

    @AfterEach
    public void tearDown(){
        jdbcTemplate.update("DELETE FROM LIKES");
        jdbcTemplate.update("DELETE FROM FILM_GENRE");
        jdbcTemplate.update("DELETE FROM FRIENDS");
        jdbcTemplate.update("DELETE FROM USERS");
        jdbcTemplate.update("DELETE FROM FILMS");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE FILMS ALTER COLUMN ID RESTART WITH 1");
    }

    @Test
    public void addAndRemoveLike(){
        MPA mpa = MPA.builder()
                .name("G")
                .id(1)
                .build();
        Film film = Film.builder()
                .id(1)
                .name("test film")
                .rate(1)
                .description("desc for test")
                .duration(1400)
                .mpa(mpa)
                .genres(Collections.emptyList())
                .releaseDate(LocalDate.of(2001,1,2))
                .build();
        User user = User.builder()
                .id(1)
                .name("Test")
                .login("TestLog")
                .email("test@yandex.ru")
                .birthday(LocalDate.of(2002, 2, 3))
                .build();
        userDbStorage.add(user);
        filmDbStorage.add(film);
        dao.addLike(film.getId(),user.getId());
        assertEquals(1, dao.getPopularFilms(10).size());
        dao.removeLike(film.getId(),user.getId());
        assertEquals(1, dao.getPopularFilms(10).size());
    }

}
