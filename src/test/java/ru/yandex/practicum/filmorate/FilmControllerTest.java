package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    private FilmController fc;

    @BeforeEach
    public void beforeEach(){
        fc = new FilmController();
    }

    private Film getFilm(){
        return Film.builder()
                .duration(1000)
                .name("test")
                .description("test description")
                .releaseDate(LocalDate.of(2000,1,1))
                .build();
    }

    @Test
    public void shouldThrowValidationException_whenGetUnknownId() {
        Film film = getFilm();
        fc.add(film);
        Film updatedFilm = getFilm();
        updatedFilm.setId(0);
        assertThrows(ValidationException.class, () -> fc.update(updatedFilm));
    }

    @Test
    public void shouldThrowValidationException_whenNameIsEmpty(){
        Film film = getFilm();
        fc.add(film);
        film.setName("");
        assertThrows(ValidationException.class, () -> fc.update(film));
        film.setName(null);
        assertThrows(NullPointerException.class, () -> fc.update(film));
    }

    @Test
    public void shouldThrowValidationException_whenLengthOfDescriptionIsMoreThan200(){
        Film film = getFilm();
        fc.add(film);
        film.setDescription("1".repeat(201));
        assertThrows(ValidationException.class, () -> fc.update(film));
    }

    @Test
    public void shouldThrowValidationException_whenReleaseDateIsBeforeThan28thOfDecember1895(){
        Film film = getFilm();
        fc.add(film);
        film.setReleaseDate(LocalDate.of(1895,12,27));
        assertThrows(ValidationException.class, () -> fc.update(film));
    }

    @Test
    public void shouldThrowValidationException_whenDurationIsNegative(){
        Film film = getFilm();
        fc.add(film);
        film.setDuration(-100);
        assertThrows(ValidationException.class, () -> fc.update(film));
    }
}
