package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDao {
    private final JdbcTemplate jdbcTemplate;

    public MpaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MPA findMpaById(Integer id) {
        String sqlQuery = "select * from MPA where id = ?";
        MPA mpa;
        try {
            mpa = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("MPA c id " + id + " не найден");
        }
        return mpa;
    }

    public List<MPA> findAllMpa() {
        String sqlQuery = "select * from MPA";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    private MPA mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return MPA.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
