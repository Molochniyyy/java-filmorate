package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDao {

    private final JdbcTemplate jdbcTemplate;

    public GenreDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre findGenreById(Integer id) {
        String sqlQuery = "select * from GENRES where id = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Genre с id = " + id + " не найден");
        }
        return genre;
    }

    public List<Genre> findAllGenres(){
        String sqlQuery = "select * from GENRES";
        return jdbcTemplate.query(sqlQuery,this::mapRowToGenre);
    }

    public List<Genre> findGenresByFilmId(int id){
        String sqlQuery = "select G2.* from FILM_GENRE\n" +
                "join GENRES G2 on G2.ID = FILM_GENRE.GENRE_ID\n" +
                "where FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery,this::mapRowToGenre,id);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
