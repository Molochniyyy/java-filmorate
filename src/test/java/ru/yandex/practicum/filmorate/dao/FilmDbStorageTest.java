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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage storage;

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
    public void createAndUpdateFilm(){
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
        assertEquals(film, storage.add(film),"Film не создался");
        film.setName("");
        assertThrows(ValidationException.class,()->storage.update(film),
                "Фильм обновился с неправильным именем");
        film.setDuration(-1);
        assertThrows(ValidationException.class,()->storage.update(film),
                "Фильм обновился с неправильной длительностью");
        film.setReleaseDate(LocalDate.of(1994,2,2));
        assertThrows(ValidationException.class,()->storage.update(film),
                "Фильм обновился с неправильной датой релиза");
    }

    @Test
    public void getFilm(){
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
        storage.add(film);
        assertEquals(1,storage.getFilmById(1).getId());
        assertThrows(NotFoundException.class, ()->storage.getFilmById(999));
    }

}
