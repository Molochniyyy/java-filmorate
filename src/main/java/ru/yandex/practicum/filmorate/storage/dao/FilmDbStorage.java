package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

@Qualifier
@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaService mpaService;
    private final GenreService genreService;

    private final FilmGenreDao filmGenreDao;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaService mpaService, GenreService genreService, FilmGenreDao filmGenreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaService = mpaService;
        this.genreService = genreService;
        this.filmGenreDao = filmGenreDao;
    }

    @Override
    public Film getFilmById(long id) {
        checkFilmId(id);
        String sqlQuery = "select * from FILMS where ID = ?";
        log.info("Получен фильм с id = {}", id);
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
    }

    @Override
    public Collection<Film> get() {
        String sqlQuery = "select * from FILMS";
        log.info("Получен список фильмов");
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film update(Film film) {
        check(film);
        checkFilmId(film.getId());
        String sqlQuery = "update FILMS set NAME = ?, DESCRIPTION = ?," +
                " RELEASE_DATE = ?, DURATION = ?, RATE = ?, MPA = ? where ID = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate().toString(),
                film.getDuration(), film.getRate(), film.getMpa().getId(), film.getId());
        if (film.getGenres() != null) {
            Set<Genre> genres= new LinkedHashSet<>(film.getGenres());
            film.getGenres().clear();
            film.getGenres().addAll(genres);
            filmGenreDao.clear();
            for (Genre genre : film.getGenres()) {
                filmGenreDao.add(film.getId(),genre.getId());
            }
            if (film.getGenres().size() == 0) {
                String sql1 = "select * from FILMS where ID = ?";
                Film film1 = jdbcTemplate.queryForObject(sql1, this::mapRowToFilm, film.getId());
                assert film1 != null;
                film1.getGenres().clear();
                filmGenreDao.clear();
                return film1;
            }
        }

        log.info("Обновлен фильм с id = {}", film.getId());
        return getFilmById(film.getId());
    }

    @Override
    public Film add(Film film) {
        check(film);
        String sqlQuery = "insert into FILMS (NAME, RELEASE_DATE, DESCRIPTION, MPA, DURATION, RATE) values (?,?,?,?,?,?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setDate(2, Date.valueOf(film.getReleaseDate()));
            ps.setString(3, film.getDescription());
            ps.setInt(4, Math.toIntExact(film.getMpa().getId()));
            ps.setInt(5, film.getDuration());
            ps.setInt(6, film.getRate());
            return ps;
        }, holder);
        int id = Objects.requireNonNull(holder.getKey()).intValue();
        film.setId(id);
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                filmGenreDao.add(film.getId(),genre.getId());
            }
        }
        String sql1 = "select * from FILMS order by ID desc limit 1";
        Film film1 = jdbcTemplate.queryForObject(sql1, this::mapRowToFilm);
        assert film1 != null;
        log.info("Добавлен фильм с id = {}", film1.getId());
        return film1;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .duration(resultSet.getInt("duration"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .description(resultSet.getString("description"))
                .rate(resultSet.getInt("rate"))
                .mpa(mpaService.findMpaById(resultSet.getInt("mpa")))
                .genres(genreService.findGenresByFilmId(resultSet.getInt("id")))
                .build();
    }

    private void checkFilmId(long id) {
        if (id > 0) {
            String sql = "select * from FILMS";
            List<Film> list = jdbcTemplate.query(sql, this::mapRowToFilm);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == id) {
                    break;
                }
                if (i == list.size() - 1) {
                    throw new NotFoundException("Фильм с таким id не найден.");
                }
            }
        } else {
            throw new NotFoundException("Фильм должен быть с id > 0.");
        }
    }

    private void check(Film film) {
        if (film.getName().length() == 0 || film.getName() == null) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не должна быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не должна быть отрицательной");
        }
    }
}
