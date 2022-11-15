package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class LikesDao {

    private final JdbcTemplate jdbcTemplate;
    private final MpaService mpaService;
    private final GenreService genreService;

    public LikesDao(JdbcTemplate jdbcTemplate, MpaService mpaService, GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    public void addLike(int filmId, int userId) {
        checkUserId(userId);
        checkFilmId(filmId);
        String sqlQuery = "insert into LIKES (FILM_ID, USER_ID) VALUES (?,?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        checkFilmId(filmId);
        checkUserId(userId);
        String sqlQuery = "delete from LIKES where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public List<Film> getPopularFilms(int count){
        String sqlQuery = "select f.*\n" +
                "from FILMS as f\n" +
                "join LIKES L on f.ID = L.FILM_ID\n" +
                "group by f.ID\n" +
                "order by COUNT(L.USER_ID) desc\n" +
                "LIMIT ?";
        List <Film> films = jdbcTemplate.query(sqlQuery,this::mapRowToFilm,count);
        if(films.size()==0){
            sqlQuery = "select * from FILMS";
            films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        }
        return films;
    }

    private void checkUserId(long id) {
        if (id > 0) {
            String sql = "select * from USERS";
            List<User> list = jdbcTemplate.query(sql, this::mapRowToUser);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == id) {
                    break;
                }
                if (i == list.size() - 1) {
                    throw new NotFoundException("Пользователь с таким id не найден.");
                }
            }
        } else {
            throw new NotFoundException("Пользователь должен быть с id > 0.");
        }
    }

    private void checkFilmId(long id){
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

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
