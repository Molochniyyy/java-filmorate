package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Qualifier
@Repository
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(long id) {
        checkId(id);
        String sqlQuery = "select * from USERS where ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public Collection<User> getUsers() {
        String sqlQuery = "select * from USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User add(User user) {
        checkUser(user);
        if (user.getName() == null || user.getName().length() == 0) {
            log.info("Пользователю с пустым именем был присвоен его логин {}", user.getLogin());
            user.setName(user.getLogin());
        }
        String sqlQuery = "insert into USERS (NAME, LOGIN, EMAIL, BIRTHDAY) values (?,?,?,?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getEmail());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, holder);
        String sql = "SELECT * FROM USERS ORDER BY ID DESC LIMIT 1";
        User user1 = jdbcTemplate.queryForObject(sql,this::mapRowToUser);
        assert user1 != null;
        return getUserById(user1.getId());
    }

    @Override
    public User update(User user) {
        checkUser(user);
        if (user.getName() == null || user.getName().length() == 0) {
            log.info("Пользователю с пустым именем был присвоен его логин {}", user.getLogin());
            user.setName(user.getLogin());
        }
        checkId(user.getId());
        String sqlQuery = "UPDATE USERS SET NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? where ID = ?";
        jdbcTemplate.update(sqlQuery, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday().toString(), user.getId());
        return getUserById(user.getId());
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

    private void checkId(long id) {
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

    private void checkUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Адрес электронной почты не должен быть равен null");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и не может содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
