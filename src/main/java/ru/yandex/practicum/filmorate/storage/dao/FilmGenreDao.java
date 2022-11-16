package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class FilmGenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenreDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(long filmId, long genreId){
        String sql = "insert into FILM_GENRE values (?,?)";
        jdbcTemplate.update(sql, filmId, genreId);
    }

    public void clear(){
        String sql = "delete from FILM_GENRE";
        jdbcTemplate.update(sql);
    }

}
