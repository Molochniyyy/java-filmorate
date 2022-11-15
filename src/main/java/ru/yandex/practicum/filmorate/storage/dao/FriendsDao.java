package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FriendsDao {

    private final JdbcTemplate jdbcTemplate;

    public FriendsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFriend(long userId, long friendId){
        checkId(userId);
        checkId(friendId);
        String sqlQuery = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) values (?,?)";
        jdbcTemplate.update(sqlQuery,userId,friendId);
    }

    public void deleteFriend(int userId, int friendId){
        checkId(userId);
        checkId(friendId);
        String sqlQuery = "DELETE FROM FRIENDS where USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery,userId,friendId);
    }

    public List<User> getFriends(int id){
        checkId(id);
        String sqlQuery = "select U.ID, U.NAME, U.EMAIL, U.LOGIN, U.BIRTHDAY\n" +
                "from FRIENDS\n" +
                "join USERS U on U.ID = FRIENDS.FRIEND_ID\n" +
                "where USER_ID = ?";
        return jdbcTemplate.query(sqlQuery,this::mapRowToUser, id);
    }

    public List<User> getMutualFriends(int userId, int otherId){
        checkId(userId);
        checkId(otherId);
        String sqlQuery = "select U.ID, U.NAME, U.EMAIL, U.LOGIN, U.BIRTHDAY\n" +
                "from FRIENDS as f1\n" +
                "join FRIENDS as f2\n" +
                "on f2.USER_ID = ?\n" +
                "and f2.FRIEND_ID = f1.FRIEND_ID\n" +
                "join USERS U on U.ID = f1.FRIEND_ID\n" +
                "where f1.USER_ID = ?;";
        return jdbcTemplate.query(sqlQuery,this::mapRowToUser, userId, otherId);
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
